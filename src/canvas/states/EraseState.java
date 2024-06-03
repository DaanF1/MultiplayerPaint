package canvas.states;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import server.serveraction.AddCanvasObjectsToServer;
import server.serveraction.DeleteCanvasObjectsFromServer;
import server.serveraction.ServerAction;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class EraseState implements ItemState{
    private Point2D lastMousePosition;
    private Point2D currentMousePosition;
    private ArrayList<CanvasObject> toRemoveCanvasObjects;
    private ArrayList<CanvasObject> oldCanvasObjects;

    public EraseState() {
        this.toRemoveCanvasObjects = new ArrayList<>();
        this.oldCanvasObjects = new ArrayList<>();
    }

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
        this.oldCanvasObjects.addAll(toRemoveCanvasObjects);
        toRemoveCanvasObjects.clear();
        lastMousePosition = currentMousePosition;
    }

    @Override
    public void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects, BlockingQueue<ServerAction> serverActions) {
        if (this.oldCanvasObjects.isEmpty())
            return;
        serverActions.add(new DeleteCanvasObjectsFromServer(this.oldCanvasObjects));
    }
}
