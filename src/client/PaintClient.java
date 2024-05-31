package client;

import canvas.CanvasObject;
import canvas.states.DrawState;
import canvas.states.EraseState;
import canvas.states.ItemState;
import canvas.states.PanState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import server.PaintServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PaintClient extends Application {
    private ArrayList<CanvasObject> canvasObjects;
    private MouseAction mouseAction;
    private CanvasAction canvasAction;
    public static PaintServer paintServer;
    public static Thread serverThread;
    public static Canvas canvas = new Canvas(400, 400);
    public static Socket clientSocket;
    private ItemState itemState = new PanState(); // Starting state is always not drawing!

    public static void main(String[] args) {
        launch(PaintClient.class);
    }

    public void changeState(ItemState drawState) {
        this.itemState = drawState;
    }

    @Override
    public void init() throws IOException {
        this.mouseAction = new MouseAction();
        this.canvasAction = new CanvasAction();
        paintServer = new PaintServer(9090);
        serverThread = new Thread(paintServer);
        serverThread.start();
        clientSocket = new Socket("localhost", 9090);

        if (clientSocket.isClosed()) return;
        ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        try {
            this.canvasObjects = (ArrayList<CanvasObject>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            try {
                // Run new open server
                Class<? extends Runnable> theClass = Class.forName(String.valueOf(paintServer)).asSubclass(Runnable.class);
                Runnable instance = theClass.newInstance();
                new Thread(instance).start();
            } catch (InstantiationException in) {
                System.out.println("Error: InstantiationException");
                in.printStackTrace();
            } catch (IllegalAccessException il) {
                System.out.println("Error: IllegalAccessException");
                il.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error: ClassNotFoundException");
                e.printStackTrace();
            }
        });

        ComboBox<PaintServer> paintServers = new ComboBox<>();

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
        serverBox.getChildren().addAll(buttonHost, paintServers, buttonExit);
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
                mouseAction.mouseReleased(e,clientSocket,canvasObjects, this.itemState);
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
}