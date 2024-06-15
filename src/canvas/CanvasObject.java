package canvas;

import java.io.Serializable;

public interface CanvasObject extends Drawable,Serializable {
    double getDistance(double mouseX, double mouseY);
    boolean equals(Object o);
    int hashCode();
}
