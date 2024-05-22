package server;

import client.Data;
import client.PaintClient;
import javafx.scene.Parent;
import org.jfree.fx.Resizable;
import org.jfree.fx.ResizableCanvas;

import java.io.IOException;
import java.net.ServerSocket;

public class PaintClosedServer extends PaintServer {

    public PaintClosedServer(int port, Data data) throws IOException{
        super();
        super.serverSocket = new ServerSocket(port);
        super.data = data;
    }
}
