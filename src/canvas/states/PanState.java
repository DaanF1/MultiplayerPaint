package canvas.states;

public class PanState extends ItemState{
    @Override
    public ItemState getState(){
        return this;
    }
    @Override
    public boolean canDraw(){
        return false;
    }
    @Override
    public boolean canPan(){
        return true;
    }
}
