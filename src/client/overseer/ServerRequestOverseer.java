package client.overseer;

import client.PaintClientCallback;
import client.clientaction.ClientAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerRequestOverseer implements Runnable {
    private final PaintClientCallback paintClientCallback;
    private final Socket clientSocket;

    public ServerRequestOverseer(Socket clientSocket, PaintClientCallback paintClientCallback) {
        this.clientSocket = clientSocket;
        this.paintClientCallback = paintClientCallback;
    }

    @Override
    public void run() {
        for (; ; ) {
            ObjectInputStream oIS = null;
            try {
                oIS = new ObjectInputStream(this.clientSocket.getInputStream());
                ClientAction clientAction = (ClientAction) oIS.readObject();
                clientAction.use(this.paintClientCallback);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
