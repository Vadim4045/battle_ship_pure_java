package com.gmail.focusdigit;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Properties;

public class EditFrame extends JFrame implements ActionListener {
    private EditMap editMap;
    private ArrayList<Pair<Integer, Integer>> ships;
    private MiniMap[] figures;
    private  int brickWidth;
    volatile boolean drag;
    JPanel dragPanel;

    public EditFrame(App parent, Properties prop){
        super("Edit yor board");
        ships = new ArrayList<Pair<Integer, Integer>>();

        int rows = Integer.valueOf(prop.getProperty("conf.rows"));
        int columns = Integer.valueOf(prop.getProperty("conf.columns"));
        brickWidth = Integer.valueOf(prop.getProperty("conf.brickWidth"));

        editMap = new EditMap(rows, columns,brickWidth,this);

        String count;
        for(int i=10;i>0;i--){
            if((count = prop.getProperty("ship." + i))!=null)
                ships.add(new Pair<Integer, Integer>(i,Integer.valueOf(count)));
        }
        figures = new MiniMap[ships.size()];

        setLayout(new BorderLayout(20, 20));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(editMap, BorderLayout.CENTER);
        add(makeShipsHolder(), BorderLayout.EAST);
        this.setAlwaysOnTop(true);
        this.pack();
        this.setVisible(true);
    }

    private JPanel makeShipsHolder(){
        if(ships.size()==0) return null;

        JPanel panel = new JPanel();

        for(int i=0;i<ships.size();i++){
            JPanel curPanel = new JPanel();
            curPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

            figures[i] = new MiniMap(ships.get(i).getFirst()
                                    , ships.get(i).getFirst()
                                    , brickWidth
                                    , this);

            curPanel.add(figures[i]);
            JButton btn = new JButton("Rotate");
            btn.setActionCommand("" + i);
            btn.addActionListener(this);
            curPanel.add(btn);
            panel.add(curPanel);
        }

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i = Integer.valueOf(e.getActionCommand());
        figures[i].setVertical();
    }

    public void setDrag(boolean drag) {
        this.drag = drag;
    }
}
