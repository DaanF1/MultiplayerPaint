package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PanState implements ItemState{
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;
    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        canvas.setTranslateX(canvas.getTranslateX() + currentMousePosition.getX()-lastMousePosition.getX());
        canvas.setTranslateY(canvas.getTranslateY() + currentMousePosition.getY()-lastMousePosition.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects) {
        lastMousePosition = currentMousePosition;
    }
}
