package canvas;

import java.io.Serializable;

public class Canvas extends javafx.scene.canvas.Canvas implements Serializable {
    public Canvas() {
    }

    public Canvas(double width, double height) {
        super(width, height);
    }
}
