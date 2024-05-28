package client;

import canvas.CanvasObject;
import canvas.LineSegment;
import canvas.states.ItemState;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MouseAction {
    public MouseAction() { }

    public void mousePressed(MouseEvent e, ItemState itemState) {
        itemState.mousePressed(e);
    }

    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ItemState itemState) {
        itemState.mouseDragged(e,canvasObjects, canvas);
    }

    public void mouseReleased(MouseEvent e, Socket clientSocket, ArrayList<CanvasObject> canvasObjects, ItemState itemState) throws IOException {
        itemState.mouseReleased(e,canvasObjects);
    }
}
