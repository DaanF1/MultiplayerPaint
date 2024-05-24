package canvas.states;

import client.PaintClient;

public class EraseState extends DrawingState{
    @Override
    public DrawingState getState(){
        return this;
    }
    @Override
    public void Drawing(PaintClient client){

    }
    @Override
    public void Panning(PaintClient client){

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
