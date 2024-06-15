package canvas.states;

import canvas.CanvasObject;
import canvas.connectionstate.ConnectionState;
import canvas.LineSegment;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.AddCanvasObjectsToServer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DrawState implements ItemState{
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;
    private ArrayList<CanvasObject> newLinesegments;
    private Color currentColor;

    public DrawState(Color color) {
        newLinesegments = new ArrayList<>();
        currentColor = color;
    }
    @Override
    public void mousePressed(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas, ConnectionState connectionState) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        LineSegment lineSegmentToAdd = new LineSegment(lastMousePosition, currentMousePosition, currentColor);
        canvasObjects.add(lineSegmentToAdd);
        newLinesegments.add(lineSegmentToAdd);
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, ConnectionState connectionState) {
        if (this.newLinesegments.isEmpty())
            return;
        connectionState.add(new AddCanvasObjectsToServer(this.newLinesegments));
    }
}
