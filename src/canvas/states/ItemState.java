package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public interface ItemState {
    void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas);

    void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas);

    void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects);
}
