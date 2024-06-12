package client;

import canvas.*;
import canvas.states.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import server.PaintServer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PaintClient extends Application {
    // Controls:
    // Zoom-in: =
    // Zoom-out: -
    //TODO: Erase TextObjects (finetune)
    //TODO: Zoom in/out on cursor?
    private ArrayList<CanvasObject> canvasObjects;
    private MouseAction mouseAction;
    private CanvasAction canvasAction;
    public static PaintServer paintServer;
    public static Thread serverThread;
    public static Canvas canvas = new Canvas(600, 600);
    public static Socket clientSocket;
    private ItemState itemState = new DefaultState();
    private Color canvasColor;
    private Color color;
    private String textToDraw = "";

    public static void main(String[] args) {
        launch(PaintClient.class);
    }

    public void changeState(ItemState drawState) {
        this.itemState = drawState;
    }

    @Override
    public void init() throws IOException {
        paintServer = new PaintServer(9090);
        serverThread = new Thread(paintServer);
        serverThread.start();
        clientSocket = new Socket("localhost", 9090);
        this.mouseAction = new MouseAction();
        this.canvasAction = new CanvasAction();
        canvasColor = Color.white;
        color = Color.black;
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
        serverBox.getChildren().addAll(buttonHost, paintServers, buttonExit);
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
                mouseAction.mouseReleased(e,clientSocket,canvasObjects, this.itemState);
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
}