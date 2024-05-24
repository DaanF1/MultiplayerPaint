package canvas.states;

public class EraseState extends ItemState{
    @Override
    public ItemState getState(){
        return this;
    }
    @Override
    public boolean canDraw(){
        return true;
    }
    @Override
    public boolean canPan(){
        return false;
    }
}
