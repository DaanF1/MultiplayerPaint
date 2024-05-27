package scenes;

import canvas.states.DrawState;
import canvas.states.EraseState;
import canvas.states.ItemState;
import canvas.states.PanState;
import javafx.event.ActionEvent;

public class ViewController{
    private ItemState itemState = new PanState(); // Starting state is always not drawing!

    public void changeState(ItemState newItemState) {
        this.itemState = newItemState;
    }

    public ItemState getItemState(){
        return this.itemState;
    }

    public void selectedMouse(ActionEvent actionEvent){
        changeState(new PanState());
    }

    public void selectedPen(ActionEvent actionEvent){
        changeState(new DrawState());
    }

    public void selectedEraser(ActionEvent actionEvent){
        changeState(new EraseState());
    }

    public void hostingServer(ActionEvent actionEvent){
//        try {
//            // Run new open server
//            Class<? extends Runnable> theClass = Class.forName(String.valueOf(paintServer)).asSubclass(Runnable.class);
//            Runnable instance = theClass.newInstance();
//            new Thread(instance).start();
//        } catch (InstantiationException in) {
//            System.out.println("Error: InstantiationException");
//            in.printStackTrace();
//        } catch (IllegalAccessException il) {
//            System.out.println("Error: IllegalAccessException");
//            il.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            System.out.println("Error: ClassNotFoundException");
//            e.printStackTrace();
//        }
    }

    public void selectedServer(ActionEvent actionEvent){
    }

    public void exit(ActionEvent actionEvent){
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
