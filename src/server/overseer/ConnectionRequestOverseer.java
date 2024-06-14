package server.overseer;

import server.ClientNotifier;
import server.PaintServerCallback;
import server.serveraction.ServerAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ConnectionRequestOverseer implements Runnable {
    private Socket connection;
    private PaintServerCallback paintServer;

    public ConnectionRequestOverseer(Socket connection, PaintServerCallback paintServer) {
        this.connection = connection;
        this.paintServer = paintServer;
    }

    @Override
    public void run() {
        try {
            for(;;) {
                ObjectInputStream objectInputStream = new ObjectInputStream(this.connection.getInputStream());
                ServerAction serverAction = (ServerAction) objectInputStream.readObject();
                this.paintServer.notifyClients(serverAction.use(this.paintServer),this.connection);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
