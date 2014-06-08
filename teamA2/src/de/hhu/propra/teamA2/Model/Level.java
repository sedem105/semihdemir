package de.hhu.propra.teamA2.Model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Susanna und Karsten on 01.06.14.
 */
public class Level {

    int levelnummer = 1;              // eingegebene Levelnummer
    int wurm1posx=0;    // initial auf 0 gesetzte Positionen der Würmer
    int wurm1posy=0;    // sollte man noch Arrays draus machen und auch unten die getter und setter anpassen
    int wurm2posx=0;    // und dann auch für eine beliebige anzahl Würmer (oder zumindest innerhalb der maximal wählbaren Spieler- und Würmerzahl
    int wurm2posy=0;
    int wurm3posx=0;
    int wurm3posy=0;
    int wurm4posx=0;
    int wurm4posy=0;
    int wurm5posx=0;
    int wurm5posy=0;
    int wurm6posx=0;
    int wurm6posy=0;
    int wurm7posx=0;
    int wurm7posy=0;
    int wurm8posx=0;
    int wurm8posy=0;
    int wurm9posx=0;
    int wurm9posy=0;
    int wurm10posx=0;
    int wurm10posy=0;
    String img_path_bg = "src/res/lvl_"+levelnummer+"_bg.png";
    String img_path_fg = "src/res/lvl_"+levelnummer+".png";

    public void level(int levelnummer){

        img_path_bg = "src/res/lvl_"+levelnummer+"_bg.png";
        img_path_fg = "src/res/lvl_"+levelnummer+".png";

        if(levelnummer==1) {
            wurm1posx = 300;        // Level-abhängige Startpositionen der Würmer
            wurm1posy = 142;        // sollten wir in eine Text-Datei auslagern

            wurm2posx = 400;        // und wie oben schon gesagt: für mehr als nur 3 Würmer schreiben
            wurm2posy = 142;

            wurm3posx = 500;
            wurm3posy = 142;

            wurm4posx = 540;
            wurm4posy = 248;
            // neu:
            wurm5posx = 250;
            wurm5posy = 142;

            wurm6posx = 199;
            wurm6posy = 244;

            wurm7posx = 75;
            wurm7posy = 244;

            wurm8posx = 608;
            wurm8posy = 197;

            wurm9posx = 650;
            wurm9posy = 248;

            wurm10posx = 450;
            wurm10posy = 142;
        }
        if(levelnummer==2) {
            wurm1posx = 250;
            wurm1posy = 228;

            wurm2posx = 490;
            wurm2posy = 218;

            wurm3posx = 550;
            wurm3posy = 218;

            wurm4posx = 590;
            wurm4posy = 218;
            //neu:
            wurm5posx = 68;
            wurm5posy = 196;

            wurm6posx = 93;
            wurm6posy = 228;

            wurm7posx = 705;
            wurm7posy = 196;

            wurm8posx = 138;
            wurm8posy = 228;

            wurm9posx = 188;
            wurm9posy = 228;

            wurm10posx = 650;
            wurm10posy = 218;
        }
        if(levelnummer==3) {
            wurm1posx = 250;
            wurm1posy = 181;

            wurm2posx = 400;
            wurm2posy = 181;

            wurm3posx = 550;
            wurm3posy = 181;

            wurm4posx = 590;
            wurm4posy = 181;
            //neu:
            wurm5posx = 70;
            wurm5posy = 255;

            wurm6posx = 640;
            wurm6posy = 240;

            wurm7posx = 150;
            wurm7posy = 181;

            wurm8posx = 196;
            wurm8posy = 181;

            wurm9posx = 450;
            wurm9posy = 181;

            wurm10posx = 700;
            wurm10posy = 181;
        }
    }
    //test:
    Rectangle2D.Double block1_1_ = new Rectangle2D.Double(249,187,289,221);     // mittlerer Block, (Input sind: x-Wert, y-Wert, Breite, Höhe)

    public Shape getShapes(){
        return block1_1_;
    }
    // übergibt dem Aufrufenden das Hintergrundbild
    public String getBg(){
        return img_path_bg;
    }
    // übergibt dem Aufrufenden das Vordergrundbild
    public String getFg(){
        return img_path_fg;
    }

    // übergibt dem Aufrufenden die Postionen der (noch nur 3) Würmer
    public int getWurm1posx(){
        return wurm1posx;
    }
    public int getWurm2posx(){
        return wurm2posx;
    }
    public int getWurm3posx(){
        return wurm3posx;
    }
    public int getWurm4posx(){
        return wurm4posx;
    }
    public int getWurm5posx(){
        return wurm5posx;
    }
    public int getWurm6posx(){
        return wurm6posx;
    }
    public int getWurm7posx(){
        return wurm7posx;
    }
    public int getWurm8posx(){
        return wurm8posx;
    }
    public int getWurm9posx(){
        return wurm9posx;
    }
    public int getWurm10posx(){
        return wurm10posx;
    }

    public int getWurm1posy(){
        return wurm1posy;
    }
    public int getWurm2posy(){
        return wurm2posy;
    }
    public int getWurm3posy(){
        return wurm3posy;
    }
    public int getWurm4posy(){
        return wurm4posy;
    }
    public int getWurm5posy(){
        return wurm5posy;
    }
    public int getWurm6posy(){
        return wurm6posy;
    }
    public int getWurm7posy(){
        return wurm7posy;
    }
    public int getWurm8posy(){
        return wurm8posy;
    }
    public int getWurm9posy(){
        return wurm9posy;
    }
    public int getWurm10posy(){
        return wurm10posy;
    }

}
