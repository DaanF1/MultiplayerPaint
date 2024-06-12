package canvas.connectionstate;

import server.serveraction.ServerAction;

import java.util.concurrent.BlockingQueue;

public class ThreadConnection implements ConnectionState{

    private final BlockingQueue<ServerAction> serverActions;

    public ThreadConnection(BlockingQueue<ServerAction> serverActions) {
        this.serverActions = serverActions;
    }

    @Override
    public void add(ServerAction serverAction) {
        this.serverActions.add(serverAction);
    }
}
