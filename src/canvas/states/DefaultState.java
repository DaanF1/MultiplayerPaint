package canvas.states;

import canvas.CanvasObject;
import canvas.connectionstate.ConnectionState;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class DefaultState implements ItemState {
    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ConnectionState connectionState) {
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, ConnectionState connectionState) {
    }
}
