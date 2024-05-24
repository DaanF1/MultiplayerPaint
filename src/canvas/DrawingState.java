package canvas;

import client.PaintClient;

public abstract class DrawingState{
    // If we can draw
    public abstract void Drawing(PaintClient client);
    // If we can't draw
    public abstract void NotDrawing(PaintClient client);
    // Get the DrawState
    public abstract boolean getDrawState();
}
