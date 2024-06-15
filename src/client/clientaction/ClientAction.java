package client.clientaction;

import client.PaintClientCallback;

import java.io.Serializable;

public interface ClientAction extends Serializable {
    boolean use(PaintClientCallback clientCallback);
}
