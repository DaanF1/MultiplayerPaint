package client;

import canvas.CanvasObject;
import server.serveraction.ServerAction;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public interface PaintClientCallback {
//    void recieveServerHostRequests(BlockingDeque<ClientAction> clientActions);
    ArrayList<CanvasObject> getCanvasObjects();
    void recieveServerActionsList(BlockingQueue<ServerAction> serverActions);
}
