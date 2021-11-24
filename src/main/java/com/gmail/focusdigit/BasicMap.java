package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;

public class BasicMap extends JPanel {
    protected Brick[][] map;
    protected int mode;

    public BasicMap(int rows, int columns, int brickWidth, int mode){
        this.setLayout(new GridLayout(rows,columns,1,1));
        this.setPreferredSize(new Dimension((brickWidth+1)*columns, (brickWidth+1)*rows));
        map=new Brick[rows][columns];
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                map[i][j] = new Brick(brickWidth, new Pair<Integer, Integer>(i,j));
                this.add(map[i][j]);
            }
        }
        setMode(mode);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.revalidate();
    }

    public void setBrick(Pair<Integer, Integer> place, Status status){
        getBrick(place).setStatus(status);
    }

    Brick getBrick(Pair<Integer, Integer> place) {
        return this.map[place.getFirst()][place.getSecond()];
    }

    public Brick[][] getMap() {
        return map;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        for(Brick[] bricks:map)
            for(Brick brick:bricks)
                brick.setMode(mode);
    }

    public void empty() {
        for(Brick[] bricks:map)
            for(Brick brick:bricks)
                brick.setStatus(Status.EMPTY);
    }
}
