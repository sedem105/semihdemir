package de.hhu.propra.teamA2.Model;

/**
 * Created by Karsten und Susanna on 25.05.14.
 */
public class Schwert extends Waffe {
    private String name = "Schwert";
    private int damage = 15;
    String img_path, img_path_waffe;

    public Schwert() {
        this.id=4;
        img_path_waffe = "src/res/schwert.png";
        img_path = img_path_waffe;
    }

    public String getImagePath(){
        return img_path;
    }

    public String getName(){
        return this.name;
    }

    public int angreifen(){
        return damage;
    }

}
