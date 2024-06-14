package server.serveraction;

import server.ClientNotifier;
import server.PaintServerCallback;

import java.io.IOException;

public class OpenServer implements ServerAction{
    @Override
    public ClientNotifier.NotificationType use(PaintServerCallback paintServerCallback) {
        try {
            if (paintServerCallback.openServer())
                return ClientNotifier.NotificationType.None;
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
