package com.gmail.focusdigit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class EditMap extends BasicMap implements MouseMotionListener {
    EditFrame parent;
    private Pair<Integer, Integer> shipHolder;
    private int brickWidth;
    public EditMap(int rows, int columns, int brickWidth, EditFrame parent) {
        super(rows, columns, brickWidth);
        this.parent=parent;
        this.brickWidth=brickWidth;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(Brick[] bricks:map){
            for(Brick b:bricks){
                if(e.getXOnScreen()>b.getPlace().getFirst()*brickWidth
                        && e.getXOnScreen()<(b.getPlace().getFirst()+1)*brickWidth
                        && e.getYOnScreen()>b.getPlace().getSecond()*brickWidth
                        && e.getYOnScreen()<(b.getPlace().getSecond()+1)*brickWidth){
                    b.setStatus(Status.FILLED);
                }
            }
        }
    }
}
