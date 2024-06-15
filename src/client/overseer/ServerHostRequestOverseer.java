package client.overseer;

import client.PaintClientCallback;
import client.clientaction.ClientAction;

import java.util.concurrent.BlockingQueue;

public class ServerHostRequestOverseer implements Runnable {
    private final PaintClientCallback clientCallback;
    private final BlockingQueue<ClientAction> clientActions;

    public ServerHostRequestOverseer(BlockingQueue<ClientAction> clientActions, PaintClientCallback clientCallback) {
        this.clientActions = clientActions;
        this.clientCallback = clientCallback;
    }

    @Override
    public void run() {
        try {
            for (; ; )
                this.clientActions.take().use(clientCallback);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
