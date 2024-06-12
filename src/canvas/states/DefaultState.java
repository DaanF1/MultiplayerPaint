package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.ServerAction;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class DefaultState implements ItemState{
    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){ }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){ }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, BlockingQueue<ServerAction> serverActions){

    }
}
