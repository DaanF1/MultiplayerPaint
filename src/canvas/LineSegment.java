package canvas;

import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.Vector;

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

    @Override
    public void draw(FXGraphics2D g) {
        g.drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(), (int) endPoint.getY());
    }

    @Override
    public double getDistance(double mouseX, double mouseY){
        Point2D mousePoint = new Point2D.Double(mouseX, mouseY);
        Point2D startToEnd = new Point2D.Double(endPoint.getX() + startPoint.getX(), endPoint.getY() + startPoint.getY());
        Point2D startToMouse = new Point2D.Double(mousePoint.getX() - startPoint.getX(), mousePoint.getY() - startPoint.getY());
        Point2D closestPoint = getPoint2D(startToEnd, startToMouse);

        return mousePoint.distance(closestPoint);

//        Point2D linePoint = new Point2D.Double((endPoint.getX()+startPoint.getX())/2, (endPoint.getY()+startPoint.getY())/2);
//        return linePoint.distance(mouseX, mouseY);
    }

    private Point2D getPoint2D(Point2D startToEnd, Point2D startToMouse){
        Point2D projectedPoint = new Point2D.Double(startToEnd.getX() * startToMouse.getX(), startToEnd.getY() * startToMouse.getY());
        double lengthSQ = Math.sqrt(startToEnd.getX()) + Math.sqrt(startToEnd.getY());
        double distance = projectedPoint.getX()/lengthSQ + projectedPoint.getY()/lengthSQ;

        Point2D closestPoint;
        if (distance <= 0){
            closestPoint = startPoint;
            System.out.println("startpoint");
        } else if (distance >= 1) {
            System.out.println("endpoint");
            closestPoint = endPoint;
        } else {
            System.out.println("newpoint");
            closestPoint = new Point2D.Double(startPoint.getX() + startToEnd.getX() * distance, startPoint.getY() + startToEnd.getY() * distance);
        }
        return closestPoint;
    }
}
