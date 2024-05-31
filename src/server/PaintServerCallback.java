package server;

import canvas.CanvasObject;

import java.util.ArrayList;

public interface PaintServerCallback {
    ArrayList<CanvasObject> getCanvasObjects();
}
