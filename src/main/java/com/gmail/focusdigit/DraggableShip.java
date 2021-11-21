package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DraggableShip extends JPanel {
    private boolean drag;
    private boolean vertical;
    private int brickWith;
    private Pair<Integer,Integer> place;
    private Pair<Integer,Integer> dimensios;
    private Brick[] bricks;

    public DraggableShip(Pair<Integer, Integer> place, int columns, int brickWidth) {
        this.place=place;
        this.brickWith=brickWidth;
        vertical=false;
        bricks = new Brick[columns];

        this.setLayout(null);

        for(int i=0;i<columns;i++){
            bricks[i] = new Brick(brickWidth, new Pair<Integer, Integer>(0,i));
            this.add(bricks[i]);
            bricks[i].setStatus(Status.FILLED);
        }
        refreshMap();
    }

    public boolean isDrag() {
        return drag;
    }

    public void setDrag(boolean drag) {
        this.drag = drag;
    }

    public Pair<Integer, Integer> getPlace() {
        return place;
    }

    public void setPlace(Pair<Integer, Integer> place) {
        this.place = new Pair<>(place);
    }

    public Pair<Integer, Integer> getDimensios() {
        return dimensios;
    }

    public void setDimensios(Pair<Integer, Integer> dimensios) {
        this.dimensios = new Pair<>(dimensios);
    }

    public void setVertical(boolean b) {
        this.vertical=vertical;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void refreshMap(){

            for(int i=0;i<bricks.length;i++) {
                if (vertical) {
                    bricks[i].setBounds(0, i * (brickWith + 1), brickWith + 1, brickWith + 1);
                } else {
                    bricks[i].setBounds(i * (brickWith + 1), 0, brickWith + 1, brickWith + 1);
                }
            }
        if(dimensios!=null && place!=null) {
            this.setBounds(place.getFirst()
                    , place.getSecond()
                    , dimensios.getFirst()
                    , dimensios.getSecond());
        }
    }

    //
    public   void splitBounds(){
        int tmp = dimensios.getFirst();
        dimensios.setFirst(dimensios.getSecond());
        dimensios.setSecond(tmp);

        if(dimensios.getFirst()>dimensios.getSecond()){
            vertical=false;
            place.setFirst(place.getFirst()-dimensios.getFirst()/2);
        }else {
            vertical=true;
            place.setFirst(place.getFirst() + dimensios.getSecond()/2);
        }
        refreshMap();
    }

    public Brick[] getMap() {
        return bricks;
    }
}
