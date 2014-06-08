package de.hhu.propra.teamA2.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * Created by Susanna und Karsten on 01.06.14.
 */

public class WurmLeiche {
    private int x;             // Koordinaten, an denen sich der Wurm befindet
    private int y;
    private String farbe;
    String img_path,img_path_wurmdead;
    private int width, height;

    public WurmLeiche(String farbe){  // Hauptmethode der Wurm-Klasse
        img_path_wurmdead = "src/res/blubs_"+farbe+"_dead.png";
        img_path = img_path_wurmdead;
        width = 50;
        height = 46;
    }

    public String getImagePath(){
        return img_path;
    }

    public int getX() {
        return x;
    }       // fragt die aktuelle Position des Wurms ab (hier: x-Wert, das können wir noch mit dem y-Wert unten zusammenfassen)
    public void setX(int x) {
        this.x = x;
    }   // bzw verändert die aktuelle Position des Wurms (hier: x-Wert, das können wir noch mit dem y-Wert unten zusammenfassen)
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void incY(){ this.y++;}
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    // hier einige weitere getter- und setter-Methoden für die obigen Variablen:
    public String getFarbe() {
        return farbe;
    }
    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

}