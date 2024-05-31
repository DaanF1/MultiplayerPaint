package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.net.Socket;
import java.util.ArrayList;

public interface ItemState {
    void mousePressed(MouseEvent e);

    void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas);

    void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Socket clientSocket);
}
