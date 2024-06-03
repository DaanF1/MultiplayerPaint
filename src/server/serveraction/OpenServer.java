package server.serveraction;

import server.PaintServerCallback;

import java.io.IOException;

public class OpenServer implements ServerAction{
    @Override
    public boolean use(PaintServerCallback paintServerCallback) {
        try {
            return paintServerCallback.openServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
