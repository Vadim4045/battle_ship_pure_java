package com.gmail.focusdigit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameMap extends BasicMap implements MouseListener {
    App parent;

    public GameMap(int rows, int columns, int brickWidth, App parent){
        super(rows,columns,brickWidth);
        this.parent=parent;
        for(Brick[] bricks:map)
            for(Brick b:bricks)
                b.addMouseListener(this);
    }

    public void setBrick(Pair<Integer, Integer> place, Status status){
        getBrick(place).setStatus(status);
    }

    private Brick getBrick(Pair<Integer, Integer> place) {
        return this.map[place.getFirst()][place.getSecond()];
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Pair p = ((Brick)e.getSource()).getPlace();
        parent.nextStep(p, this);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
