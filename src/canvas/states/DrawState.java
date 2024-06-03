package canvas.states;

import canvas.CanvasObject;
import canvas.LineSegment;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.AddCanvasObjectsToServer;
import server.serveraction.ServerAction;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class DrawState implements ItemState{
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;
    private ArrayList<CanvasObject> newLinesegments;

    public DrawState() {
        newLinesegments = new ArrayList<>();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects, Canvas canvas) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        LineSegment lineSegmentToAdd = new LineSegment(lastMousePosition, currentMousePosition);
        canvasObjects.add(lineSegmentToAdd);
        newLinesegments.add(lineSegmentToAdd);
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, BlockingQueue<ServerAction> serverActions) {
        if (this.newLinesegments.isEmpty())
            return;
        serverActions.add(new AddCanvasObjectsToServer(this.newLinesegments));
    }
}
