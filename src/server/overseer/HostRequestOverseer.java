package server.overseer;

import server.PaintServerCallback;
import server.serveraction.ServerAction;

import java.util.concurrent.BlockingQueue;

public class HostRequestOverseer implements Runnable {
    private final BlockingQueue<ServerAction> hostServerActions;
    private final PaintServerCallback paintServerCallback;


    public HostRequestOverseer(BlockingQueue<ServerAction> hostServerActions, PaintServerCallback paintServerCallback) {
        this.hostServerActions = hostServerActions;
        this.paintServerCallback = paintServerCallback;
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                this.paintServerCallback.notifyClients(this.hostServerActions.take().use(paintServerCallback));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
