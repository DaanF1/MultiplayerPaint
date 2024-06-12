package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public interface ItemState {
    void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas);

    void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas);

    void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, BlockingQueue<ServerAction> serverActions);
}
