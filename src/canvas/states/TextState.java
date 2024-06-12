package canvas.states;

import canvas.CanvasObject;
import canvas.connectionstate.ConnectionState;
import canvas.TextObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.AddCanvasObjectToServer;

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
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ConnectionState connectionState){
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        TextObject newTextObject = new TextObject(currentMousePosition.getX(), currentMousePosition.getY(), 15, 15, text, color);
        canvasObjects.add(newTextObject);
        connectionState.add(new AddCanvasObjectToServer(newTextObject));
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas){

    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, ConnectionState connectionState){

    }
}
