package client;

import canvas.CanvasObject;
import canvas.LineSegment;
import javafx.scene.input.MouseEvent;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MouseAction {

    private Point2D lastMousePosition;
    private Point2D currentMousePosition;

    public MouseAction() {
    }

    public void mousePressed(MouseEvent e) {
        lastMousePosition = new Point2D.Double(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects) {
        currentMousePosition = new Point2D.Double(e.getX(), e.getY());
        canvasObjects.add(new LineSegment(lastMousePosition, currentMousePosition));
        lastMousePosition = currentMousePosition;
    }

    public void mouseReleased(MouseEvent e, Socket clientSocket, ArrayList<CanvasObject> canvasObjects) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(canvasObjects);
    }
}
