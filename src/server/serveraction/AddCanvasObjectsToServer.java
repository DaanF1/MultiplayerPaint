package server.serveraction;

import canvas.CanvasObject;
import server.PaintServerCallback;

import java.util.ArrayList;
import java.util.List;

public class AddCanvasObjectsToServer implements ServerAction {
    private final ArrayList<CanvasObject> canvasObjectsToAdd;
    public AddCanvasObjectsToServer(ArrayList<CanvasObject> canvasObjectsToAdd) {
        this.canvasObjectsToAdd = canvasObjectsToAdd;
    }

    @Override
    public boolean use(PaintServerCallback paintServerCallback) {
        return paintServerCallback.getCanvasObjects().addAll(this.canvasObjectsToAdd);
    }
}
