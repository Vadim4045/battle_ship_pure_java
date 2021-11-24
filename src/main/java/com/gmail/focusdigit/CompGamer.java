package com.gmail.focusdigit;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class CompGamer {
    private App parent;
    private GameMap gameMap;
    private int rows;
    private int columns;
    private ArrayList<ArrayList<Pair<Integer,Status>>> myShips;
    private Vector<Integer> enemyCells;
    private Random ran;
    private boolean ready=false;

    public CompGamer(App parent) {
        this.parent = parent;
        rows=parent.getRows();
        columns=parent.getColumns();
        this.gameMap = new GameMap(rows, columns, 10,3);
        gameMap.empty();
        for(int i=0;i<100;i++){
            ArrayList<int[]> statusMap = parent.getMapper().map();
            if(statusMap!=null){
                gameMap.fillMyMap(statusMap);
                setReady(true);
                break;
            }
        }

        rows = gameMap.getMap().length;
        columns = gameMap.getMap()[0].length;
        enemyCells = makeGameVector();

    }

    public Status getShot(Pair<Integer,Integer> goal){
        return gameMap.getShot(goal);
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private Vector<Integer> makeGameVector() {
        Vector<Integer> vector = new Vector<>();
        for(int i=0;i<rows*columns;i++)
            vector.add(i);
        return vector;
    }

}
