package canvas;

import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;

public class LineSegment implements CanvasObject {
    Point2D startPoint, endPoint;
    Color color;

    public LineSegment(double startX, double startY, double endX, double endY, Color color) {
        this.startPoint = new Point2D.Double(startX, startY);
        this.endPoint = new Point2D.Double(endX, endY);
        this.color = color;
    }

    public LineSegment(Point2D startPoint, Point2D endPoint, Color color) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;
    }

    @Override
    public void draw(FXGraphics2D g) {
        g.setColor(color);
        g.drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(), (int) endPoint.getY());
    }

    @Override
    public double getDistance(double mouseX, double mouseY){
        Point2D startToEnd = new Point2D.Double(endPoint.getX() - startPoint.getX(), endPoint.getY() - startPoint.getY());
        Point2D startToMouse = new Point2D.Double(mouseX - startPoint.getX(), mouseY - startPoint.getY());
        double proj = (startToMouse.getX()*startToEnd.getX()+startToMouse.getY()*startToEnd.getY());
        double length = startToEnd.distanceSq(0,0);
        double normalizedProjection = proj/length;
        return getPoint2D(startToEnd,normalizedProjection).distance(mouseX,mouseY);
    }

    private Point2D getPoint2D(Point2D startToEnd, double normalizedProjection){
        if (normalizedProjection <= 0){
            return startPoint;
        } else if (normalizedProjection >= 1) {
            return endPoint;
        } else {
            return new Point2D.Double(startPoint.getX() + startToEnd.getX() * normalizedProjection, startPoint.getY() + startToEnd.getY() * normalizedProjection);
        }
    }
}
