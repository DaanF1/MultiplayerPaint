package client;

import canvas.CanvasObject;
import canvas.states.DrawState;
import canvas.states.EraseState;
import canvas.states.ItemState;
import canvas.states.PanState;
import client.clientaction.ClientAction;
import client.overseer.ServerHostRequestOverseer;
import client.overseer.ServerRequestOverseer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    public static Canvas canvas = new Canvas(400, 400);
    public static Socket clientSocket;
    private Thread serverListenerThread;
    private ItemState itemState = new PanState(); // Starting state is always not drawing!
    private BlockingQueue<ClientAction> clientActions;
    private BlockingQueue<ServerAction> serverActions;
    private Thread serverHostRequestOverseer;

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
//        clientSocket = new Socket("localhost", 9090);
//        if (clientSocket.isClosed())
//            return;
//        ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
//        try {
//            this.canvasObjects = (ArrayList<CanvasObject>) objectInputStream.readObject();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        serverListenerThread = new Thread(() -> {
//            ObjectInputStream oIS = null;
//            try {
//                oIS = new ObjectInputStream(clientSocket.getInputStream());
//                this.canvasObjects = (ArrayList<CanvasObject>) oIS.readObject();
//            } catch (ClassNotFoundException | IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
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
                TextField ipAdress = new TextField("Ip Adress");
                TextField port = new TextField("port");
                Button connect = new Button("ok");
                connect.setOnAction(e -> {
                    serverListenerThread = new Thread(new ServerRequestOverseer(ipAdress.getText(),Integer.parseInt(port.getText()), this));
                });

                connectionInput.getChildren().addAll(ipAdress,port,connect);

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
            changeState(new DrawState());
        });

        Button buttonSelectEraser = new Button("Select Eraser");
        buttonSelectEraser.setOnAction(event -> {
            changeState(new EraseState());
            // TODO erase
        });

        Button buttonDrawLine = new Button("Draw Line");
        buttonDrawLine.setOnAction(event -> {
            // TODO draw line
        });

        Button buttonSelectColor = new Button("Select Color");
        buttonSelectColor.setOnAction(event -> {
            // TODO select color
        });

        Button buttonColorCanvas = new Button("Color Canvas");
        buttonColorCanvas.setOnAction(event -> {
            // TODO color canvas
        });
        //#endregion

        // Configure Scene
        itemsBox.getChildren().addAll(buttonSelectMouse, buttonSelectPen, buttonSelectEraser, buttonDrawLine, buttonSelectColor, buttonColorCanvas);
        serverBox.getChildren().addAll(buttonHost, connectServer, buttonExit);
        mainPane.setTop(serverBox);
        mainPane.setCenter(canvas);
        mainPane.setRight(itemsBox);

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Multiplayer Paint");
        primaryStage.show();
        //#endregion

        // Canvas Events
        canvas.setOnMousePressed(e -> {
            mouseAction.mousePressed(e, this.itemState);
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

        FXGraphics2D g = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.canvasAction.draw(new FXGraphics2D(canvas.getGraphicsContext2D()), canvas, canvasObjects);
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                canvasAction.update((now - last) / 1000000000.0);
                last = now;
                canvasAction.draw(g, canvas, canvasObjects);
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