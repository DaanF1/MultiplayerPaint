package client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import server.PaintClosedServer;
import server.PaintOpenServer;
import server.PaintServer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.net.Socket;

public class PaintClient extends Application{
    public Data data;
    public static PaintServer paintServer;
    public static Canvas canvas = new Canvas(400, 400);
    public Graphics2D g;
    public boolean allowDrawing = false;
    public boolean isDrawing = false;
    public Point2D mousePoint = new Point2D(0, 0);

    public static void main(String[] args){
        launch(PaintClient.class);
    }

    @Override
    public void init() {
        //TODO internal server starten

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
//        //#region Server Thread
//        new Thread(() -> {
//            try{
//                // Handle socket (closed server)
//                Socket closedServerSocket = new Socket("127.0.0.1", 1337);
//                System.out.println("Connection with closed server!");
//
//                // Get data from the server
//                data = new Data(); // server.getData()?
//
//            } catch (Exception e){
//                System.out.println("Something went wrong!");
//                e.printStackTrace();
//            }
//
//        }).start();
//        //#endregion

        //#region Application Structure
        BorderPane mainPane = new BorderPane();
        HBox serverBox = new HBox();
        VBox itemsBox = new VBox();

        //#region Buttons
        Button buttonHost = new Button("Host server");
        buttonHost.setOnAction(event -> {
            try {
                // Run new open server
                Class<? extends Runnable> theClass =
                        Class.forName(String.valueOf(paintServer)).asSubclass(Runnable.class);
                Runnable instance = theClass.newInstance();
                new Thread(instance).start();
            } catch (InstantiationException in){
                System.out.println("Error: InstantiationException");
                in.printStackTrace();
            } catch (IllegalAccessException il){
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

        Button buttonSelectMouse = new Button("Select Mouse");
        buttonSelectMouse.setOnAction(event -> {
            allowDrawing = false;
        });

        Button buttonSelectPen = new Button("Select Pen");
        buttonSelectPen.setOnAction(event -> {
            allowDrawing = true;
        });

        Button buttonSelectEraser = new Button("Select Eraser");
        Button buttonDrawLine = new Button("Draw Line");
        Button buttonSelectColor = new Button("Select Color");
        Button buttonColorCanvas = new Button("Color Canvas");
        //#endregion

        // Configure Scene
        itemsBox.getChildren().addAll(buttonSelectMouse, buttonSelectPen, buttonSelectEraser);
        serverBox.getChildren().addAll(buttonHost, paintServers, buttonExit);
        mainPane.setTop(serverBox);
        mainPane.setCenter(canvas);
        mainPane.setRight(itemsBox);

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Multiplayer Paint");
        primaryStage.show();
        //#endregion

        // Events
        canvas.setOnMouseMoved(e -> mousePressed(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));

        FXGraphics2D g = new FXGraphics2D(canvas.getGraphicsContext2D());
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g);
            }
        }.start();
    }

    private void draw(FXGraphics2D g){
        g.setTransform(new AffineTransform());
        g.setBackground(Color.white);
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        g.scale(1, 1);

        this.g = g;

        g.setColor(Color.black);
        for (Point point : data.getPoints()){
            point.draw(g);
        }
    }

    private void update(double deltaTime){
        data.update();
    }

    private void mousePressed(MouseEvent e)
    {
        if (allowDrawing){
            isDrawing = true;
            mousePoint = new Point2D(e.getX(), e.getY());
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        if (allowDrawing){
            isDrawing = false;
            mousePoint = new Point2D(e.getX(), e.getY());
        }
    }

    private void mouseDragged(MouseEvent e)
    {
        if (allowDrawing && isDrawing){
            // Get mouse location
            mousePoint = new Point2D(e.getX(), e.getY());
            // Add new Point
            Point newPoint = new Point(mousePoint);
            data.addPoint(newPoint);
        } else {
            // Pan
            java.awt.geom.Point2D newMousePoint = new java.awt.geom.Point2D.Double(e.getX(), e.getY());
            double translateX = canvas.getTranslateX();
            double translateY = canvas.getTranslateY();
            canvas.setTranslateX(translateX + newMousePoint.getX() - mousePoint.getX());
            canvas.setTranslateY(translateY + newMousePoint.getY() - mousePoint.getY());
        }
    }

}