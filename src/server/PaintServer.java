package server;

import canvas.CanvasObject;
import server.serveraction.ServerAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PaintServer implements Runnable, PaintServerCallback {
    private ArrayList<CanvasObject> canvasObjects;
    private Socket host;
    private ServerSocket serverSocket;

    public PaintServer(int port) throws IOException {
        this.canvasObjects = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (this.host == null) {
                this.host = this.serverSocket.accept();
                if (!this.host.getInetAddress().equals(InetAddress.getLoopbackAddress())) {
                    this.host.close();
                    this.host = null;
                }
            }

            ObjectOutputStream oos = new ObjectOutputStream(host.getOutputStream());
            oos.writeObject(this.canvasObjects);

            for (; ; ) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(host.getInputStream());
                    ServerAction serverAction = (ServerAction) objectInputStream.readObject();
                    serverAction.use(this);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean stop() {
        try {
            this.host.close();
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<CanvasObject> getCanvasObjects() {
        return this.canvasObjects;
    }
}
