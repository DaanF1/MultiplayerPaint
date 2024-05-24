package canvas;

import client.PaintClient;

public class NotDrawState extends DrawingState{
    @Override
    public void Drawing(PaintClient client){
        client.changeState(new DrawState());
    }
    @Override
    public void NotDrawing(PaintClient client){
        System.out.println("Not drawing!");
    }
    @Override
    public boolean getDrawState(){
        return false;
    }
}
