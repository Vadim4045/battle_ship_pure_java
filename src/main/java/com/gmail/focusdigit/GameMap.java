package com.gmail.focusdigit;

import java.util.ArrayList;

public class GameMap extends BasicMap {
    ArrayList<int[]> statusMap;

    public GameMap(int rows, int columns, int brickWidth, int hide){
        super(rows,columns,brickWidth, hide);
    }

    public void fillMyMap(ArrayList<int[]> statusMap){
        this.statusMap=statusMap;
        for(int[] curShip:statusMap)
            for(int num:curShip)
                getMap()[num/map[0].length][num%map[0].length].setStatus(Status.FILLED);
    }

    public ArrayList<int[]> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(ArrayList<int[]> statusMap) {
        this.statusMap = statusMap;
    }

    public Status getShot(Pair<Integer, Integer> goal) {
        Brick b = getBrick(goal);
        if(b.getStatus()==Status.EMPTY) b.setStatus(Status.SHOTED);
        if(b.getStatus()==Status.FILLED) b.setStatus(Status.KILLED);
        return b.getStatus();
    }
}
