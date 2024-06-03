package client.overseer;

import client.PaintClientCallback;
import client.clientaction.ClientAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerRequestOverseer implements Runnable{

    private final String host;
    private final int port;
    private final PaintClientCallback paintClientCallback;

    public ServerRequestOverseer(String host, int port, PaintClientCallback paintClientCallback) {
        this.host = host;
        this.port = port;
        this.paintClientCallback = paintClientCallback;
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("localhost", 9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(;;) {
            ObjectInputStream oIS = null;
            try {
                oIS = new ObjectInputStream(clientSocket.getInputStream());
                ClientAction clientAction = (ClientAction) oIS.readObject();
                clientAction.use(this.paintClientCallback);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
