package server;

import canvas.CanvasObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public interface PaintServerCallback {
    ArrayList<CanvasObject> getCanvasObjects();
    boolean notifyClients(ClientNotifier.NotificationType notificationType);
    boolean notifyClients(ClientNotifier.NotificationType notificationType, Socket harbingerClient);
    boolean openServer() throws IOException;
}
