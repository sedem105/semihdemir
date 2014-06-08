package com.thai.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import de.hhu.propra.teamA2.Model.*;
import de.hhu.propra.teamA2.View.Board.AL;
import de.hhu.propra.teamA2.View.Board.ML;
import de.hhu.propra.teamA2.View.LastScreen;
import de.hhu.propra.teamA2.View.SpielMain;

public class Boardnetzwerk extends  JPanel implements ActionListener,Clientdelegate{      //Erstellen der Spielfläche, erstmal ein paar Variablen festlegen:


    int round = 1;      // Rundennummer, startet bei Runde 1
    int levelnummer = 1;  // Levelnummer, startet bei Level 1
    int wurmnr1=0;
    int wurmnr2=0;  //weitere, sobald mehr wurmstartpos vorhanden

    SpielMain spiel;
    Level level = new Level();  // erstelle ein Objekt der Klasse Level

    Image img1, img2;   // das werden Hintergrund- (img1) und Vordergrundbild (img2), kommt unten noch vor
    int teams=1;
    boolean show_bg=true;

    int teamsize = 2;
    Wurm wurm_active;
    LinkedList<Wurm> team1;
    LinkedList<Wurm> team2;
    LinkedList<Wurm> team3;
    LinkedList<Wurm> team4;
    LinkedList<Wurm> team5;

    ImageLoad img = new ImageLoad();

    ArrayList<WurmLeiche> dead_wurm = new ArrayList<WurmLeiche>();

    boolean gameover = false;
    boolean exitwindow=false;
    int winner=0;

    Timer time;
    Bullet bullet;

    Point mausPos = new Point();              // Position der Maus beim Klicken zum Zielen mit Waffe
    Point zielPos = new Point();              // Endpunkt der Geraden beim Zielen
    Point startPos = new Point();             // Startpunkt der Gerade beim Zielen, wichtig bei Abprallern!

    Rectangle zielpunkt = new Rectangle();

    Waffe waffe_active;
    Bazooka bazooka = new Bazooka();
    Granate granate = new Granate();
    Pistole pistole = new Pistole();
    Schwert schwert = new Schwert();

    //Terrain-Objekte (noch in Datei auslagern)
    //Level1
    int[] xpoints_0 = {71,249,249,71};
    int[] ypoints_0 = {320,290,414,414};
    Polygon block1_0_ = new Polygon(xpoints_0,ypoints_0,4);
    Rectangle2D.Double block1_1_ = new Rectangle2D.Double(249,187,289,221);     // mittlerer Block, (Input sind: x-Wert, y-Wert, Breite, Höhe)
    Rectangle2D.Double block1_2_ = new Rectangle2D.Double(538,294,186,120);     // rechts, Mauerwürfel fehlt noch
    //Level2
    Rectangle2D.Double block2_1_ = new Rectangle2D.Double(82,274,213,72);   //seife links
    Rectangle2D.Double block2_2_ = new Rectangle2D.Double(463,264,248,113); //seife rechts
    Rectangle2D.Double block2_3_ = new Rectangle2D.Double(68,242,23,48);    //wannenrand links
    Rectangle2D.Double block2_4_ = new Rectangle2D.Double(705,242,33,48);   //wannenrand rechts
    //Level3
    int[] xpoints_a = {60,196,196};         // die x-Werte für die Polygone werden in einem,
    int[] ypoints_a = {367,227,367};        // und die y-Werte in einem anderen Array gespeichert
    int[] xpoints_b = {594,594,732};        // nochmal für das zweite Polygon
    int[] ypoints_b = {367,227,367};
    Rectangle2D.Double block3_1_ = new Rectangle2D.Double(196,227,398,30);  // Brücke Mittelteil
    Polygon block3_2_ = new Polygon(xpoints_a,ypoints_a,3);      // Schräge links als Dreieck (Input sind: Array mit x-werten, Array mit y-Werten, Anzahl der Eckpunkte)
    Polygon block3_3_ = new Polygon(xpoints_b,ypoints_b,3);      // Schräge rechts als Dreieck

    Rectangle2D.Double water = new Rectangle2D.Double(0,366,800,84);
    Rectangle2D.Double water_2 = new Rectangle2D.Double(0,330,800,120);

    public Boardnetzwerk(){
        Spielstand stand = SaveGame.loadGame(); //load spielstand

        teams = stand.getTeam().size();
        team1 = new LinkedList<Wurm>();
        team2 = new LinkedList<Wurm>();
        team3 = new LinkedList<Wurm>();
        team4 = new LinkedList<Wurm>();
        team5 = new LinkedList<Wurm>();

        for (int j = 1; j <= teamsize; j++)
            team1.add(new Wurm(stand.getTeam().get(0).getFarbe()));
        for (int j = 1; j <= teamsize; j++)
            team2.add(new Wurm(stand.getTeam().get(1).getFarbe()));
        if (teams==3) {
            for (int j = 1; j <= teamsize; j++)
                team3.add(new Wurm(stand.getTeam().get(2).getFarbe()));
            if (teams==4) {
                for (int j = 1; j <= teamsize; j++)
                    team4.add(new Wurm(stand.getTeam().get(3).getFarbe()));
                if (teams==5) {
                    for (int j = 1; j <= teamsize; j++)
                        team5.add(new Wurm(stand.getTeam().get(4).getFarbe()));
                }}}

        addKeyListener(new AL());   // hier wird ein neuer KeyListener erstellt und dem Board hinzugefügt
        addMouseListener(new ML()); // Mauslistener für Zielen mit Waffe
        setFocusable(true);         // damit das Board aufnahmebereit ist zB für die KeyEvent, wird es hier auf focusable gestellt

        levelnummer = stand.getAktuelle_Level();
        level.level(levelnummer);   // die aktuelle levelnummer (initial 1) wird hier an die Klasse Level übergeben

        wurm_active=team1.get(1);

        team1.get(0).setX(level.getWurm1posx());       // Startpositionen der Würmer werden von der Klasse Level abgefragt.
        team1.get(0).setY(level.getWurm1posy());       // Je nach am anfang übergebenem Level sind die Positionen unterschiedlich
        team1.get(1).setX(level.getWurm2posx());       // das soll noch verbessert werden, in einem Array oder so
        team1.get(1).setY(level.getWurm2posy());
        team2.get(0).setX(level.getWurm3posx());
        team2.get(0).setY(level.getWurm3posy());
        team2.get(1).setX(level.getWurm4posx());
        team2.get(1).setY(level.getWurm4posy());

        bullet = new Bullet(wurm_active.getX(),wurm_active.getY());

        mausPos.setLocation(wurm_active.getXoffset(),wurm_active.getYoffset());     //Zielstrahl am Anfang zum Punkt machen, zielt nirgendwohin  bzw. auf sich selbst
        zielPos.setLocation(wurm_active.getXoffset(),wurm_active.getYoffset());
        startPos.setLocation(wurm_active.getXoffset(),wurm_active.getYoffset());

        time = new Timer(5,this);
        time.start();
    }

    public void setSpiel(SpielMain spiel){
        this.spiel = spiel;
    }
    public void actionPerformed(ActionEvent e){

        if (!gameover){
            wurm_active.decY();                                 // hebe Wurm 1px an und prüfe:
            if (wurm_active.getDx()==1)wurm_active.incX();      // wenn Bewegung nach rechts oder links aktiv,
            if (wurm_active.getDx()==-1)wurm_active.decX();     // führe Bewegen einmal manuell aus
            if (checkCollisions(wurm_active)==0){                          // prüfe, ob Kollision mit einem Terrain-Objekt
                wurm_active.incY();                             // wenn nicht, senke Wurm wieder ab um 1px
                if (wurm_active.getDx()==1)wurm_active.decX();  // und setze die Bewegung wieder um einen px zurück
                if (wurm_active.getDx()==-1)wurm_active.incX();
                wurm_active.move();                             // führe move() normal aus
            }else{
                wurm_active.decY();
                if (checkCollisions(wurm_active)==0) {
                    wurm_active.move();
                }else {
                    wurm_active.incY();
                }
                wurm_active.incY();
                if (wurm_active.getDx()==1)wurm_active.decX();
                if (wurm_active.getDx()==-1)wurm_active.incX();
            }
        }
        checkWurmDead();
        if (team1.size()>0) {               // hier auch noch für mehr teams, sobald mehr wurmstartpos vorhanden
            for (int i=0;i<team1.size();i++)
                falling(team1.get(i));
        }
        if (team2.size()>0) {
            for (int i = 0; i < team2.size(); i++)
                falling(team2.get(0));
        }        // und für die restlichen Teams später auch noch

        if (dead_wurm.size()>0) {
            for (int i = 0; i < dead_wurm.size(); i++)
                falling(dead_wurm.get(0));
        }

        winner = checkWinner();
        if(checkGameOver()) {
            gameover=true;
        }/*
        if(gameover&&!exitwindow){
            try {
                SpielMain exit = new SpielMain();
                exit.setBounds(300, 300, 400, 300);
                exit.setTitle("Game Over");
                LastScreen end = new LastScreen(spiel,exit);
                exit.add(end);
                exitwindow = true;
                exit.setVisible(true);
                spiel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                exit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }*/
        repaint();
    }

    protected void paintComponent(Graphics block){          // ab hier wird auf dem Board (=JPanel) gezeichnet
        super.paintComponent(block);
        if(levelnummer==1){                                 // je nach aktellem Level werden unterschiedliche Hintergrundobjekte gezeichnet:
            Graphics2D block1_0 = (Graphics2D) block;
            Graphics2D block1_1 = (Graphics2D) block;
            Graphics2D block1_2 = (Graphics2D) block;
            //block2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            // Beispiel für Kantenglättung, brauchen wir hier aber nicht. Die Objekte verschwinden ja hinter den Bildern
            block1_0.draw(block1_0_);
            block1_1.draw(block1_1_);     // mittlerer Block, (Input sind: x-Wert, y-Wert, Breite, Höhe)
            block1_2.draw(block1_2_);     // rechts, Mauerwürfel fehlt noch
        }else
        if(levelnummer==2){
            Graphics2D block2_1 = (Graphics2D) block;
            Graphics2D block2_2 = (Graphics2D) block;
            Graphics2D block2_3 = (Graphics2D) block;
            Graphics2D block2_4 = (Graphics2D) block;
            block2_1.draw(block2_1_);   //seife links
            block2_2.draw(block2_2_); //seife rechts
            block2_3.draw(block2_3_);    //wannenrand links
            block2_4.draw(block2_4_);   //wannenrand rechts
        }else
        if(levelnummer==3){
            Graphics2D block3_1 = (Graphics2D) block;
            Graphics2D block3_2 = (Graphics2D) block;
            Graphics2D block3_3 = (Graphics2D) block;
            block3_1.draw(block3_1_);  // Brücke Mittelteil
            block3_2.draw(block3_2_);      // Schräge links als Dreieck (Input sind: Array mit x-werten, Array mit y-Werten, Anzahl der Eckpunkte)
            block3_3.draw(block3_3_);      // Schräge rechts als Dreieck
        }

        Graphics2D waterblock = (Graphics2D) block;
        if (levelnummer==2) waterblock.draw(water_2);
        if (levelnummer==1||levelnummer==3) waterblock.draw(water);
    }

    public void paint(Graphics statics){          // ab hier wird wieder auf dem Board (=JPanel) gezeichnet, ähnlich wie oben, aber diesmal keine Shapes, sondern Images

        super.paint(statics);
        Graphics2D g2d = (Graphics2D) statics;

        if (show_bg) {
            g2d.drawImage(img.getImage(level.getBg()), 0, 0, null);        // Hintergrund je nach levelnummer wird von Klasse Level erfragt und an die Koordinaten (0,0) relativ zu null positioniert
            g2d.drawImage(img.getImage(level.getFg()), 0, 0, null);        // Vordergrund ebenso
        }
        if (team1.size()>0) {
            for (int i = 0; i < team1.size(); i++)
                g2d.drawImage(img.getImage(team1.get(i).getImagePath()), team1.get(i).getX(), team1.get(i).getY(), null);
            //g2d.drawImage(team1.get(i).getImage(), team1.get(i).getX(), team1.get(i).getY(), null);      // zeichnet Würmer an Startpositionen bzw die aktuelle Position.
            // Input sind: Image, x-Wert, y-Wert, relativ zu Positon
        }
        if (team2.size()>0) {
            for (int i = 0; i < team2.size(); i++)
                //g2d.drawImage(team2.get(i).getImage(), team2.get(i).getX(), team2.get(i).getY(), null);
                g2d.drawImage(img.getImage(team2.get(i).getImagePath()), team2.get(i).getX(), team2.get(i).getY(), null);
        }
        g2d.drawString("Round: "+round, 250, 15);           // Schreibt oben an Position (250,15) den Text Round: und fügt dann die aktuelle Rundennummer ein
        g2d.drawString("Level: "+levelnummer, 320, 15);     // Schreibt oben an Position (320,15) den Text Level: und fügt dann die aktuelle Levelnummer ein

        if (wurm_active.getWaffe()==1) {
            g2d.drawImage(img.getImage(bazooka.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        }
        if (wurm_active.getWaffe()==2) {
            g2d.drawImage(img.getImage(granate.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        }
        if (wurm_active.getWaffe()==3) {
            g2d.drawImage(img.getImage(pistole.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        }
        if (wurm_active.getWaffe()==4) {
            g2d.drawImage(img.getImage(schwert.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        }

        g2d.drawString("X", wurm_active.getX()+25, wurm_active.getY()-10);     // markiert aktuellen Wurm mit einem Buchstaben X

        if (team1.size()>0) {
            for (int i = 0; i < team1.size(); i++)
                g2d.drawString("" + team1.get(i).getHitpoints(), team1.get(i).getX() + 25, team1.get(i).getY()); // Hitpoints über jeden Wurm schreiben
        }
        if (team2.size()>0) {
            for (int i = 0; i < team2.size(); i++)
                g2d.drawString("" + team2.get(i).getHitpoints(), team2.get(i).getX() + 25, team2.get(i).getY());
        }

        if(dead_wurm.size()>0) {
            for (int i = 0; i < dead_wurm.size(); i++){
                g2d.drawImage(img.getImage(dead_wurm.get(i).getImagePath()), dead_wurm.get(i).getX(), dead_wurm.get(i).getY(), null);
            }
        }
        if(gameover) {
            g2d.drawString("GAME OVER", 300, 100);
            g2d.drawString("Team " + winner + " hat gewonnen!", 300, 115);
        }
        g2d.drawImage(img.getImage(bullet.getImagePath()), bullet.getX(), bullet.getY(), null);
    }

    public class AL extends KeyAdapter{             // der oben erstellte KeyListener ("AL") wird hier definiert
        public void keyReleased(KeyEvent e) {
            wurm_active.keyReleased(e);
        }

        public void keyPressed(KeyEvent e){         // bei gedrücktem Key wird abgefragt, welcher Key es war
            wurm_active.keyPressed(e);
            char key = e.getKeyChar();              // der Eingabewert des Keys wird in einen charakter umgewandelt

            if(levelnummer<3) {                     // damit nach Level 3 nicht wieder Level 1 kommt oder sonst etwas komisches, stoppen wir hier die Levelerhöhung
                if (key == 'l' && !gameover) {                   // Wenn "l" wie Level gedrückt wurde:
                    levelnummer++;                  // dann erhöhe die aktuelle Levelnummer um 1
                    level.level(levelnummer);       // aktualisiere den Wert auch in der Klasse Level
                    round=1;                        // mit jedem neuen Level starten wir wieder bei Runde 1
                    wurm_active=team1.get(0);
                    team1.get(0).setX(level.getWurm1posx());       // Startpositionen der Würmer, die sind hier anders als in Level 1, darum werden sie neu geladen
                    team1.get(0).setY(level.getWurm1posy());
                    team1.get(1).setX(level.getWurm2posx());
                    team1.get(1).setY(level.getWurm2posy());
                    team2.get(0).setX(level.getWurm3posx());
                    team2.get(0).setY(level.getWurm3posy());
                    team2.get(1).setX(level.getWurm4posx());
                    team2.get(1).setY(level.getWurm4posy());
                }
            }
            if(levelnummer==3) {                     // damit nach Level 3 nicht wieder Level 1 kommt oder sonst etwas komisches, stoppen wir hier die Levelerhöhung
                if (key == 'z' && !gameover) {                   // Wenn "l" wie Level gedrückt wurde:
                    levelnummer=1;                  // dann erhöhe die aktuelle Levelnummer um 1
                    level.level(levelnummer);       // aktualisiere den Wert auch in der Klasse Level
                    round=1;                        // mit jedem neuen Level starten wir wieder bei Runde 1
                    wurmnr1=0;
                    wurmnr2=0;          //gleiches noch für mehr teams (sobald mehr startpos verfügbar)
                    wurm_active=team1.get(0);
                    team1.get(0).setX(level.getWurm1posx());       // Startpositionen der Würmer, die sind hier anders als in Level 1, darum werden sie neu geladen
                    team1.get(0).setY(level.getWurm1posy());
                    team1.get(1).setX(level.getWurm2posx());
                    team1.get(1).setY(level.getWurm2posy());
                    team2.get(0).setX(level.getWurm3posx());
                    team2.get(0).setY(level.getWurm3posy());
                    team2.get(1).setX(level.getWurm4posx());
                    team2.get(1).setY(level.getWurm4posy());
                }
            }
            if (key == 'r' && !gameover) {                       // Wenn "r" wie Round gedrückt wurde:
                nextRound();
            }
            if (key == 'h') {
                show_bg=!show_bg;               // Hintergrund ausblenden
            }
            if(key=='1'){
                wurm_active.setWaffe(1);
                waffe_active = bazooka;
                System.out.println("Waffe 1 aktiv! (Bazooka)");   // Dummy für neue Waffe laden, Bild reinmachen usw.
            }
            if(key=='2'){
                wurm_active.setWaffe(2);
                waffe_active = pistole;
                System.out.println("Waffe 2 aktiv! (Pistole)");
            }
            if(key=='3'){
                wurm_active.setWaffe(3);
                waffe_active = granate;
                System.out.println("Waffe 3 aktiv! (Granate)");
            }
            if(key=='4'){
                wurm_active.setWaffe(4);
                waffe_active = schwert;
                System.out.println("Waffe 4 aktiv! (Schwert)");
            }
        }
    }

    public class ML extends MouseAdapter{

        public void mouseClicked(MouseEvent e) {
            double[] richtungsvektor=new double[2];
            boolean treffer;
            int i;

            if(e.getButton()==MouseEvent.BUTTON1) {         // bei linksklick Mausposition bestimmen
                mausPos = e.getPoint();


                richtungsvektor[0]=(mausPos.getX()-wurm_active.getX())/(Math.sqrt(Math.pow(mausPos.getX()-wurm_active.getX(),2)+Math.pow(mausPos.getY()-wurm_active.getY(),2)));        //normierter Richtungsvektor vom Schuss
                richtungsvektor[1]=(mausPos.getY()-wurm_active.getY())/(Math.sqrt(Math.pow(mausPos.getX()-wurm_active.getX(),2)+Math.pow(mausPos.getY()-wurm_active.getY(),2)));

                zielpunkt.setSize(1,1);
                zielpunkt.setLocation(wurm_active.getX(), wurm_active.getY());           //offset einfügen damit nicht selber direkt erwischt wird? zur Zeit wird Suizid durch fehlende Kollision verhindert


                treffer=false;
                i=1;        // HIER verändern für Offset beim Schiessen gegen Suizid!!!

                startPos.setLocation(wurm_active.getX(),wurm_active.getY());
                bullet.setX(startPos.getX());
                bullet.setY(startPos.getY());
                bullet.setVisible(true);

                while(!treffer){
                    if(startPos.getX()+i*richtungsvektor[0]<=0||startPos.getX()+i*richtungsvektor[0]>=800){
                        startPos.setLocation(startPos.getX()+i*richtungsvektor[0],startPos.getY()+i*richtungsvektor[1]);
                        i=1;
                        richtungsvektor[0]=-1*richtungsvektor[0];
                    }

                    if(startPos.getY()+i*richtungsvektor[1]<=0||startPos.getY()+i*richtungsvektor[1]>=450){
                        startPos.setLocation(startPos.getX()+i*richtungsvektor[0],startPos.getY()+i*richtungsvektor[1]);
                        i=1;
                        richtungsvektor[1]=-1*richtungsvektor[1];
                    }

                    zielpunkt.setLocation((int) (startPos.getX()+i*richtungsvektor[0]),(int)(startPos.getY()+i*richtungsvektor[1]));

                    bullet.setX(zielpunkt.getX());
                    bullet.setY(zielpunkt.getY());

                    repaint();

                    if(kollisionsabfrageRectangle(zielpunkt)!=null)treffer=true;
                    if(kollisionsabfrageWurm(zielpunkt)!=null)treffer=true;
                    i++;
                }
            }
        }
    }

    public Shape kollisionsabfrageRectangle(Rectangle movingObject){      // damit auch Polygone mit Shape statt Rectangle2D
        if(levelnummer==1) {
            if(block1_0_.intersects(movingObject))return block1_0_;
            if(block1_1_.intersects(movingObject))return block1_1_;
            if(block1_2_.intersects(movingObject))return block1_2_;
            return null;
        }else if(levelnummer==2){
            if(block2_1_.intersects(movingObject))return block2_1_;
            if(block2_2_.intersects(movingObject))return block2_2_;
            if(block2_3_.intersects(movingObject))return block2_3_;
            if(block2_4_.intersects(movingObject))return block2_4_;
            return null;
        }else if(levelnummer==3){
            if(block3_1_.intersects(movingObject))return block3_1_;     //Polygone fehlen!!! umbauen?
            if(block3_2_.intersects(movingObject))return block3_2_;
            if(block3_3_.intersects(movingObject))return block3_3_;
            return null;
        }
        return null;
    }

    public Wurm kollisionsabfrageWurm(Rectangle movingObject){
        if (team1.size()>0) {
            for(int i=0;i<team1.size();i++) {
                Rectangle wurm = team1.get(i).getBounds();
                if (wurm.intersects(movingObject) && team1.get(i) != wurm_active && team1.get(i).getAlive()) {
                    team1.get(i).takeDamage(waffe_active.angreifen());
                    return team1.get(i);
                }
            }
        }
        if (team2.size()>0) {
            for(int i=0;i<team2.size();i++) {
                Rectangle wurm = team2.get(i).getBounds();
                if (wurm.intersects(movingObject) && team2.get(i) != wurm_active && team2.get(i).getAlive()) {
                    team2.get(i).takeDamage(waffe_active.angreifen());
                    return team2.get(i);
                }
            }
        }

        return null; //dummy, hier eigentlich getroffener Wurm
    }

    public int checkCollisions(Wurm wurm){       //für falling()
        Rectangle wurm_ = wurm.getBounds();
        if (levelnummer==1 && (wurm_.intersects(block1_1_)||block1_0_.intersects(wurm_)||wurm_.intersects(block1_2_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==2 &&(wurm_.intersects(block2_1_)||wurm_.intersects(block2_2_)||wurm_.intersects(block2_3_)||wurm_.intersects(block2_4_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==3 &&(wurm_.intersects(block3_1_)||block3_2_.intersects(wurm_active.getBounds())||block3_3_.intersects(wurm_active.getBounds())||wurm_.getY()==450)){
            return 1; //boden
        }
        if ((levelnummer==1||levelnummer==3)&&wurm_.intersects(water)){
            wurm.ertrinken();
        }
        if ((levelnummer==2)&&wurm_.intersects(water_2)){
            wurm.ertrinken();
        }
        return 0;
    }
    public int checkCollisionsLeiche(WurmLeiche wurm){       //für falling()
        Rectangle wurm_ = wurm.getBounds();
        if (levelnummer==1 && (wurm_.intersects(block1_1_)||block1_0_.intersects(wurm_)||wurm_.intersects(block1_2_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==2 &&(wurm_.intersects(block2_1_)||wurm_.intersects(block2_2_)||wurm_.intersects(block2_3_)||wurm_.intersects(block2_4_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==3 &&(wurm_.intersects(block3_1_)||block3_2_.intersects(wurm_active.getBounds())||block3_3_.intersects(wurm_active.getBounds())||wurm_.getY()==450)){
            return 1; //boden
        }
        return 0;
    }

    public void falling(Wurm wurm){
        if (checkCollisions(wurm)==0)   //  collisiondetection
            wurm.incY();   //y++
    }

    public void falling(WurmLeiche wurm){
        if (checkCollisionsLeiche(wurm)==0)   //  collisiondetection
            wurm.incY();   //y++

    }

    public void checkWurmDead(){
        if (team1.size()>0) {
            for (int i = team1.size() - 1; i >= 0; i--) {
                if (!team1.get(i).getAlive()) {
                    dead_wurm.add(new WurmLeiche(team1.get(i).getFarbe()));
                    dead_wurm.get(dead_wurm.size() - 1).setX(team1.get(i).getX());
                    dead_wurm.get(dead_wurm.size() - 1).setY(team1.get(i).getY());
                    if (team1.get(i) == wurm_active) nextRound();
                    team1.remove(i);

                }
            }
        }
        if (team2.size()>0) {
            for (int i = team2.size() - 1; i >= 0; i--) {
                if (!team2.get(i).getAlive()) {
                    dead_wurm.add(new WurmLeiche(team2.get(i).getFarbe()));
                    dead_wurm.get(dead_wurm.size()-1).setX(team2.get(i).getX());
                    dead_wurm.get(dead_wurm.size()-1).setY(team2.get(i).getY());
                    if (team2.get(i) == wurm_active) nextRound();
                    team2.remove(i);
                }   // mehr Abfragen, sobald mehr wurmstartpos vorhanden
            }
        }
    }

    public void nextRound() {
        round++;                            // dann erhöhe die aktuelle Rundennummer um 1
        if (round % teams == 1 && team1.size()!=0) {                     // damit man rotiert, wir die aktuelle Rundennummer modulo Anzahl der Teams gerechnet, statt 2 noch ändern auf teams
            wurm_active = team1.get((wurmnr1 % team1.size()));
            wurmnr1++;
            wurm_active.getWaffe();
        } else if (round % teams == 0 && team2.size()!=0) {
            wurm_active = team2.get((wurmnr2 % team2.size()));
            wurmnr2++;
            wurm_active.getWaffe();
        }       //gleiches noch für mehr teams (sobald mehr startpos verfügbar)
    }

    public boolean checkGameOver(){
        if (team1.size()==0||team2.size()==0){      // noch auf mehr als 2 teams anpassen
            return true;
        }
        return false;
    }
    public int checkWinner(){
        if (team1.size()!=0 && team2.size()==0 && team3.size()==0 && team4.size()!=0 && team5.size()==0){      // noch auf mehr als 2 teams anpassen
            return 1;
        }
        if (team1.size()==0 && team2.size()!=0 && team3.size()==0 && team4.size()==0 && team5.size()==0){      // noch auf mehr als 2 teams anpassen
            return 2;
        }
        if (team1.size()==0 && team2.size()==0 && team3.size()!=0 && team4.size()==0 && team5.size()==0){      // noch auf mehr als 2 teams anpassen
            return 3;
        }
        if (team1.size()==0 && team2.size()==0 && team3.size()==0 && team4.size()!=0 && team5.size()==0){      // noch auf mehr als 2 teams anpassen
            return 4;
        }
        if (team1.size()==0 && team2.size()==0 && team3.size()==0 && team4.size()==0 && team5.size()!=0){      // noch auf mehr als 2 teams anpassen
            return 5;
        }
        return 0;
    }

    public boolean gameOver(){
        return gameover;
    }


	@Override
	public void update(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTable(List<String> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kick() {
		// TODO Auto-generated method stub
		
	}
}
