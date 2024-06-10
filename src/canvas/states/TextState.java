package canvas.states;

import canvas.CanvasObject;
import canvas.TextObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TextState implements ItemState{
    private Point2D currentMousePosition;
    private String text = "";
    private Color color;

    public TextState(String textToDraw, Color color){
        this.text = textToDraw;
        this.color = color;
    }

    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        TextObject newTextObject = new TextObject(currentMousePosition.getX(), currentMousePosition.getY(), 15, 15, text, color);
        canvasObjects.add(newTextObject);
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){

    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects){

    }
}
