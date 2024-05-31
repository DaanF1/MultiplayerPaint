package server.serveraction;

import server.PaintServerCallback;

import java.io.Serializable;
import java.util.List;

public interface ServerAction extends Serializable {
    boolean use(PaintServerCallback paintServerCallback);
}
