package canvas.connectionstate;

import server.serveraction.ServerAction;

public interface ConnectionState {
    void add(ServerAction serverAction);
}
