package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.ServerAction;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public interface ItemState {
    void mousePressed(MouseEvent e);

    void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas);

    void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, BlockingQueue<ServerAction> serverActions);
}
