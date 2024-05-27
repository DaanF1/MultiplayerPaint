package client;

import canvas.CanvasObject;
import canvas.LineSegment;
import canvas.states.ItemState;
import javafx.scene.input.MouseEvent;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MouseAction {
    public MouseAction() {
    }

    public void mousePressed(MouseEvent e, ItemState itemState) {
        itemState.mousePressed(e);
    }

    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, ItemState itemState) {
        itemState.mouseDragged(e,canvasObjects);
    }

    public void mouseReleased(MouseEvent e, Socket clientSocket, ArrayList<CanvasObject> canvasObjects, ItemState itemState) throws IOException {
        itemState.mouseReleased(e,canvasObjects);
    }
}
