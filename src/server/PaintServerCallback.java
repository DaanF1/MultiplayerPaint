package server;

import canvas.CanvasObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public interface PaintServerCallback {
    ArrayList<CanvasObject> getCanvasObjects();
    boolean notifyClients();
    boolean notifyClients(Socket harbingerClient);
    boolean openServer() throws IOException;
}
