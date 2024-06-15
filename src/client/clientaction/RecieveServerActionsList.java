package client.clientaction;

import client.PaintClientCallback;
import server.serveraction.ServerAction;

import java.util.concurrent.BlockingQueue;

public class RecieveServerActionsList implements ClientAction{

    private final BlockingQueue<ServerAction> serverActions;

    public RecieveServerActionsList(BlockingQueue<ServerAction> serverActions) {
        this.serverActions = serverActions;
    }

    @Override
    public boolean use(PaintClientCallback clientCallback) {
        clientCallback.recieveServerActionsList(this.serverActions);
        return true;
    }
}
