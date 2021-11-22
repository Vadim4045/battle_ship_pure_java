package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BasicMap extends JPanel {
    protected Brick[][] map;


    public BasicMap(int rows, int columns, int brickWidth){
        this.setLayout(new GridLayout(rows,columns,1,1));
        this.setPreferredSize(new Dimension((brickWidth+1)*columns, (brickWidth+1)*rows));
        map=new Brick[rows][columns];
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                map[i][j] = new Brick(brickWidth, new Pair<Integer, Integer>(i,j));
                this.add(map[i][j]);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.revalidate();
    }

    public Brick[][] getMap() {
        return map;
    }

    public void setMap(Brick[][] map) {
        this.map = map;
        this.repaint();
    }
}
