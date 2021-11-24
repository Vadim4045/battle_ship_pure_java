package com.gmail.focusdigit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ViewMap extends BasicMap implements MouseListener {
    private App parent;
    private Brick goal;
    public ViewMap(int rows, int columns, int brickWidth, App parent, int mode) {
        super(rows, columns, brickWidth, mode);
        this.parent=parent;

        for(Brick[] bricks:getMap())
            for(Brick brick:bricks)
                brick.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Brick b = (Brick) e.getSource();
        if(goal!=null) goal.setGoal(false);
        goal=b;
        b.setGoal(true);
        parent.setGoal(goal.getPlace());
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void setShot(Pair<Integer, Integer> goal, Status result) {
        Brick b = getBrick(goal);
        b.setStatus(result);
    }
}
