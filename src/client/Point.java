package client;

import javafx.geometry.Point2D;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Point{
    private AffineTransform pointTransform;
    private Point2D point;

    public Point(Point2D point){
        this.point = point;
    }

    public void draw(FXGraphics2D g){
        // Draw Point
        g.setColor(Color.black);
        g.fill(new Ellipse2D.Double(point.getX()-5, point.getY()-5, 10, 10));
    }

}
