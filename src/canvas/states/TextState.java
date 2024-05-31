package canvas.states;

import canvas.CanvasObject;
import canvas.TextObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TextState implements ItemState{
    private Point2D currentMousePosition;

    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        canvasObjects.add(new TextObject(currentMousePosition.getX(), currentMousePosition.getY(), 15, 15, ""));
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){

    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects){

    }
}
