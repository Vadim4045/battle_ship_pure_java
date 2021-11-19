package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;

public class Brick extends JPanel {
    private int brickWidth;
    private final Pair<Integer,Integer> place;
    private Status status;

    public Brick(final int brickWidth, Pair<Integer, Integer> place) {

        this.brickWidth=brickWidth;
        this.place = place;

        this.setStatus(Status.EMPTY);
        drawBrick();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.lightGray);
        g.drawRect(0, 0, brickWidth, brickWidth);
    }

    public void drawBrick() {
        switch (status){
            case EMPTY:
                this.setBackground(Color.WHITE);
                break;
            case FILLED:
                this.setBackground(Color.BLACK);
                break;
            case USELESS:
                this.setBackground(Color.PINK);
                break;
            case USEFULL:
                this.setBackground(Color.GRAY);
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
    }

    public int getBrickWidth() {
        return brickWidth;
    }

    public void setBrickWidth(int brickWidth) {
        this.brickWidth = brickWidth;
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
