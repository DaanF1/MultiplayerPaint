package server.serveraction;

import server.ClientNotifier;
import server.PaintServerCallback;

public class RemoveConnection implements ServerAction{

    @Override
    public ClientNotifier.NotificationType use(PaintServerCallback paintServerCallback) {
        return ClientNotifier.NotificationType.RemoveClient;
    }
}
