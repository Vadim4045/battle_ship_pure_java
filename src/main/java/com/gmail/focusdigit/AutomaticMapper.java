package com.gmail.focusdigit;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

public class AutomaticMapper {
    private final App parent;
    Random ran;
    private Status[][] map;
    private int rows;
    private int columns;
    private ArrayList<Pair<Integer, Integer>> ships;
    private Vector<Integer> cells;

    public AutomaticMapper(App parent, Properties prop){
        this.parent=parent;
        ran = new Random();
        ships = new ArrayList<>();

        String count;
        for(int i=10;i>0;i--)
            if((count = prop.getProperty("ship." + i))!=null)
                ships.add(new Pair<Integer, Integer>(i, Integer.valueOf(count)));

        rows = Integer.valueOf(prop.getProperty("conf.rows"));
        columns = Integer.valueOf(prop.getProperty("conf.columns"));

    }

    public  boolean map(){
        for(int offer = 0;offer<100;offer++){
            map=new Status[rows][columns];

            for(int i=0;i<rows;i++)
                for(int j=0;j<columns;j++)
                    map[j][i] = Status.EMPTY;

            int mapped=0;
            cells = new Vector<>();
            for(int i=0;i<rows*columns;i++) cells.add(i);

            for(Pair<Integer, Integer> p:ships){
                int step=0;
                for(int i = 0;i < p.getSecond(); i++){
                    for(int setF = 0;setF<100;setF++) {
                        if (setFigure(p.getFirst())){
                            step++;
                            break;
                        }
                    }
                }
                if(step==p.getSecond())mapped++;

            }

            if(mapped==ships.size()){
                for(int i=0;i<rows;i++)
                    for(int j=0;j<columns;j++)
                        if(map[i][j]==Status.FILLED) parent.fillBrickInMyMap(i,j);
                return true;
            }
        }
        return false;
    }

    private boolean setFigure(int bricks){
        int[] numbers = new int[bricks];

        numbers[0] = cells.get(ran.nextInt(cells.size()));

        if(ran.nextInt(100)>50)
            if(setVertical(bricks,  numbers))return true;
        else
            if(setGorizontal(bricks, numbers))return true;
        return false;
    }

    private boolean setVertical(int bricks, int[]numbers){
        int i=0;
        int count = 0;
        for(;i<bricks;i++){
            int cur = numbers[0]+columns*i;
            if(numbers[0]/columns!=cur%columns) break;
            if(checkCell(cur)){
                numbers[i]=cur;
                count++;
            }else break;
        }
        for(int j=1;j<bricks-i;j++){
            int cur = numbers[0]-columns*j;
            if(numbers[0]/columns!=cur%columns) break;
            if(checkCell(cur)){
                numbers[i+j]=cur;
                count++;
            }else break;
        }
        if(count==bricks){
            for(int cur:numbers)
                setCellsBussy(cur);

            for(int cur:numbers)
                setCellsFilled(cur);

            return true;
        }else return false;
    }

    private boolean setGorizontal(int bricks, int[] numbers){
        int i=0;
        int count = 0;
        for(;i<bricks;i++){
            int cur = numbers[0]+i;
            if(numbers[0]/columns!=cur/columns) break;
            if(checkCell(cur)){
                numbers[i]=cur;
                count++;
            }else break;
        }
        for(int j=1;j<bricks-i;j++){
            int cur = numbers[0]-j;
            if(numbers[0]/columns!=cur/columns) break;
            if(checkCell(cur)){
                numbers[i+j]=cur;
                count++;
            }else break;
        }
        if(count==bricks){
            for(int cur:numbers)
                setCellsBussy(cur);

            for(int cur:numbers)
                setCellsFilled(cur);

            return true;
        }else return false;
    }

    private  boolean checkCell(int cell){
        if(cell<0 || cell>=rows*columns) return false;

        int row = cell/columns;
        int column = cell%columns;

        int a=row==0? 0:-1;
        int b=row==rows-1?1:2;
        int c=column==0?0:-1;
        int d=column==columns-1?1:2;

        for(int i=a;i<b;i++){
            for(int j=c;j<d;j++){
                int cur = cell+columns*i+j;
                if(!cells.contains(cur)) return false;
            }
        }
        return true;
    }

    private  void setCellsBussy(int cell){
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
                int cur = cell+columns*i+j;
                if(cur<0 || cur>=rows*columns) continue;

                int row = cur/columns;
                int col = cur%columns;
                map[row][col]=Status.BUSSY;
                cells.removeElement(cur);
            }
        }
    }

    private  void setCellsFilled(int cur){
        int row = cur/columns;
        int col = cur%columns;
        map[row][col]=Status.FILLED;
    }
}
