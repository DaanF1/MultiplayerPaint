package canvas.connectionstate;

import server.serveraction.ServerAction;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketConnection implements ConnectionState{
    private final Socket clientSocket;

    public SocketConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void add(ServerAction serverAction) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
            objectOutputStream.writeObject(serverAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
