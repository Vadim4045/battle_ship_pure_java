package com.gmail.focusdigit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditMap extends BasicMap implements MouseListener {
    EditFrame parent;
    private Pair<Integer, Integer> shipHolder;

    public EditMap(int rows, int columns, int brickWidth, EditFrame parent) {
        super(rows, columns, brickWidth);
        this.parent=parent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Pair p = ((Brick)e.getSource()).getPlace();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
