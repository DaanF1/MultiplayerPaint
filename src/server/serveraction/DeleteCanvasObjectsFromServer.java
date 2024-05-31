package server.serveraction;

import canvas.CanvasObject;
import server.PaintServerCallback;

import java.util.ArrayList;
import java.util.List;

public class DeleteCanvasObjectsFromServer implements ServerAction{
    private final ArrayList<CanvasObject> canvasObjectsToDelete;

    public DeleteCanvasObjectsFromServer(ArrayList<CanvasObject> canvasObjectsToDelete) {
        this.canvasObjectsToDelete = canvasObjectsToDelete;
    }

    @Override
    public boolean use(PaintServerCallback paintServerCallback) {
        return paintServerCallback.getCanvasObjects().removeAll(this.canvasObjectsToDelete);
    }
}
