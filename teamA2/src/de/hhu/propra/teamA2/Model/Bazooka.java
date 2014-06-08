package de.hhu.propra.teamA2.Model;

/**
 * Created by Karsten und Susanna on 25.05.14.
 */
public class Bazooka extends Waffe {
    private String name = "Bazooka";
    private int damage = 20;
    String img_path, img_path_waffe;

    public Bazooka() {
        this.id=1;
        img_path_waffe = "src/res/bazooka.png";
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
