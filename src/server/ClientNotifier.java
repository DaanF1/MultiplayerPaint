package server;

import canvas.CanvasObject;
import client.clientaction.ClientAction;
import client.clientaction.UpdateCanvasObjects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ClientNotifier {
    public boolean notifyClients(List<Socket> connections, ArrayList<CanvasObject> canvasObjects) {
        connections.stream().forEach(connection -> {
            try {
                new ObjectOutputStream(connection.getOutputStream()).writeObject(new UpdateCanvasObjects(canvasObjects));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public boolean notifyClients(Socket harbingerClient, List<Socket> connections, ArrayList<CanvasObject> canvasObjects, BlockingQueue<ClientAction> hostClientActions) {
        hostClientActions.add(new UpdateCanvasObjects(canvasObjects));
        connections.stream().filter(connection -> !connection.equals(harbingerClient)).forEach(connection -> {
            try {
                new ObjectOutputStream(connection.getOutputStream()).writeObject(new UpdateCanvasObjects(canvasObjects));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }
}
