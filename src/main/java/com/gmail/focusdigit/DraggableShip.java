package com.gmail.focusdigit;

import javax.swing.*;

public class DraggableShip extends JPanel {
    private boolean vertical;
    private int brickWidth;
    private Pair<Integer,Integer> place;
    private Pair<Integer,Integer> dimensios;
    private Brick[] bricks;

    public DraggableShip(int count, int brickWidth) {
        this.place=new Pair<Integer, Integer>(0,0);
        this.brickWidth=brickWidth;
        vertical=true;
        bricks = new Brick[count];

        this.setLayout(null);

        int i=0;
        for(;i<count;i++){
            bricks[i] = new Brick(brickWidth, new Pair<Integer, Integer>(0,i));
            this.add(bricks[i]);
            bricks[i].setStatus(Status.FILLED);
            bricks[i].setMode(3);
        }
        this.setDimensios(new Pair<Integer, Integer>(brickWidth+1,
                (brickWidth+1)*i
        ));
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
        this.vertical=b;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void drawFigure(){
        for(Brick b:bricks){
            b.setBounds(b.getPlace().getFirst()*(brickWidth+1)
                    , b.getPlace().getSecond()*(brickWidth+1)
                    , brickWidth+1
                    , brickWidth+1);
        }
        this.revalidate();
        this.repaint();
    }

    //
    public   void splitBounds(){
        setVertical(!this.vertical);

        int tmp = getDimensios().getFirst();
        getDimensios().setFirst(getDimensios().getSecond());
        getDimensios().setSecond(tmp);

        for(Brick b:bricks){
            tmp=b.getPlace().getFirst();
            b.getPlace().setFirst(b.getPlace().getSecond());
            b.getPlace().setSecond(tmp);
        }

        tmp = isVertical() ? getDimensios().getSecond()/2 :getDimensios().getFirst()/2;
        int first = isVertical() ? tmp:-tmp;
        int second = isVertical() ? -tmp:+tmp;
        this.setPlace(getPlace().getFirst()+first,getPlace().getSecond()+second);
    }

    public Brick[] getMap() {
        return bricks;
    }

    public void setPlace(int left, int top) {
        getPlace().setFirst((left/(brickWidth+1))*(brickWidth+1));
        getPlace().setSecond((top/(brickWidth+1))*(brickWidth+1));
    }
}
