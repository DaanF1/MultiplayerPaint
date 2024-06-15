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

    public void draw(FXGraphics2D g, Canvas canvas, ArrayList<CanvasObject> canvasObjects, Color canvasColor, Color penColor) {
        g.setBackground(canvasColor);
        g.setTransform(new AffineTransform());
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        g.setColor(Color.gray);
        g.drawRect(1, 1, (int) canvas.getWidth() - 1, (int) canvas.getHeight() - 1);
        g.setColor(penColor);
        canvasObjects.forEach(drawable -> drawable.draw(g));
    }

    public void update(double deltaTime) {
    }
}
