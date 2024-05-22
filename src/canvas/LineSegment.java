package canvas;

import javafx.geometry.Point2D;
import org.jfree.fx.FXGraphics2D;

public class LineSegment implements Drawable {
    Point2D startPoint, endPoint;

    public LineSegment(double startX, double startY, double endX, double endY) {
        this.startPoint = new Point2D(startX, startY);
        this.endPoint = new Point2D(endX, endY);
    }

    public LineSegment(Point2D startPoint, Point2D endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void draw(FXGraphics2D g) {
        g.drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(), (int) endPoint.getY());
    }
}
