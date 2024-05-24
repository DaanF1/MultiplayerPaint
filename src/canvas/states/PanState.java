package canvas.states;

import client.PaintClient;

public class PanState extends DrawingState{
    @Override
    public DrawingState getState(){
        return this;
    }
    @Override
    public void Drawing(PaintClient client){
        client.changeState(new DrawState());
    }
    @Override
    public void Panning(PaintClient client){
        System.out.println("Not drawing!");
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
