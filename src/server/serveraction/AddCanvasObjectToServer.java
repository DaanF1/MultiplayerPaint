package server.serveraction;

import canvas.CanvasObject;
import server.ClientNotifier;
import server.PaintServerCallback;

import java.util.ArrayList;

public class AddCanvasObjectToServer implements ServerAction {
    private final CanvasObject canvasObjectToAdd;
    public AddCanvasObjectToServer(CanvasObject canvasObjectToAdd) {
        this.canvasObjectToAdd = canvasObjectToAdd;
    }

    @Override
    public ClientNotifier.NotificationType use(PaintServerCallback paintServerCallback) {
        if (paintServerCallback.getCanvasObjects().add(this.canvasObjectToAdd))
            return ClientNotifier.NotificationType.CanvasObjectsUpdate;
        return null;
    }
}
