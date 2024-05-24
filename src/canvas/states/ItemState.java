package canvas.states;

public abstract class ItemState{
    // Get the DrawState
    public abstract ItemState getState();
    // Can we draw?
    public abstract boolean canDraw();
    // Can we pan?
    public abstract boolean canPan();
}
