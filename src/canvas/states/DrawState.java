package canvas.states;

import client.PaintClient;

public class DrawState extends DrawingState{
    @Override
    public DrawingState getState(){
        return this;
    }
    @Override
    public void Drawing(PaintClient client){
        System.out.println("Drawing!");
    }
    @Override
    public void Panning(PaintClient client){
        client.changeState(new PanState());
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
