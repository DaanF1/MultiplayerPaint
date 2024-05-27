package scenes;

import canvas.states.DrawState;
import canvas.states.EraseState;
import canvas.states.ItemState;
import canvas.states.PanState;
import client.MouseAction;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.Socket;

public class ViewController{
    public static Socket clientSocket;
    private ItemState itemState = new PanState(); // Starting state is always not drawing!
    private MouseAction mouseAction = new MouseAction();

    public void changeState(ItemState newItemState) {
        this.itemState = newItemState;
    }

    public ItemState getItemState(){
        return this.itemState;
    }

    public void selectedMouse(ActionEvent actionEvent){
        changeState(new PanState());
    }

    public void selectedPen(ActionEvent actionEvent){
        changeState(new DrawState());
    }

    public void selectedEraser(ActionEvent actionEvent){
        changeState(new EraseState());
    }

    public void hostingServer(ActionEvent actionEvent){
//        try {
//            // Run new open server
//            Class<? extends Runnable> theClass = Class.forName(String.valueOf(paintServer)).asSubclass(Runnable.class);
//            Runnable instance = theClass.newInstance();
//            new Thread(instance).start();
//        } catch (InstantiationException in) {
//            System.out.println("Error: InstantiationException");
//            in.printStackTrace();
//        } catch (IllegalAccessException il) {
//            System.out.println("Error: IllegalAccessException");
//            il.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            System.out.println("Error: ClassNotFoundException");
//            e.printStackTrace();
//        }
    }

    public void canvasMousePressed(MouseEvent mouseEvent){
        //mouseAction.mousePressed(mouseEvent, getItemState());
    }

    public void canvasMouseDragged(MouseEvent mouseEvent){
        //mouseAction.mouseDragged(mouseEvent, canvasObjects, canvas, getItemState());
    }

    public void canvasMouseReleased(MouseEvent mouseEvent){
//        try {
//            mouseAction.mouseReleased(mouseEvent, clientSocket, canvasObjects, getItemState());
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    public void selectedServer(ActionEvent actionEvent){
    }

    public void exit(ActionEvent actionEvent){
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
