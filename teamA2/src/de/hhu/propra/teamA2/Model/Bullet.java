package de.hhu.propra.teamA2.Model;

import javax.swing.*;
import java.awt.*;
/**
 * Created by Karsten und Susanna on 01.06.14.
 */
public class Bullet {
    int x=0;
    int y=0;

    Image img;
    ImageIcon img_bullet;
    boolean visible;
    ImageIcon img_waffe;
    String img_path, img_path_bullet;


    public Bullet(int startX, int startY){
        x = startX;
        y = startY;
        img_bullet = new ImageIcon("src/res/feuergeschoss.png");
        img = img_bullet.getImage();
        visible = true;
        img_path_bullet = "src/res/feuergeschoss.png";
        img_path = img_path_bullet;
    }

    public String getImagePath(){
        return img_path;
    }
    public void move(){
        x = x + 2;
        if (x>800) visible = false;
    }
    public Image getImage(){
        return img;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setX(double x){
        this.x = (int) x;
    }
    public void setY(double y){
        this.y = (int) y;
    }
    public void wurmActivePos(int x,int y){

    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }
    public boolean getVisible(){
        return visible;
    }
}
