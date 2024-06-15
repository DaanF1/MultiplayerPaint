package client;

import canvas.*;
import canvas.connectionstate.ConnectionState;
import canvas.connectionstate.SocketConnection;
import canvas.connectionstate.ThreadConnection;
import canvas.states.*;
import client.clientaction.ClientAction;
import client.overseer.ServerHostRequestOverseer;
import client.overseer.ServerRequestOverseer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import server.PaintServer;
import server.serveraction.OpenServer;
import server.serveraction.ServerAction;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

public class PaintClient extends Application implements PaintClientCallback {
    private ArrayList<CanvasObject> canvasObjects;
    private MouseAction mouseAction;
    private CanvasAction canvasAction;
    public static PaintServer paintServer;
    public static Thread serverThread;
    public static Canvas canvas = new Canvas(600, 600);
    public static Socket clientSocket;
    private Thread serverListenerThread;
    private ItemState itemState = new PanState(); // Starting state is always not drawing!
    private BlockingQueue<ClientAction> clientActions;
    private BlockingQueue<ServerAction> serverActions;
    private Thread serverHostRequestOverseer;
    private Color color = Color.black;
    private String textToDraw = "";
    private Color canvasColor = Color.white;
    private ConnectionState connectionState;
    private int portNumber = 0;
    private boolean isHosting = false;
    private boolean isInServer = false;
    public static void main(String[] args) {
        launch(PaintClient.class);
    }

    @Override
    public void init() throws IOException {
        this.canvasObjects = new ArrayList<>();
        this.clientActions = new LinkedBlockingQueue<>();
        this.serverHostRequestOverseer = new Thread(new ServerHostRequestOverseer(this.clientActions, this));
        this.serverHostRequestOverseer.start();
        this.mouseAction = new MouseAction();
        this.canvasAction = new CanvasAction();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        while (portNumber == 0) {
            final Stage portDialog = new Stage();
            portDialog.initModality(Modality.APPLICATION_MODAL);
            portDialog.initOwner(primaryStage);
            VBox portInputBox = new VBox(20);
            Label labelPort = new Label("Enter port number used for server: ");
            TextField ownPort = new TextField("9090");
            Button setPort = new Button("Set Port");
            setPort.setOnAction(e -> {
                if (!ownPort.getText().matches("-?\\d+")){
                    Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                    invalidAlert.setTitle("");
                    invalidAlert.setHeaderText("Error!");
                    invalidAlert.setContentText("The port number is invalid!");
                    invalidAlert.show();
                    return;
                }
                this.portNumber = Integer.parseInt(ownPort.getText());
                try {
                    paintServer = new PaintServer(portNumber, clientActions, this);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                serverThread = new Thread(paintServer);
                serverThread.start();

                portDialog.close();
            });
            portInputBox.getChildren().addAll(labelPort,ownPort,setPort);
            Scene dialogPortScene = new Scene(portInputBox, 300, 150);
            portDialog.setScene(dialogPortScene);
            portDialog.showAndWait();
        }

        //#region Application Structure
        BorderPane mainPane = new BorderPane();
        HBox serverBox = new HBox();
        VBox itemsBox = new VBox();

        //#region Server items
        Button buttonHost = new Button("Host Server");
        buttonHost.setOnAction(event -> {
            if (isHosting)
                return;

            this.serverActions.add(new OpenServer());
            // Go back to Default state
            changeState(new DefaultState());
            isHosting = true;
            isInServer = true;
        });

        Button connectServer = new Button("Connect To Server");
        connectServer.setOnAction(
            event -> {
                if (isHosting){
                    Alert alreadyHosting = new Alert(Alert.AlertType.ERROR);
                    alreadyHosting.setTitle("");
                    alreadyHosting.setHeaderText("Error!");
                    alreadyHosting.setContentText("You are already hosting a server. Please close this server before connecting again!");
                    alreadyHosting.show();
                    return;
                }

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox connectionInput = new VBox(20);
                TextField ipAdress = new TextField("127.0.0.1");
                TextField port = new TextField("9090");
                Button connect = new Button("Connect");
                Label textError = new Label("");
                connect.setOnAction(e -> {
                    if (!Regex.Validate_It(ipAdress.getText()) || !port.getText().matches("-?\\d+")) {
                        Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                        invalidAlert.setTitle("");
                        invalidAlert.setHeaderText("Error!");
                        invalidAlert.setContentText("The IP adress or port number is invalid!");
                        invalidAlert.show();
                        isInServer = false;
                        return;
                    }

                    this.serverHostRequestOverseer.interrupt();
                    serverThread.interrupt();
                    paintServer.stop();
                    try {
                        clientSocket = null;
                        clientSocket = new Socket(ipAdress.getText(),Integer.parseInt(port.getText()));
                        clientActions = null;
                        serverActions = null;
                    } catch (IOException ex) {
                        Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
                        invalidAlert.setTitle("");
                        invalidAlert.setHeaderText("Error!");
                        invalidAlert.setContentText("Cannot connect to server, or server is non-existent!");
                        invalidAlert.show();
                        isHosting = false;
                        return;
                    }
                    this.connectionState = new SocketConnection(clientSocket);
                    serverListenerThread = new Thread(new ServerRequestOverseer(clientSocket, this));
                    serverListenerThread.start();
                    // Go back to Default state
                    changeState(new DefaultState());
                    isInServer = true;
                });

                connectionInput.getChildren().addAll(ipAdress,port,connect,textError);

                Scene dialogScene = new Scene(connectionInput, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        );

        Button buttonExit = new Button("Exit Server");
        buttonExit.setOnAction(event -> {
            if (!isInServer || this.serverHostRequestOverseer.isInterrupted())
                return;

            try {
                this.serverActions.clear();
                this.clientActions = new LinkedBlockingQueue<>();
                this.connectionState = new ThreadConnection(serverActions);
                if (serverListenerThread != null) {
                    serverListenerThread.interrupt();
                }
                this.serverHostRequestOverseer = new Thread(new ServerHostRequestOverseer(this.clientActions, this));
                this.serverHostRequestOverseer.start();
                if (paintServer.getConnections().isEmpty() || isHosting) {
                    paintServer.getConnections().forEach(connection -> paintServer.disconnect(connection));
                    paintServer.stop();
                } else {
                    paintServer.disconnect(clientSocket);
                }
                serverThread.interrupt();
                paintServer = new PaintServer(this.portNumber, clientActions, this);
                serverThread = new Thread(paintServer);
                serverThread.start();
                // Clear the canvasObjects
                canvasObjects.clear();
                isHosting = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Go back to Default state
            changeState(new DefaultState());
            isInServer = false;
            System.out.println("Exited Server!");
        });
        //#endregion

        //#region Canvas Buttons
        Button buttonSelectMouse = new Button("Select Mouse");
        buttonSelectMouse.setOnAction(event -> {
            changeState(new PanState());
        });

        Button buttonSelectPen = new Button("Select Pen");
        buttonSelectPen.setOnAction(event -> {
            changeState(new DrawState(color));
        });

        Button buttonSelectEraser = new Button("Select Eraser");
        buttonSelectEraser.setOnAction(event -> {
            changeState(new EraseState());
        });

        Label labelTextToDraw = new Label("Text to draw: ");

        TextField textFieldToDraw = new TextField();
        textFieldToDraw.textProperty().addListener(observable -> {
            textToDraw = textFieldToDraw.getText();
        });

        Button buttonDrawText = new Button("Draw Text");
        buttonDrawText.setOnAction(event -> {
            changeState(new TextState(textToDraw, color));
        });

        Label labelPenColor = new Label("Select Draw Color:");
        ColorPicker selectPenColor = new ColorPicker();
        selectPenColor.setValue(javafx.scene.paint.Color.BLACK);
        selectPenColor.setOnAction(event -> {
            changeState(new DefaultState());
            // Change pen color
            javafx.scene.paint.Color sceneColor = selectPenColor.getValue();
            color = new Color((float) sceneColor.getRed(),
                    (float) sceneColor.getGreen(),
                    (float) sceneColor.getBlue(),
                    (float) sceneColor.getOpacity());
        });

        Label labelCanvasColor = new Label("Select Canvas Color:");
        ColorPicker selectCanvasColor = new ColorPicker();
        selectCanvasColor.setOnAction(event -> {
            changeState(new DefaultState());
            // Change canvas color
            javafx.scene.paint.Color sceneColor = selectCanvasColor.getValue();
            canvasColor = new Color((float) sceneColor.getRed(),
                    (float) sceneColor.getGreen(),
                    (float) sceneColor.getBlue(),
                    (float) sceneColor.getOpacity());
        });

        Label labelBackgroundColor = new Label("Select Background Color:");
        ColorPicker selectBackgroundColor = new ColorPicker();
        selectBackgroundColor.setOnAction(event -> {
            changeState(new DefaultState());
            // Change background color
            javafx.scene.paint.Color backgroundColor = selectBackgroundColor.getValue();
            double red = backgroundColor.getRed()*100;
            int rInt = (int) red;
            double green = backgroundColor.getGreen()*100;
            int gInt = (int) green;
            double blue = backgroundColor.getBlue()*100;
            int bInt = (int) blue;
            String hex = String.format("#%02X%02X%02X", rInt, gInt, bInt);
            mainPane.setStyle("-fx-background-color: " + hex + ";");
        });
        //#endregion

        // Configure Scene
        itemsBox.getChildren().addAll(buttonSelectMouse, buttonSelectPen, buttonSelectEraser, labelTextToDraw, textFieldToDraw, buttonDrawText, labelPenColor, selectPenColor, labelCanvasColor, selectCanvasColor, labelBackgroundColor, selectBackgroundColor);
        itemsBox.setStyle("-fx-background-color: #FFFFFF;");
        serverBox.getChildren().addAll(buttonHost, connectServer, buttonExit);
        serverBox.setStyle("-fx-background-color: #FFFFFF;");
        mainPane.setCenter(canvas);
        mainPane.setTop(serverBox);
        mainPane.setRight(itemsBox);

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Multiplayer Paint");
        primaryStage.show();
        //#endregion

        // Canvas Events
        canvas.setOnMousePressed(e -> {
            mouseAction.mousePressed(e, this.connectionState, this.canvasObjects, canvas, this.itemState);
        });

        canvas.setOnMouseDragged(e -> {
            mouseAction.mouseDragged(e,canvasObjects, canvas, this.itemState);
        });

        canvas.setOnMouseReleased(e -> {
            try {
                mouseAction.mouseReleased(e,this.connectionState,canvasObjects, this.itemState);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        canvas.setOnScroll(e -> {
           mouseAction.onScroll(e, canvas);
        });

        FXGraphics2D g = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.canvasAction.draw(new FXGraphics2D(canvas.getGraphicsContext2D()), canvas, canvasObjects, canvasColor, color);
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                canvasAction.update((now - last) / 1000000000.0);
                last = now;
                canvasAction.draw(g, canvas, canvasObjects, canvasColor, color);
            }
        }.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public void changeState(ItemState drawState) {
        this.itemState = drawState;
    }

    @Override
    public ArrayList<CanvasObject> getCanvasObjects() {
        return this.canvasObjects;
    }

    @Override
    public void recieveServerActionsList(BlockingQueue<ServerAction> serverActions) {
        this.serverActions = serverActions;
        this.connectionState = new ThreadConnection(serverActions);
    }

}