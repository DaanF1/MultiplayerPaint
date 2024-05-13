package server;

import org.jfree.fx.ResizableCanvas;

import canvas.Canvas;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PaintServer implements Runnable {
    private Canvas canvas;
    private Socket host;
    private ServerSocket serverSocket;

    public PaintServer(int port) throws IOException {
        this.canvas = new Canvas(1200,900);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(host.getOutputStream());
            oos.writeObject(this.canvas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
