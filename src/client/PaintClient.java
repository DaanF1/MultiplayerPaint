package client;

import canvas.*;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import server.PaintServer;
import server.serveraction.OpenServer;
import server.serveraction.ServerAction;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
        paintServer = new PaintServer(9090, clientActions, this);
        serverThread = new Thread(paintServer);
        serverThread.start();
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        //#region Application Structure
        BorderPane mainPane = new BorderPane();
        HBox serverBox = new HBox();
        VBox itemsBox = new VBox();

        //#region Server items
        Button buttonHost = new Button("Host server");
        buttonHost.setOnAction(event -> {
            this.serverActions.add(new OpenServer());
        });

        Button connectServer = new Button("Connect to server");
        connectServer.setOnAction(
            event -> {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox connectionInput = new VBox(20);
                TextField ipAdress = new TextField("Enter Ip Adress");
                TextField port = new TextField("Enter Port Number");
                Button connect = new Button("Connect");
                Label textError = new Label("");
                connect.setOnAction(e -> {
                    this.serverHostRequestOverseer.interrupt();
                    serverThread.interrupt();
                    paintServer.stop();
                    if (!ipAdress.getText().equalsIgnoreCase("Enter Ip Adress") && !ipAdress.getText().equalsIgnoreCase("") &&
                        port.getText().equalsIgnoreCase("Enter Port Number") && port.getText().equalsIgnoreCase("")) {
                        serverListenerThread = new Thread(new ServerRequestOverseer(ipAdress.getText(),Integer.parseInt(port.getText()), this));
                        if (serverListenerThread == null) {
                            textError.setText("Error, please fill in correct value(s)!");
                            return;
                        }
                        serverListenerThread.start();
                    }
                });

                connectionInput.getChildren().addAll(ipAdress,port,connect,textError);

                Scene dialogScene = new Scene(connectionInput, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        );

        Button buttonExit = new Button("Exit");
        buttonExit.setOnAction(event -> {
            System.out.println("Exiting application...");
            System.exit(0);
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
        selectPenColor.setOnAction(event -> {
            changeState(new DefaultState());
            // Change pen color
            javafx.scene.paint.Color sceneColor = selectPenColor.getValue();
            Color awtColor = new java.awt.Color((float) sceneColor.getRed(),
                    (float) sceneColor.getGreen(),
                    (float) sceneColor.getBlue(),
                    (float) sceneColor.getOpacity());
            color = awtColor;
        });

        Label labelCanvasColor = new Label("Select Canvas Color:");
        ColorPicker selectCanvasColor = new ColorPicker();
        selectCanvasColor.setOnAction(event -> {
            changeState(new DefaultState());
            // Change canvas color
            javafx.scene.paint.Color sceneColor = selectCanvasColor.getValue();
            Color awtColor = new java.awt.Color((float) sceneColor.getRed(),
                    (float) sceneColor.getGreen(),
                    (float) sceneColor.getBlue(),
                    (float) sceneColor.getOpacity());
            canvasColor = awtColor;
        });

        Label labelBackgroundColor = new Label("Select Background Color:");
        ColorPicker selectBackgroundColor = new ColorPicker();
        selectBackgroundColor.setOnAction(event -> {
            changeState(new DefaultState());
            // Change background color
            javafx.scene.paint.Color backgroundColor = selectBackgroundColor.getValue();
            Double red = backgroundColor.getRed()*100;
            int rInt = red.intValue();
            Double green = backgroundColor.getGreen()*100;
            int gInt = green.intValue();
            Double blue = backgroundColor.getBlue()*100;
            int bInt = blue.intValue();
            String hex = String.format("#%02X%02X%02X", rInt, gInt, bInt);
            mainPane.setStyle("-fx-background-color: " + hex + ";");
        });
        //#endregion

        // Configure Scene
        itemsBox.getChildren().addAll(buttonSelectMouse, buttonSelectPen, buttonSelectEraser, labelTextToDraw, textFieldToDraw, buttonDrawText, labelPenColor, selectPenColor, labelCanvasColor, selectCanvasColor, labelBackgroundColor, selectBackgroundColor);
        itemsBox.setStyle("-fx-background-color: #FFFFFF;");
        serverBox.getChildren().addAll(buttonHost, connectServer, buttonExit);
        serverBox.setStyle("-fx-background-color: #FFFFFF;");
        mainPane.setTop(serverBox);
        mainPane.setCenter(canvas);
        mainPane.setRight(itemsBox);

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Multiplayer Paint");
        primaryStage.show();
        //#endregion

        // Canvas Events
        canvas.setOnMousePressed(e -> {
            mouseAction.mousePressed(e, this.canvasObjects, canvas, this.itemState);
        });

        canvas.setOnMouseDragged(e -> {
            mouseAction.mouseDragged(e,canvasObjects, canvas, this.itemState);
        });

        canvas.setOnMouseReleased(e -> {
            try {
                mouseAction.mouseReleased(e,this.serverActions,canvasObjects, this.itemState);
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
    }

}