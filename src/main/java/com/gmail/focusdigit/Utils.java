package com.gmail.focusdigit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    final String jarPath;
    final String idePath;

    //
    public Utils(String idePath, String jarPath){
        this.idePath = idePath;
        this.jarPath = jarPath;
    }

    //
    public BufferedImage getImageFromResources(String name) throws IOException {
        final File jarFile = new File(Utils
                .class.getProtectionDomain()
                .getCodeSource().getLocation()
                .getPath());

        if (jarFile.isFile()){
            String path = (jarPath + "/" + name).replace("//","/").trim();
            return ImageIO.read(getClass().getResourceAsStream(path));
        }
        else{
            String path = (idePath + "/" + name).replace("//","/").trim();
            return ImageIO.read(new File(path));
        }
    }

    //
    public Properties getProperties(String propertiFileName){
        try (InputStream input = App.class.getClassLoader().getResourceAsStream(propertiFileName)) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find" + propertiFileName);
                return null;
            }
            prop.load(input);
            return prop;
        } catch (IOException ex) { ex.printStackTrace(); }
        return null;
    }

    //
    public Dimension getScreenDimesions(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Gets target(JLabel) to show message,
     * String whith given message and period of time
     * Invoke InfoMesegWithTimer class for start in enather thread
     *
     * @param label
     * @param msg
     * @param seconds
     */
    void infoMsgByPlace(final JLabel label, final String msg, final int seconds) {
        Thread thread = new Thread(){
            public void run(){
                new InfoMesegWithTimer(label, msg, seconds);
            }
        };
        thread.start();
    }

    /**
     * Class for show temporary message on given target
     * in different thread
     */
    class InfoMesegWithTimer {
        Timer timer;
        String msg, oldMsg;
        JLabel label;

        /**
         * Show given message on given target by given period time
         *
         * @param label
         * @param msg
         * @param seconds
         */
        InfoMesegWithTimer(JLabel label, String msg, int seconds) {
            this.msg=msg;
            this.label=label;
            timer = new Timer();
            oldMsg = label.getText();
            label.setText(msg);
            label.revalidate();
            label.repaint();
            timer.schedule(new RemindTask(), seconds*1000);
        }

        /**
         * Scheduler for return old message on target after given period time
         */
        class RemindTask extends TimerTask {
            public void run() {
                label.setText(oldMsg);
                label.revalidate();
                label.repaint();
                timer.cancel();
            }
        }
    }
}
