package client;

import canvas.*;
import canvas.states.DrawState;
import canvas.states.ItemState;
import canvas.states.EraseState;
import canvas.states.PanState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import scenes.ViewController;
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
    public static ViewController viewController = new ViewController();

    public static void main(String[] args) {
        launch(PaintClient.class);
    }

    @Override
    public void init() throws IOException {
        paintServer = new PaintServer(9090);
        serverThread = new Thread(paintServer);
        serverThread.start();
        clientSocket = new Socket("localhost", 9090);
        this.mouseAction = new MouseAction();
        this.canvasAction = new CanvasAction();
        if (clientSocket.isClosed())
            return;
        ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        try {
            this.canvasObjects = (ArrayList<CanvasObject>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader FXMLLoader = null;
        Parent root = FXMLLoader.load(this.getClass().getResource("/PaintClient.fxml"));


        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Multiplayer Paint");
        primaryStage.show();
        //#endregion

        // Canvas Events
        canvas.setOnMousePressed(e -> {
            mouseAction.mousePressed(e, viewController.getItemState());
        });

        canvas.setOnMouseDragged(e -> {
            mouseAction.mouseDragged(e,canvasObjects, canvas, viewController.getItemState());
        });

        canvas.setOnMouseReleased(e -> {
            try {
                mouseAction.mouseReleased(e,clientSocket,canvasObjects, viewController.getItemState());
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
}