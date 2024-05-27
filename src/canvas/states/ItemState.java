package canvas.states;

import canvas.CanvasObject;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public abstract class ItemState {
    // Get the DrawState
//    public abstract ItemState getState();
    // Can we draw?
//    public abstract boolean canDraw();
//    // Can we pan?
//    public abstract boolean canPan();
    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseDragged(MouseEvent e, ArrayList<CanvasObject> canvasObjects);

    public abstract void mouseReleased(MouseEvent e, ArrayList<CanvasObject> canvasObjects);
}
