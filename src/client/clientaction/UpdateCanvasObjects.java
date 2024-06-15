package client.clientaction;

import canvas.CanvasObject;
import client.PaintClientCallback;

import java.util.ArrayList;

public class UpdateCanvasObjects implements ClientAction {
    private final ArrayList<CanvasObject> canvasObjectsToUpdate;

    public UpdateCanvasObjects(ArrayList<CanvasObject> canvasObjectsToUpdate) {
        this.canvasObjectsToUpdate = canvasObjectsToUpdate;
    }

    @Override
    public boolean use(PaintClientCallback clientCallback) {
        clientCallback.getCanvasObjects().clear();
        clientCallback.getCanvasObjects().addAll(canvasObjectsToUpdate);
        return true;
    }
}
