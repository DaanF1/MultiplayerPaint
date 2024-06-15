package canvas.states;

import canvas.CanvasObject;
import canvas.connectionstate.ConnectionState;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.DeleteCanvasObjectsFromServer;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EraseState implements ItemState {
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;
    private ArrayList<CanvasObject> toRemoveCanvasObjects;
    private ArrayList<CanvasObject> oldCanvasObjects;

    public EraseState() {
        this.toRemoveCanvasObjects = new ArrayList<>();
        this.oldCanvasObjects = new ArrayList<>();
    }

    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ConnectionState connectionState) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        canvasObjects.forEach(drawable -> {
            if (drawable.getDistance(e.getX(), e.getY()) < 15) {
                toRemoveCanvasObjects.add(drawable);
            }
        });
        canvasObjects.removeAll(toRemoveCanvasObjects);
        this.oldCanvasObjects.addAll(toRemoveCanvasObjects);
        toRemoveCanvasObjects.clear();
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, ConnectionState connectionState) {
        if (this.oldCanvasObjects.isEmpty()) return;
        connectionState.add(new DeleteCanvasObjectsFromServer(this.oldCanvasObjects));
    }
}
