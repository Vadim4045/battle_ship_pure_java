package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class EditFrame extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
    private final App parent;
    private int rows;
    private int columns;
    private BasicMap editMap;
    private Pair<Integer,Integer> mousePressXY = new Pair<Integer,Integer>(0,0);
    private ArrayList<Pair<Integer, Integer>> ships;
    private ArrayList<DraggableShip> figures;
    private ArrayList<Pair<Integer, Integer>> originalPlases;
    private  int brickWidth;
    private static ThreadPoolExecutor fixedThreadPoolWithQueueSize;
    DraggableShip dragPanel;

    public EditFrame(App parent, Properties prop){
        super("Edit yor board");
        this.parent=parent;
        ships = new ArrayList<>();
        originalPlases=new ArrayList<>();
        rows = Integer.valueOf(prop.getProperty("conf.rows"));
        columns = Integer.valueOf(prop.getProperty("conf.columns"));
        brickWidth = Integer.valueOf(prop.getProperty("conf.brickWidth"));

        String count;
        for(int i=10;i>0;i--){
            if((count = prop.getProperty("ship." + i))!=null) {
                ships.add(new Pair<Integer, Integer>(i, Integer.valueOf(count)));
            }
        }

        editMap = new BasicMap(rows, columns,brickWidth, 3);
        JPanel controlPanel = makeControlPanel();
        figures = makeShipsHolder();
        JPanel centralPanel = parseShipsOnBoard(new JPanel());

        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Manual board edit", JLabel.CENTER);
        lbl.setFont(new Font("Serif", Font.BOLD,28));
        this.add(lbl, BorderLayout.NORTH);
        this.add(centralPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);



        this.setSize(centralPanel.getPreferredSize().width,
                lbl.getPreferredSize().height
                        +centralPanel.getPreferredSize().height
                        + controlPanel.getPreferredSize().height);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.validate();
        this.setVisible(true);

        fixedThreadPoolWithQueueSize = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        fixedThreadPoolWithQueueSize.setCorePoolSize(1);
    }

    private JPanel parseShipsOnBoard(JPanel panel) {
        if(figures.size() == 0) return null;

        int max=0, i=0, left = (brickWidth+1)*(columns+1), top=(brickWidth+1);

        panel.setLayout(null);
        for(int j=2;j>0;j--){
            left = (brickWidth+1)*(columns+1);
            for(;i<figures.size()/j;i++){
                max = figures.get(i).getDimensios().getSecond()>max?
                        figures.get(i).getDimensios().getSecond():max;

                figures.get(i).setPlace(left,top);
                panel.add(figures.get(i));
                drawFigure(figures.get(i));
                left+=2*(brickWidth+1);
            }
            top+=max+brickWidth+1;
            max=0;
        }
        panel.add(editMap);
        editMap.setBounds(0,0,(brickWidth+1)*columns,(brickWidth+1)*rows);
        top = (brickWidth+1)*rows>top?(brickWidth+1)*rows:top;
        panel.setPreferredSize(new Dimension(left, top+brickWidth));

        return panel;
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

    private ArrayList<DraggableShip> makeShipsHolder(){
        if(ships.size()==0) return null;
        ArrayList<DraggableShip> list = new ArrayList<>();

        DraggableShip currentShip;

        for(int i=0;i<ships.size();i++){
            for(int j=0;j<ships.get(i).getSecond();j++){
                currentShip = new DraggableShip(ships.get(i).getFirst(), brickWidth);
                currentShip.addMouseMotionListener(this);
                currentShip.addMouseListener(this);
                list.add(currentShip);
            }
        }
        return list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String com = e.getActionCommand();
        switch (com){
            case "Turn":
                if(dragPanel!=null)splitFigureBounds(dragPanel);
                break;
            case "Abort":
                if(dragPanel!=null){
                    if(!dragPanel.isVertical()) dragPanel.splitBounds();
                    dragPanel.setPlace(originalPlases.get(figures.indexOf(dragPanel)));
                    drawFigure(dragPanel);
                }
                break;
            case "Auto":
                    parent.autoMap();
                    this.dispose();
                break;
            case "Save":
                if(check()) {
                    saveMap();
                    this.dispose();
                }

                break;
            case "Return":
                this.dispose();
                break;
        }
    }

    private void saveMap() {
        ArrayList<int[]> statusMap = new ArrayList<>();
        for(DraggableShip f:figures){
            int currentFigureX = f.getPlace().getFirst()/(brickWidth+1);
            int currentFigureY = f.getPlace().getSecond()/(brickWidth+1);
            int[] curArray = new int[f.getMap().length];
            for(int i=0;i<curArray.length;i++){
                curArray[i]=(f.getMap()[i].getPlace().getFirst()+currentFigureX)
                        + (f.getMap()[i].getPlace().getSecond()+currentFigureY)*columns;
            }
            statusMap.add(curArray);
        }
        parent.fillMyMap(statusMap, null);
    }

    private boolean check() {
        for(int i=0;i<figures.size()-1;i++){
            int checkedFigureX = figures.get(i).getPlace().getFirst()/(brickWidth+1);
            int checkedFigureY = figures.get(i).getPlace().getSecond()/(brickWidth+1);
            for(int j=i+1;j<figures.size();j++){
                int currentFigureX = figures.get(j).getPlace().getFirst()/(brickWidth+1);
                int currentFigureY = figures.get(j).getPlace().getSecond()/(brickWidth+1);
                for(Brick curBrick: figures.get(i).getMap()){
                    int checkedBrickX = checkedFigureX + curBrick.getPlace().getFirst();
                    int checkedBrickY = checkedFigureY + curBrick.getPlace().getSecond();
                    for(Brick b: figures.get(j).getMap()){
                        int currentBrickX = currentFigureX + b.getPlace().getFirst();
                        int currentBrickY = currentFigureY + b.getPlace().getSecond();
                        if(checkedBrickX<0 || checkedBrickX>=columns
                                || checkedBrickY<0 || checkedBrickY>= rows
                                || currentBrickX<0 || currentBrickX>=columns
                                || currentBrickY<0 || currentBrickY>= rows
                                || (Math.abs(checkedBrickX-currentBrickX)<2)
                                && Math.abs(checkedBrickY-currentBrickY)<2){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void splitFigureBounds(DraggableShip f){
        f.splitBounds();
        drawFigure(f);
    }

    public void drawFigure(DraggableShip f){
        if(f.getDimensios()!=null && f.getPlace()!=null) {
            f.setBounds(f.getPlace().getFirst()
                    , f.getPlace().getSecond()
                    , f.getDimensios().getFirst()
                    , f.getDimensios().getSecond());
            f.drawFigure();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        fixedThreadPoolWithQueueSize.execute(new MooveTask("Drag", e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        fixedThreadPoolWithQueueSize.execute(new MooveTask("Press", e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        fixedThreadPoolWithQueueSize.execute(new MooveTask("Release", e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private class MooveTask implements Runnable{
        private String comand;
        MouseEvent e;

        public MooveTask(String comand, MouseEvent e) {
            this.comand=comand;
            this.e=e;
        }

        @Override
        public void run() {
            switch (comand){
                case "Press":
                    dragPanel = (DraggableShip) e.getSource();
                    mousePressXY.setFirst(e.getXOnScreen()-dragPanel.getLocation().x);
                    mousePressXY.setSecond(e.getYOnScreen()-dragPanel.getLocation().y);
                    break;
                case "Drag":
                    int left = e.getXOnScreen()-mousePressXY.getFirst();
                    int top = e.getYOnScreen()-mousePressXY.getSecond();
                    dragPanel.setPlace(left, top);
                    drawFigure(dragPanel);
                    break;
                case "Release":
                    if(dragPanel!=null){
                        drawFigure(dragPanel);
                    }

                    break;
                default:
                    break;
        }
    }
    }
}
