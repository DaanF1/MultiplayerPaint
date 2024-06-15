package server.serveraction;

import canvas.CanvasObject;
import server.ClientNotifier;
import server.PaintServerCallback;

import java.util.ArrayList;
import java.util.List;

public class AddCanvasObjectsToServer implements ServerAction {
    private final ArrayList<CanvasObject> canvasObjectsToAdd;
    public AddCanvasObjectsToServer(ArrayList<CanvasObject> canvasObjectsToAdd) {
        this.canvasObjectsToAdd = canvasObjectsToAdd;
    }

    @Override
    public ClientNotifier.NotificationType use(PaintServerCallback paintServerCallback) {
        if (paintServerCallback.getCanvasObjects().addAll(this.canvasObjectsToAdd))
            return ClientNotifier.NotificationType.CanvasObjectsUpdate;
        return null;
    }
}
