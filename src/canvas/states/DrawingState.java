package canvas.states;

import client.PaintClient;

public abstract class DrawingState{
    // Get the DrawState
    public abstract DrawingState getState();
    // Draw
    public abstract void Drawing(PaintClient client);
    // Pan
    public abstract void Panning(PaintClient client);
    // Can we draw?
    public abstract boolean canDraw();
    // Can we pan?
    public abstract boolean canPan();
}
