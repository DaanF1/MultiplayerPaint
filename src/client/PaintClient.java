package client;

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
    public static PaintServer paintServer;
    public static Canvas canvas = new Canvas(400, 400);
    public Graphics2D g;
    public boolean allowDrawing = false;
    public boolean isDrawing = false;
    public Point2D mousePoint = new Point2D(0, 0);
    public int panXOffset = 0;
    public Point2D panOffset = new Point2D(0, 0);

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
//                // Get components from the server
//                //canvas = closedServer.getResizableCanvas();
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

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void draw(FXGraphics2D g){
        g.setTransform(new AffineTransform());
        g.setBackground(Color.white);
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        g.scale(1, 1);

        this.g = g;
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
        if (allowDrawing){
            // Get mouse location
            mousePoint = new Point2D(e.getX(), e.getY());
            // Draw point
            g.setColor(Color.black);
            Ellipse2D point = new Ellipse2D.Double(mousePoint.getX()-5, mousePoint.getY()-5, 10, 10);
            g.fill(point);
        } else {
            // Pan

        }
    }

    private void panCanvas(){

    }

}