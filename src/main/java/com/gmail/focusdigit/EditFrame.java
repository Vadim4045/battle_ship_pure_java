package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Properties;

public class EditFrame extends JFrame implements ActionListener, MouseMotionListener {
    private final App parent;
    private int rows;
    private int columns;
    private EditMap editMap;
    private ArrayList<Pair<Integer, Integer>> ships;
    private ArrayList<DraggableShip> figures;
    private ArrayList<Pair<Integer, Integer>> originalPlases;
    private  int brickWidth;
    volatile boolean drag;
    DraggableShip dragPanel;

    public EditFrame(App parent, Properties prop){
        super("Edit yor board");
        this.parent=parent;
        ships = new ArrayList<>();
        originalPlases=new ArrayList<>();
        rows = Integer.valueOf(prop.getProperty("conf.rows"));
        columns = Integer.valueOf(prop.getProperty("conf.columns"));
        brickWidth = Integer.valueOf(prop.getProperty("conf.brickWidth"));

        editMap = new EditMap(rows, columns,brickWidth,this);

        int curHeight=0;
        String count;
        for(int i=10;i>0;i--){
            if((count = prop.getProperty("ship." + i))!=null) {
                ships.add(new Pair<Integer, Integer>(i, Integer.valueOf(count)));
                curHeight += brickWidth*(i+1);
            }
        }
        figures = new ArrayList<>();

        this.setResizable(false);
        this.setLayout(new BorderLayout(10,10));

        this.add(makeShipsHolder(), BorderLayout.CENTER);
        this.add(makeControlPanel(), BorderLayout.SOUTH);


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.pack();
        this.setVisible(true);
    }

    private  JPanel makeControlPanel(){
        final String[] forButton = {"Turn", "Abort", "Save", "Auto", "Return"};
        JPanel panel = new JPanel();

        for(int i=0;i<forButton.length;i++){
            JButton btn = new JButton(forButton[i]);
            btn.setActionCommand(forButton[i]);
            btn.setSize(60, 30);
            btn.addActionListener(this);
            panel.add(btn);
        }

        return panel;
    }

    private JPanel makeShipsHolder(){
        if(ships.size()==0) return null;

        JPanel panel = new JPanel();
        panel.setLayout(null);

        DraggableShip currentShip;
        int currentTop = 0;
        int left = (brickWidth+3)*columns;

        for(int i=0;i<ships.size();i++){
            left = (brickWidth+3)*columns;
            for(int j=0;j<ships.get(i).getSecond();j++){
                currentShip = new DraggableShip(new Pair<Integer, Integer>(left,currentTop)
                        , ships.get(i).getFirst()
                        , brickWidth
                );
                originalPlases.add(currentShip.getPlace());

                left+= 2*brickWidth;

                figures.add(currentShip);

                currentShip.setDimensios(new Pair<Integer, Integer>((brickWidth+1)*ships.get(i).getFirst()
                        ,brickWidth+1
                ));

                panel.add(currentShip);

                currentShip.setBounds(0
                        , 0
                        , currentShip.getDimensios().getFirst()
                        , currentShip.getDimensios().getSecond()
                );

                currentShip.addMouseMotionListener(this);
                currentShip.splitBounds();
            }
            currentTop+=(brickWidth+1)*(ships.get(i).getFirst()+1);
        }

        panel.add(editMap);
        editMap.setBounds(0,0,(brickWidth+1)*rows, (brickWidth+1)*columns);

        panel.setPreferredSize(new Dimension(left+(brickWidth)
                , currentTop>(brickWidth+1)*rows? currentTop:(brickWidth+1)*rows));
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        switch (com){
            case "Turn":
                if(dragPanel!=null)dragPanel.splitBounds();
                break;
            case "Abort":
                if(dragPanel!=null){
                    if(!dragPanel.isVertical()) dragPanel.splitBounds();
                    dragPanel.setPlace(originalPlases.get(figures.indexOf(dragPanel)));
                    dragPanel.refreshMap();
                }
                break;
            case "Auto":

                break;
            case "Save":
                if(check()) {
                    parent.setMap(saveMap());
                    this.dispose();
                }

                break;
            case "Return":
                this.dispose();
                break;
        }
    }

    private Brick[][] saveMap() {
        Brick[][] map = editMap.getMap();
        for(Brick[] brick:map){
            for(Brick mapBrick:brick){
                int mapBrickX = mapBrick.getPlace().getFirst();
                int mapBrickY = mapBrick.getPlace().getSecond();
                for(DraggableShip d:figures){
                    for(Brick shipBrick:d.getMap()){
                        int shipBrickX = shipBrick.getPlace().getFirst();
                        int shipBrickY = shipBrick.getPlace().getSecond();
                        if(Math.abs(mapBrickX-shipBrickX)<brickWidth
                            && Math.abs(mapBrickY-shipBrickY)<brickWidth)
                                mapBrick.setStatus(Status.FILLED);
                    }
                }
            }
        }
        return map;
    }

    private boolean check() {
        int mapWidth = (brickWidth+1)*columns;
        int mapHeight = (brickWidth+1)*rows;
        for(int i=0;i<figures.size()-1;i++){
            int checkedFigureX = figures.get(i).getPlace().getFirst();
            int checkedFigureY = figures.get(i).getPlace().getSecond();
            for(int j=i+1;j<figures.size();j++){
                int currentFigureX = figures.get(j).getPlace().getFirst();
                int currentFigureY = figures.get(j).getPlace().getSecond();
                for(Brick curBrick: figures.get(i).getMap()){
                    int checkedBrickX = checkedFigureX + curBrick.getPlace().getFirst();
                    int checkedBrickY = checkedFigureY + curBrick.getPlace().getSecond();
                    for(Brick b: figures.get(j).getMap()){
                        int currentBrickX = currentFigureX + b.getPlace().getFirst();
                        int currentBrickY = currentFigureY + b.getPlace().getSecond();
                        if(checkedBrickX<0 || checkedBrickX>=mapWidth
                                || checkedBrickY<0 || checkedBrickY>= mapHeight
                                || currentBrickX<0 || currentBrickX>=mapWidth
                                || currentBrickY<0 || currentBrickY>= mapHeight
                                || (Math.abs(checkedBrickX-currentBrickX)<2*(brickWidth+1))
                                && Math.abs(checkedBrickY-currentBrickY)<2*(brickWidth+1)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void setDrag(boolean drag, DraggableShip cur) {
        this.drag = drag;
        if(drag && cur!=null) dragPanel=cur;
    }

    public boolean isDrag(){
        return drag;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        setDrag(true, (DraggableShip) e.getSource());

        dragPanel.setBounds(((e.getXOnScreen()-brickWidth)/(brickWidth+1))*(brickWidth+1)
                , ((e.getYOnScreen()-brickWidth)/(brickWidth+1))*(brickWidth+1)
                ,dragPanel.getDimensios().getFirst()
                ,dragPanel.getDimensios().getSecond()
        );

        dragPanel.setPlace(
                new Pair<Integer, Integer>(
                        ((e.getXOnScreen()-brickWidth)/(brickWidth+1))*(brickWidth+1)
                            , ((e.getYOnScreen()-brickWidth)/(brickWidth+1))*(brickWidth+1)
                ));
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
