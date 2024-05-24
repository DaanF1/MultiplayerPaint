package server;

import canvas.CanvasObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PaintServer implements Runnable {
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
                    this.canvasObjects = (ArrayList<CanvasObject>) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
