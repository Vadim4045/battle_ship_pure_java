package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MiniMap extends BasicMap implements MouseListener {
    private boolean vertical;
    private EditFrame parent;
    JComponent component;

    public MiniMap(int rows, int columns, int brickWidth, EditFrame parent) {
        super(rows, columns, brickWidth);
        this.parent=parent;
        vertical=true;
        setVertical();
    }

    public boolean isVertical() {
        return vertical;
    }

    public void clearMap(){
        for(Brick[] brick:map){
            for(Brick b:brick){
                b.setStatus(Status.EMPTY);
                b.removeMouseListener(this);
            }
        }
    }

    public void refreshMap(){
        if(vertical) {
            for (Brick[] brick : map) {
                brick[map.length / 2].setStatus(Status.FILLED);
                brick[map.length / 2].addMouseListener(this);
            }
        }
        else {
            for (Brick b : map[map.length / 2]) {
                b.setStatus(Status.FILLED);
                b.addMouseListener(this);
            }
        }
    }

    public void setVertical() {
        vertical = !vertical;
        clearMap();
        refreshMap();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        component = (JComponent )e.getSource();
        int myX = component.getX();
        int myY = component.getY();
        component.setLocation(e.getXOnScreen()-myX, e.getYOnScreen()-myY);
        System.out.println(myX + "/" + e.getXOnScreen() + "///" + myX + "/" + e.getYOnScreen());
        parent.setDrag(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        parent.setDrag(false);
        component=null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);

        if(component!=null)
            component.setLocation(e.getXOnScreen(), e.getYOnScreen());
    }
}
