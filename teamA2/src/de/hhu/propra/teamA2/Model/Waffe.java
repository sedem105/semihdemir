package de.hhu.propra.teamA2.Model;

import java.awt.*;

/**
 * Created by Karsten und Susanna on 25.05.14.
 */
public abstract class Waffe {
    public int damage;
    int x=0;
    int y=0;
    int id;

    public void Waffe(){

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
    public int angreifen(){
        return damage;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }


}
