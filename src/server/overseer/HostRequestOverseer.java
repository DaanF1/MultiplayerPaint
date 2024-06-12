package server.overseer;

import server.PaintServerCallback;
import server.serveraction.ServerAction;

import java.util.concurrent.BlockingQueue;

public class HostRequestOverseer implements Runnable{
    private BlockingQueue<ServerAction> hostServerActions;
    private PaintServerCallback paintServerCallback;


    public HostRequestOverseer(BlockingQueue<ServerAction> hostServerActions, PaintServerCallback paintServerCallback) {
        this.hostServerActions = hostServerActions;
        this.paintServerCallback = paintServerCallback;
    }

    @Override
    public void run() {
        try {
            for (;;) {
                if (this.hostServerActions.take().use(paintServerCallback))
                    this.paintServerCallback.notifyClients();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
