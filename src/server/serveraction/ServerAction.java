package server.serveraction;

import server.ClientNotifier;
import server.PaintServerCallback;

import java.io.Serializable;

public interface ServerAction extends Serializable {
    ClientNotifier.NotificationType use(PaintServerCallback paintServerCallback);
}
