package canvas;

import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

public class TextObject implements CanvasObject {
    private final Point2D leftBottomPoint;
    private final Point2D topRightPoint;
    private final Color color;
    private final String text;
    private int textLength = 0;

    public TextObject(double leftBottomPointX, double leftBottomPointY, double topRightPointX, double topRightPointY, String text, Color color) {
        this.leftBottomPoint = new Point2D.Double(leftBottomPointX, leftBottomPointY);
        this.topRightPoint = new Point2D.Double(topRightPointX, topRightPointY);
        this.text = text;
        this.textLength = this.text.length();
        this.color = color;
    }

    @Override
    public void draw(FXGraphics2D g) {
        g.setColor(color);
        g.drawRect((int) this.leftBottomPoint.getX() - 1, (int) this.leftBottomPoint.getY() - (int) (this.topRightPoint.getY()), (int) this.topRightPoint.getX() + (7 * textLength), (int) (this.topRightPoint.getY() + 6));
        g.drawString(text, (int) this.leftBottomPoint.getX(), (int) this.leftBottomPoint.getY());
    }

    @Override
    public double getDistance(double mouseX, double mouseY) {
        Point2D startToEnd = new Point2D.Double(topRightPoint.getX() - leftBottomPoint.getX(), topRightPoint.getY() - leftBottomPoint.getY());
        Point2D startToMouse = new Point2D.Double(mouseX - leftBottomPoint.getX(), mouseY - leftBottomPoint.getY());
        double proj = (startToMouse.getX() * startToEnd.getX() + startToMouse.getY() * startToEnd.getY());
        double length = startToEnd.distanceSq(0, 0);
        double normalizedProjection = proj / length;
        return getPoint2D(startToEnd, normalizedProjection).distance(mouseX, mouseY);
    }

    private Point2D getPoint2D(Point2D startToEnd, double normalizedProjection) {
        if (normalizedProjection <= 0) {
            return leftBottomPoint;
        } else if (normalizedProjection >= 1) {
            return topRightPoint;
        } else {
            return new Point2D.Double(leftBottomPoint.getX() + startToEnd.getX() * normalizedProjection, leftBottomPoint.getY() + startToEnd.getY() * normalizedProjection);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextObject that = (TextObject) o;
        return textLength == that.textLength && Objects.equals(leftBottomPoint, that.leftBottomPoint) && Objects.equals(topRightPoint, that.topRightPoint) && Objects.equals(color, that.color) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftBottomPoint, topRightPoint, color, text, textLength);
    }
}
