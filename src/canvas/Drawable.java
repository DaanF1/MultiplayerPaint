package canvas;

import org.jfree.fx.FXGraphics2D;

import java.io.Serializable;

public interface Drawable extends Serializable {
    void draw(FXGraphics2D g);
}
