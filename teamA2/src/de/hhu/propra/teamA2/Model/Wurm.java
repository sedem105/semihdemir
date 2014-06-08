package de.hhu.propra.teamA2.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * Created by Susanna und Karsten on 01.06.14.
 */

public class Wurm {
	private int hitpoints = 100;      // Lebenspunkte des Wurms. Wenn sie auf <= 0 sinken, ist der Wurm tot
	private int x,dx;             // Koordinaten, an denen sich der Wurm befindet
	private int y;
    private String farbe;
    boolean alive = true;
    private int width = 50;        //width = img.getWidth(null);
    private int height = 46;        //height = img.getHeight(null);
    String img_path, img_path_left, img_path_right, img_path_dead;

    int waffe_active=1;
    int bazooka=1;
    int pistole=2;
    int granate=3;
    int schwert=4;

    public Wurm(String farbe){  // Hauptmethode der Wurm-Klasse
        this.farbe=farbe;

        img_path_left = "src/res/blubs_"+farbe+"_l.png";
        img_path_right = "src/res/blubs_"+farbe+"_r.png";
        img_path_dead = "src/res/blubs_"+farbe+"_dead.png";
        img_path = img_path_left;

        waffe_active = bazooka; //initiale Waffe ist Bazooka
    }

    public void setWaffe(int waffe){
        if(waffe==1)this.waffe_active = bazooka;
        if(waffe==2)this.waffe_active = pistole;
        if(waffe==3)this.waffe_active = granate;
        if(waffe==4)this.waffe_active = schwert;
    }
    public int getWaffe(){
        if(waffe_active==bazooka)return 1;
        if(waffe_active==pistole)return 2;
        if(waffe_active==granate)return 3;
        if(waffe_active==schwert)return 4;
        return 0;
    }

    public String getImagePath(){
        return img_path;
    }
    public void takeDamage(int damage){
        if (alive) {
            hitpoints = Math.max(0,hitpoints-damage);
            if (hitpoints==0){
                this.alive = false;
                y += 15;
            }
        }
    }
    public void ertrinken(){
        hitpoints = 0;
        this.alive = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void move(){
        if (alive){
            if (dx == 1) {
                if ((x + dx) <= 750)
                    x = x + dx;
            }
            else if (dx == -1){
                if ((x + dx) >= 0)
                    x = x + dx;
            }
        }
    }

    public void keyPressed(KeyEvent e){
        if (alive){
            int key = e.getKeyCode();
            char key_ = e.getKeyChar();              // der Eingabewert des Keys wird in einen charakter umgewandelt

            if (key == KeyEvent.VK_LEFT||key_=='q') {      // gehe und schaue nach links
                dx = -1;
                img_path = img_path_left;
            }
            if (key == KeyEvent.VK_RIGHT||key_=='e') {     // gehe und schaue nach rechts
                dx = 1;
                img_path = img_path_right;
            }
        }
    }
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        char key_ = e.getKeyChar();              // der Eingabewert des Keys wird in einen charakter umgewandelt

        if (key == KeyEvent.VK_LEFT||key_=='q')        // anhalten
            dx = 0;

        if (key == KeyEvent.VK_RIGHT||key_=='e')       // anhalten
            dx = 0;
    }

    public boolean getAlive(){
        return alive;
    }
    public int getHitpoints() {
        return hitpoints;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getXoffset() {
        return x+25;
    }   // damit die waffe von der Mitte des Wurms losschießt
    public int getYoffset() {
        return y+23;
    }   // damit die waffe von der Mitte des Wurms losschießt

    public void incX(){ this.x++;}
    public void decX(){ this.x--;}
    public void incY(){ this.y++;}
    public void decY(){ this.y--;}
    public int getDx(){ return dx;}

    public String getFarbe() {
        return farbe;
    }


}