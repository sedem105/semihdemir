package de.hhu.propra.teamA2.Model;

/**
 * Created by Karsten und Susanna on 25.05.14.
 */
public class Pistole extends Waffe {
    private String name = "Pistole";
    private int damage = 10;
    String img_path, img_path_waffe;

    public Pistole() {
        this.id=3;
        img_path_waffe = "src/res/pistole.png";
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
