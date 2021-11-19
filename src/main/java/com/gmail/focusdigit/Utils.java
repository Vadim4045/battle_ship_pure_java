package com.gmail.focusdigit;

import javax.imageio.ImageIO;
import java.awt.*;
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

        //try {
            if (jarFile.isFile()){
                String path = (jarPath + "/" + name).replace("//","/").trim();
                return ImageIO.read(getClass().getResourceAsStream(path));
            }
            else{
                String path = (idePath + "/" + name).replace("//","/").trim();
                return ImageIO.read(new File(path));
            }
        /*}catch(IOException ie){
            JOptionPane.showMessageDialog(null,ie.getMessage(),"IOexception",JOptionPane.WARNING_MESSAGE);
        }
        return null;*/
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
        /*GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return new Pair(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());*/
    }
}
