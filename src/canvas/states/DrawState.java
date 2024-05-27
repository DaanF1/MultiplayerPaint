package canvas.states;

import canvas.CanvasObject;
import canvas.LineSegment;
import javafx.scene.input.MouseEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DrawState extends ItemState{
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;

    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        canvasObjects.add(new LineSegment(lastMousePosition, currentMousePosition));
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects) {

    }
}
