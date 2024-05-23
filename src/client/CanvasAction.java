package client;

import canvas.CanvasObject;
import javafx.scene.canvas.Canvas;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class CanvasAction {
    public CanvasAction() {
    }

    public void draw(FXGraphics2D g, Canvas canvas, ArrayList<CanvasObject> canvasObjects) {
        g.setTransform(new AffineTransform());
        g.setBackground(Color.white);
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        canvasObjects.forEach(drawable -> drawable.draw(g));
    }

    public void update(double deltaTime) {
    }
}
