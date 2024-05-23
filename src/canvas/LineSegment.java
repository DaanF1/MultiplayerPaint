package canvas;

import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;

public class LineSegment implements CanvasObject {
    Point2D startPoint, endPoint;

    public LineSegment(double startX, double startY, double endX, double endY) {
        this.startPoint = new Point2D.Double(startX, startY);
        this.endPoint = new Point2D.Double(endX, endY);
    }

    public LineSegment(Point2D startPoint, Point2D endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void draw(FXGraphics2D g) {
        g.drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(), (int) endPoint.getY());
    }
}
