package canvas;

import client.PaintClient;

public class DrawState extends DrawingState{
    @Override
    public void Drawing(PaintClient client){
        System.out.println("Drawing!");
    }
    @Override
    public void NotDrawing(PaintClient client){
        client.changeState(new NotDrawState());
    }
    @Override
    public boolean getDrawState(){
        return true;
    }
}
