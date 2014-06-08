package de.hhu.propra.teamA2.Model;

/**
 * Created by Karsten und Susanna on 25.05.14.
 */
public class Granate extends Waffe {
    private String name = "Granate";
    private int damage = 20;
    String img_path, img_path_waffe;

    public Granate() {

        this.id=2;
        img_path_waffe = "src/res/granate.png";
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
