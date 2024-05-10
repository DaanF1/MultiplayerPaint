package server;

import org.jfree.fx.ResizableCanvas;
import java.net.ServerSocket;

public abstract class PaintServer{
    protected ResizableCanvas canvas;
    protected ServerSocket serverSocket;
}
