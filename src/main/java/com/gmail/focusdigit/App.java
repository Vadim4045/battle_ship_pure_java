package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class App extends JFrame implements ActionListener
{
    private  static Utils utils;
    private Properties prop;
    private int rows;
    private int columns;
    private int brickWidth;
    private GameMap myMap;
    private GameMap enemyMap;
    private JButton[] controls;

    public App() {
        super("BattleShip");

        prop = utils.getProperties("config.properties");

        if (prop == null) throw new CustomException("Not found property file");

        rows = Integer.valueOf(prop.getProperty("conf.rows", "10"));
        columns = Integer.valueOf(prop.getProperty("conf.columns", "10"));

        Dimension dimensions = utils.getScreenDimesions();
        if (dimensions == null) throw new CustomException("Screen dimensions not available");

        int width = (int) dimensions.getWidth();
        int height = (int) dimensions.getHeight();

        if(width>=height) brickWidth = height/(2*rows);
        else brickWidth = width/(2*columns);

        myMap = new GameMap(rows, columns, brickWidth, this);
        enemyMap = new GameMap(rows, columns, brickWidth, this);

        this.setSize(dimensions);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        JLabel topLabel = new JLabel("Battle ship", SwingConstants.CENTER);
        topLabel.setFont(new Font("Serif", Font.BOLD,28));
        this.add(topLabel, BorderLayout.NORTH);
        this.add(makeControlPanel(), BorderLayout.SOUTH);

        JPanel centralPanel = new JPanel();

        if(width>=height){
            centralPanel.setLayout(new GridLayout(1,2,20,20));
            centralPanel.add(myMap);
            centralPanel.add(enemyMap);
        }else{
            centralPanel.setLayout(new GridLayout(2,1,20,20));
            centralPanel.add(myMap);
            centralPanel.add(enemyMap);
        }

        this.add(centralPanel,BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);

        prop.setProperty("conf.brickWidth", String.valueOf(brickWidth));
        prop.setProperty("conf.rows", String.valueOf(rows));
        prop.setProperty("conf.columns", String.valueOf(columns));
    }

    private  JPanel makeControlPanel(){
        final String[] forButtons = {"AutoMapping", "ManualMapping", "SaveMap", "StartGame", "Pause"};
        controls = new JButton[forButtons.length];
        JPanel control = new JPanel();
        for(int i=0;i<forButtons.length;i++){
            JButton b = new JButton(forButtons[i]);
            b.addActionListener(this);
            control.add(b);
        }
        return control;
    }

    public void nextStep(Pair p, BasicMap sender) {
        if(sender==enemyMap){

        }
        if(sender==myMap){

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ((JButton)e.getSource()).getText();
        switch (name){
            case "AutoMapping":

                break;
            case "ManualMapping":
                new EditFrame(this,prop);
                break;
            case "SaveMap":

                break;
            case "StartGame":

                break;
            case "":

                break;
            case "Pause":

                break;
            default:
                break;
        }
    }

    public void setMap(Brick[][] map) {
        myMap.setMap(map);
        revalidate();
        repaint();
    }

    public static void main(String[] args)
    {
        final String idePath = "src/main/resources";
        final String jarPath = "";
        utils = new Utils(idePath, jarPath);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new App();
            }
        });
    }
}
