package client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import server.PaintClosedServer;
import server.PaintOpenServer;
import server.PaintServer;

import java.net.Socket;

public class PaintClient extends Application{
    public static PaintServer paintServer;
    public static ResizableCanvas canvas;

    public static void main(String[] args){
        launch(PaintClient.class);
    }

    @Override
    public void init() {
        //TODO internal server starten

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //#region Server Thread
        new Thread(() -> {
            try{
                // Handle socket (closed server)
                Socket closedServerSocket = new Socket("127.0.0.1", 1337);
                System.out.println("Connection with closed server!");

                // Get components from the server
                //canvas = closedServer.getResizableCanvas();

            } catch (Exception e){
                System.out.println("Something went wrong!");
                e.printStackTrace();
            }

        }).start();
        //#endregion

        //#region Application Structure
        BorderPane mainPane = new BorderPane();
        HBox serverBox = new HBox();

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
        //paintServers.setItems(FXCollections.observableList());
//        Button buttonJoin = new Button("Join server");
//        buttonJoin.setOnAction(event -> {
//            // Try connecting to a server
//        });
        Button buttonExit = new Button("Exit");
        buttonExit.setOnAction(event -> {
            System.out.println("Exiting application...");
            System.exit(0);
        });
        //#endregion

        // Configure Scene
        serverBox.getChildren().addAll(buttonHost, paintServers, buttonExit);
        mainPane.setTop(serverBox);
        mainPane.setCenter(canvas);

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Multiplayer Paint");
        primaryStage.show();
        //#endregion
    }

    private void draw(FXGraphics2D g){
        // Draw items
        //g.drawLine(0, 0, 100, 100);
    }
}