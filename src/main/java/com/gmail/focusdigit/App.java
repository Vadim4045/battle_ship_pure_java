package com.gmail.focusdigit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;

public class App extends JFrame implements ActionListener
{
    private  static Utils utils;
    private AutomaticMapper mapper;
    private Properties prop;
    private int rows;
    private int columns;
    private int brickWidth;
    private GameMap myMap;
    private ViewMap enemyMap;
    private JButton[] controls;
    private Pair<Integer,Integer> goal;
    private CompGamer compGamer;

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

        myMap = new GameMap(rows, columns, brickWidth,3);
        enemyMap = new ViewMap(rows, columns, brickWidth, this, 0);

        this.setSize(dimensions);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(10,10));

        this.add(makeControlPanel(), BorderLayout.SOUTH);
        this.add(makeTopPanel(), BorderLayout.NORTH);

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
        mapper = new AutomaticMapper(this,prop);
    }

    private  JPanel makeTopPanel(){
        JPanel panel = new JPanel();

        JLabel topLabel = new JLabel("Battle ship", SwingConstants.CENTER);
        topLabel.setFont(new Font("Serif", Font.BOLD,36));
        panel.add(topLabel);

        JButton b = new JButton("Fire");
        b.addActionListener(this);
        controls[0]=b;
        panel.add(b);
        b.setEnabled(false);
        return panel;
    }

    private  JPanel makeControlPanel(){
        final String[] forButtons = {"Automatic mapping", "Manual mapping", "Start game", "Restart game"};
        controls = new JButton[forButtons.length+1];
        JPanel control = new JPanel();
        for(int i=1;i<=forButtons.length;i++){
            JButton b = new JButton(forButtons[i-1]);
            b.addActionListener(this);
            controls[i]=b;
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
            case "Automatic mapping":
                myMap.empty();
                for(int i=0;i<100;i++){
                    ArrayList<int[]> statusMap = mapper.map();
                    if(statusMap!=null){
                        myMap.fillMyMap(statusMap);
                        break;
                    }
                }
                break;
            case "Manual mapping":
                myMap.empty();
                new EditFrame(this,prop);
                break;
            case "Start game":
                enemyMap.empty();
                controls[1].setEnabled(false);
                controls[2].setEnabled(false);
                compGamer=new CompGamer(this);
                break;
            case "Restart game":
                myMap.empty();
                enemyMap.empty();
                compGamer=null;
                controls[1].setEnabled(false);
                controls[2].setEnabled(false);
                break;
            case "Fire":
                Status result =  compGamer.getShot(goal);
                enemyMap.setShot(goal, result);
                break;
            default:
                break;
        }
    }

    public void fillMyMap(ArrayList<int[]> statusMap, GameMap map) {
        if(map==null) map=myMap;
        map.fillMyMap(statusMap);
    }

    public void infoMsgByPlace(JLabel label, String msg, int seconds){
        utils.infoMsgByPlace(label, msg,seconds);
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

    public void autoMap(){
        controls[1].doClick();
    }

    public void setGoal(Pair<Integer, Integer> place) {
        this.goal = place;
        controls[0].setEnabled(true);
    }
    public void enemyShot(Pair<Integer, Integer> place) {

    }

    public AutomaticMapper getMapper() {
        return mapper;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
