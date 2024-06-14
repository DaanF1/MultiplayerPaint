package server.serveraction;

import canvas.CanvasObject;
import server.ClientNotifier;
import server.PaintServerCallback;

import java.util.ArrayList;
import java.util.List;

public class DeleteCanvasObjectsFromServer implements ServerAction{
    private final ArrayList<CanvasObject> canvasObjectsToDelete;

    public DeleteCanvasObjectsFromServer(ArrayList<CanvasObject> canvasObjectsToDelete) {
        this.canvasObjectsToDelete = canvasObjectsToDelete;
    }

    @Override
    public ClientNotifier.NotificationType use(PaintServerCallback paintServerCallback) {
        if (paintServerCallback.getCanvasObjects().removeAll(this.canvasObjectsToDelete))
            return ClientNotifier.NotificationType.CanvasObjectsUpdate;
        return null;
    }
}
