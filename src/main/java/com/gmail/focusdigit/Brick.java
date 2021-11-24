package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;

public class Brick extends JPanel {
    private int brickWidth;
    private final Pair<Integer,Integer> place;
    private Status status;
    private int mode;
    private boolean goal=false;

    public Brick(final int brickWidth, Pair<Integer, Integer> place) {

        this.brickWidth=brickWidth;
        this.place = place;
        this.setMode(0);
        this.setStatus(Status.EMPTY);
        drawBrick();
    }

    void setMode(int mode) {
        this.mode=mode;
        drawBrick();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, brickWidth, brickWidth);
        if(goal){
            g.setColor(Color.RED);
            g.drawOval(3, 3, brickWidth-6, brickWidth-6);
        }
    }

    public void setGoal(boolean goal){
        this.goal=goal;
        drawBrick();
    }

    public void drawBrick() {
        if(status==null) return;
        switch (status){
            case EMPTY:
                this.setBackground(Color.WHITE);
                break;
            case FILLED:
                if(mode==3) this.setBackground(Color.BLACK);
                break;
                case BUSSY:
                if(mode>=1) this.setBackground(Color.LIGHT_GRAY);
                break;
            case POTENCIAL:
                if(mode>=2) this.setBackground(Color.DARK_GRAY);
                break;
            case SHOTED:
                this.setBackground(Color.LIGHT_GRAY);
                break;
            case WOUNDED:
                this.setBackground(Color.YELLOW);
                break;
            case KILLED:
                this.setBackground(Color.RED);
                break;
            default:
                break;
        }
        repaint();
    }

    public Pair<Integer, Integer> getPlace() {
        return place;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        drawBrick();
    }
}
