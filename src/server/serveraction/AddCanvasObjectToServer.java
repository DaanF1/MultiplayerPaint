package server.serveraction;

import canvas.CanvasObject;
import server.PaintServerCallback;

import java.util.ArrayList;

public class AddCanvasObjectToServer implements ServerAction {
    private final CanvasObject canvasObjectToAdd;
    public AddCanvasObjectToServer(CanvasObject canvasObjectToAdd) {
        this.canvasObjectToAdd = canvasObjectToAdd;
    }

    @Override
    public boolean use(PaintServerCallback paintServerCallback) {
        return paintServerCallback.getCanvasObjects().add(this.canvasObjectToAdd);
    }
}
