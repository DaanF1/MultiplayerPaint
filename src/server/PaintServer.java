package server;

import client.Data;
import org.jfree.fx.ResizableCanvas;
import java.net.ServerSocket;

public abstract class PaintServer{
    protected Data data;
    protected ServerSocket serverSocket;
    public Data getData(){
        return this.data;
    }
    public void setData(Data data){
        this.data = data;
    }
}
