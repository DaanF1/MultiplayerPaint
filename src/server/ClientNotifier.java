package server;

import client.clientaction.ClientAction;
import client.clientaction.UpdateCanvasObjects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ClientNotifier {
    public enum NotificationType {CanvasObjectsUpdate, RemoveClient, None}

    private boolean CanvasObjectsUpdate(List<Socket> connections, PaintServerCallback paintServerCallback) {
        connections.stream().forEach(connection -> {
            try {
                new ObjectOutputStream(connection.getOutputStream()).writeObject(new UpdateCanvasObjects(paintServerCallback.getCanvasObjects()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    private boolean CanvasObjectsUpdate(Socket harbingerClient, List<Socket> connections, BlockingQueue<ClientAction> hostClientActions, PaintServerCallback paintServerCallback) {
        hostClientActions.add(new UpdateCanvasObjects(paintServerCallback.getCanvasObjects()));
        connections.stream().filter(connection -> !connection.equals(harbingerClient)).forEach(connection -> {
            try {
                new ObjectOutputStream(connection.getOutputStream()).writeObject(new UpdateCanvasObjects(paintServerCallback.getCanvasObjects()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public boolean notifyClients(List<Socket> connections, PaintServerCallback paintServerCallback, NotificationType notificationType) {
        switch (notificationType) {
            case CanvasObjectsUpdate:
                CanvasObjectsUpdate(connections, paintServerCallback);
                return true;
            case RemoveClient:
                return true;
            case None:
                return true;
            default:
                return false;
        }
    }

    public boolean notifyClients(Socket harbingerClient, List<Socket> connections, BlockingQueue<ClientAction> hostClientActions, PaintServerCallback paintServerCallback, NotificationType notificationType) {
        switch (notificationType) {
            case CanvasObjectsUpdate:
                return CanvasObjectsUpdate(harbingerClient, connections, hostClientActions, paintServerCallback);
            case RemoveClient:
                return connections.remove(harbingerClient);
            case None:
                return true;
            default:
                return false;
        }
    }
}
