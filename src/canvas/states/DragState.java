package canvas.states;

import canvas.CanvasObject;
import canvas.ConnectionState;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.ServerAction;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class DragState implements ItemState{
    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ConnectionState connectionState){

    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){

    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, ConnectionState connectionState){

    }
}
