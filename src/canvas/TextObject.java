package canvas;

import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;

public class TextObject implements CanvasObject{
    Point2D leftBottomPoint, topRightPoint;
    String text;
    int textLength = 0;
    boolean changingText = true;

    public TextObject(double leftBottomPointX, double leftBottomPointY, double topRightPointX, double topRightPointY, String text){
        this.leftBottomPoint = new Point2D.Double(leftBottomPointX, leftBottomPointY);
        this.topRightPoint = new Point2D.Double(topRightPointX, topRightPointY);
        this.text = text;
    }

    public void addCharacter(char newCharacter){
        this.text = text+newCharacter;
    }

    public void deleteCharacter(){
        this.text = String.valueOf(text.length()-1);
    }

    @Override
    public void draw(FXGraphics2D g){
        g.drawRect((int) this.leftBottomPoint.getX()-1, (int) this.leftBottomPoint.getY()-(int) (this.topRightPoint.getY()), (int) this.topRightPoint.getX() + (10*textLength), (int) (this.topRightPoint.getY()+6));
        g.drawString(text, (int) this.leftBottomPoint.getX(), (int) this.leftBottomPoint.getY());
    }

    @Override
    public double getDistance(double mouseX, double mouseY){
        Point2D startToEnd = new Point2D.Double(topRightPoint.getX() - leftBottomPoint.getX(), topRightPoint.getY() - leftBottomPoint.getY());
        Point2D startToMouse = new Point2D.Double(mouseX - leftBottomPoint.getX(), mouseY - leftBottomPoint.getY());
        double proj = (startToMouse.getX()*startToEnd.getX()+startToMouse.getY()*startToEnd.getY());
        double length = startToEnd.distanceSq(0,0);
        double normalizedProjection = proj/length;
        return getPoint2D(startToEnd,normalizedProjection).distance(mouseX,mouseY);
    }

    private Point2D getPoint2D(Point2D startToEnd, double normalizedProjection){
        if (normalizedProjection <= 0){
            return leftBottomPoint;
        } else if (normalizedProjection >= 1) {
            return topRightPoint;
        } else {
            return new Point2D.Double(leftBottomPoint.getX() + startToEnd.getX() * normalizedProjection, leftBottomPoint.getY() + startToEnd.getY() * normalizedProjection);
        }
    }
}
