package client;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Data{
    private ArrayList<Point> drawnPoints;

    public Data(){
        this.drawnPoints = new ArrayList<>();
    }

    public void update(){

    }

    public ArrayList<Point> getPoints(){
        return this.drawnPoints;
    }

    public void addPoint(Point point){
        this.drawnPoints.add(point);
    }

}
