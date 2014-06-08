package de.hhu.propra.teamA2.Model;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yuana on 05.06.14.
 */
public class ImageLoad {

    Image img;                  // das Bild des Wurms, der auf das Board geladen werden soll
    ImageIcon img_;
    String img_path=null;


    public ImageLoad(){

    }


    public Image getImage(String img_path){
        this.img_path = img_path;
        img_ = new ImageIcon(img_path); // nach Eingabe der Farbe wird der Dateipfad für das Bild konstruiert
        img = img_.getImage();         // und dann das Bild geladen
        return img;
    }


}
