package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EraseState implements ItemState{
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;
    ArrayList<CanvasObject> toRemoveCanvasObjects = new ArrayList<>();
    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        canvasObjects.forEach(drawable -> {
                if (drawable.getDistance(e.getX(), e.getY()) < 15){
                    toRemoveCanvasObjects.add(drawable);
                }
        });
        canvasObjects.removeAll(toRemoveCanvasObjects);
        toRemoveCanvasObjects.clear();
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects) {

    }
}
