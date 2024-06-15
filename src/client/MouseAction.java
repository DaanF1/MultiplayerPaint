package client;

import canvas.CanvasObject;
import canvas.connectionstate.ConnectionState;
import canvas.states.ItemState;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.io.IOException;
import java.util.ArrayList;

public class MouseAction {
    private Camera camera = new Camera();

    public MouseAction() {
    }

    public void mousePressed(MouseEvent e, ConnectionState connectionState, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ItemState itemState) {
        itemState.mousePressed(e, canvasObjects, canvas, connectionState);
    }

    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ItemState itemState) {
        itemState.mouseDragged(e, canvasObjects, canvas);
    }

    public void mouseReleased(MouseEvent e, ConnectionState connectionState, ArrayList<CanvasObject> canvasObjects, ItemState itemState) throws IOException {
        itemState.mouseReleased(e, canvasObjects, connectionState);
    }

    public void onScroll(ScrollEvent sE, Canvas canvas) {
        double deltaY = sE.getDeltaY();
        if (deltaY < 0) {
            camera.cameraZoomOut(canvas);
        } else if (deltaY > 0) {
            camera.cameraZoomIn(canvas);
        }
    }
}
