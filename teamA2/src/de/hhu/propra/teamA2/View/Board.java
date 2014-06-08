package de.hhu.propra.teamA2.View;

import de.hhu.propra.teamA2.Model.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D; // hm, tja, keine Ahnung, warum er sich nicht beschwert, dass Polygon nicht auch importiert wird
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;

/**
 * Created by Susanna und Karsten on 01.06.14.
 */

public class Board extends JPanel implements ActionListener{      //Erstellen der Spielfläche, erstmal ein paar Variablen festlegen:

    int round = 1;      // Rundennummer, startet bei Runde 1
    int levelnummer = 1;  // Levelnummer, startet bei Level 1
    int wurmnr1=0;
    int wurmnr2=0;
    int wurmnr3=0;
    int wurmnr4=0;
    int wurmnr5=0;

    SpielMain spiel;
    Level level = new Level();  // erstelle ein Objekt der Klasse Level

    boolean paused = false;     //pause-fenster offen?
    int teams=1;
    boolean show_bg=true;

    int teamsize = 2;
    Wurm wurm_active;
    Mannschaft team_active;
    LinkedList<Wurm> team1;
    LinkedList<Wurm> team2;
    LinkedList<Wurm> team3;
    LinkedList<Wurm> team4;
    LinkedList<Wurm> team5;
    ArrayList<Mannschaft> teamlist;

    ImageLoad img = new ImageLoad();

    ArrayList<WurmLeiche> dead_wurm = new ArrayList<WurmLeiche>();

    boolean gameover = false;
    boolean exitwindow=false;
    String winner = null;

    Timer time;
    Bullet bullet;

    Point mausPos = new Point();              // Position der Maus beim Klicken zum Zielen mit Waffe
    Point zielPos = new Point();              // Endpunkt der Geraden beim Zielen
    Point startPos = new Point();             // Startpunkt der Gerade beim Zielen, wichtig bei Abprallern!

    Rectangle zielpunkt = new Rectangle(10,10,2,2);

    Bazooka bazooka = new Bazooka();
    Granate granate = new Granate();
    Pistole pistole = new Pistole();
    Schwert schwert = new Schwert();
    Waffe waffe_active=bazooka;

    //Terrain-Objekte (noch in Datei auslagern)
    //Level1
    int[] xpoints_0 = {71,249,249,71};
    int[] ypoints_0 = {320,290,414,414};
    Polygon block1_0_ = new Polygon(xpoints_0,ypoints_0,4);                   // Schräge links
    Rectangle2D.Double block1_1_ = new Rectangle2D.Double(249,187,289,221);   // mittlerer Block
    Rectangle2D.Double block1_2_ = new Rectangle2D.Double(538,294,186,120);   // rechts
    Rectangle2D.Double block1_3_ = new Rectangle2D.Double(608,243,52,52);     // rechts Mauerwürfel
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

    public Board(){
        Spielstand stand = SaveGame.loadGame(); //load spielstand

        teams = stand.getTeam().size();
        team1 = new LinkedList<Wurm>();
        team2 = new LinkedList<Wurm>();
        team3 = new LinkedList<Wurm>();
        team4 = new LinkedList<Wurm>();
        team5 = new LinkedList<Wurm>();
        teamlist = stand.getTeam();
        team_active = teamlist.get(0);

        for (int j = 1; j <= teamsize; j++){
            team1.add(new Wurm(stand.getTeam().get(0).getFarbe()));
        }
        for (int j = 1; j <= teamsize; j++)
            team2.add(new Wurm(stand.getTeam().get(1).getFarbe()));
        if (teams>=3) {
            for (int j = 1; j <= teamsize; j++)
                team3.add(new Wurm(stand.getTeam().get(2).getFarbe()));

        if (teams>=4) {
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

        if(teams>=3) {
            team3.get(0).setX(level.getWurm5posx());
            team3.get(0).setY(level.getWurm5posy());
            team3.get(1).setX(level.getWurm6posx());
            team3.get(1).setY(level.getWurm6posy());
        }
        if (teams>=4) {
            team4.get(0).setX(level.getWurm7posx());
            team4.get(0).setY(level.getWurm7posy());
            team4.get(1).setX(level.getWurm8posx());
            team4.get(1).setY(level.getWurm8posy());
        }
        if(teams==5) {
            team5.get(0).setX(level.getWurm9posx());
            team5.get(0).setY(level.getWurm9posy());
            team5.get(1).setX(level.getWurm10posx());
            team5.get(1).setY(level.getWurm10posy());
        }

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
            wurm_active.decY(); //

            if (wurm_active.getDx()==1)wurm_active.incX();      // wenn Bewegung nach rechts oder links aktiv,
            if (wurm_active.getDx()==-1)wurm_active.decX();     // führe Bewegen einmal manuell aus
            if (checkCollisions(wurm_active)==0){                          // prüfe, ob Kollision mit einem Terrain-Objekt
                wurm_active.incY();                             // wenn nicht, senke Wurm wieder ab um 1px
                wurm_active.incY(); //
                if (wurm_active.getDx()==1)wurm_active.decX();  // und setze die Bewegung wieder um einen px zurück
                if (wurm_active.getDx()==-1)wurm_active.incX();
                wurm_active.move();                             // führe move() normal aus
            }else{
                wurm_active.decY();                             // hebe Wurm um 1 px
                wurm_active.decY(); //
                if (checkCollisions(wurm_active)==0) {
                    wurm_active.move();                         // erlaube Bewegung
                }else {                                         // oder senke Wurm wieder ab um 1px
                    wurm_active.incY();
                    wurm_active.incY(); //
                }
                wurm_active.incY();                             // senke Wurm wieder ab um 1px
                wurm_active.incY(); //
                if (wurm_active.getDx()==1)wurm_active.decX();
                if (wurm_active.getDx()==-1)wurm_active.incX();
            }
        }
        checkWurmDead();
        checkTeamDead();
        if (team1.size()>0) {               // hier auch noch für mehr teams, sobald mehr wurmstartpos vorhanden
            for (int i=0;i<team1.size();i++)
                falling(team1.get(i));
        }
        if (team2.size()>0) {
            for (int i = 0; i < team2.size(); i++)
                falling(team2.get(i));
        }
        if (team3.size()>0) {
            for (int i = 0; i < team3.size(); i++)
                falling(team3.get(i));
        }
        if (team4.size()>0) {
            for (int i = 0; i < team4.size(); i++)
                falling(team4.get(i));
        }
        if (team5.size()>0) for (int i = 0; i < team5.size(); i++) falling(team5.get(i));


        if (dead_wurm.size()>0) {
            for (int i = 0; i < dead_wurm.size(); i++)
                falling(dead_wurm.get(i));
        }

        startPos.setLocation(wurm_active.getXoffset(),wurm_active.getYoffset());

        winner = checkWinner();
        if(checkGameOver()) {
            gameover=true;
        }
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
        }
        repaint();
    }

    protected void paintComponent(Graphics block){          // ab hier wird auf dem Board (=JPanel) gezeichnet
        super.paintComponent(block);
        if(levelnummer==1){                                 // je nach aktellem Level werden unterschiedliche Hintergrundobjekte gezeichnet:
            Graphics2D block1_0 = (Graphics2D) block;
            Graphics2D block1_1 = (Graphics2D) block;
            Graphics2D block1_2 = (Graphics2D) block;
            Graphics2D block1_3 = (Graphics2D) block;
            //block2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            // Beispiel für Kantenglättung, brauchen wir hier aber nicht. Die Objekte verschwinden ja hinter den Bildern
            block1_0.draw(block1_0_);
            block1_1.draw(block1_1_);     // mittlerer Block, (Input sind: x-Wert, y-Wert, Breite, Höhe)
            block1_2.draw(block1_2_);     // rechts
            block1_3.draw(block1_3_);     // rechts Mauerwürfel
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

        Graphics2D ziel = (Graphics2D) block;
        ziel.fillRect((int) zielpunkt.getX(),(int) zielpunkt.getY(),(int) zielpunkt.getWidth(),(int) zielpunkt.getHeight());

    }

    public void paint(Graphics statics){          // ab hier wird wieder auf dem Board (=JPanel) gezeichnet, ähnlich wie oben, aber diesmal keine Shapes, sondern Images

        super.paint(statics);
        Graphics2D g2d = (Graphics2D) statics;
        // Hintergrund ein- und ausblenden
        if (show_bg) {
            g2d.drawImage(img.getImage(level.getBg()), 0, 0, null);        // Hintergrund je nach levelnummer wird von Klasse Level erfragt und an die Koordinaten (0,0) relativ zu null positioniert
            g2d.drawImage(img.getImage(level.getFg()), 0, 0, null);        // Vordergrund ebenso
        }
        // Würmer zeichnen
        if (team1.size()>0) for (int i = 0; i < team1.size(); i++)
            g2d.drawImage(img.getImage(team1.get(i).getImagePath()), team1.get(i).getX(), team1.get(i).getY(), null);
        if (team2.size()>0) for (int i = 0; i < team2.size(); i++)
            g2d.drawImage(img.getImage(team2.get(i).getImagePath()), team2.get(i).getX(), team2.get(i).getY(), null);
        if (team3.size()>0) for (int i = 0; i < team3.size(); i++)
            g2d.drawImage(img.getImage(team3.get(i).getImagePath()), team3.get(i).getX(), team3.get(i).getY(), null);
        if (team4.size()>0) for (int i = 0; i < team4.size(); i++)
            g2d.drawImage(img.getImage(team4.get(i).getImagePath()), team4.get(i).getX(), team4.get(i).getY(), null);
        if (team5.size()>0) for (int i = 0; i < team5.size(); i++)
            g2d.drawImage(img.getImage(team5.get(i).getImagePath()), team5.get(i).getX(), team5.get(i).getY(), null);

        // Waffe am aktiven Wurm zeichnen
        if (wurm_active.getWaffe()==1) g2d.drawImage(img.getImage(bazooka.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        if (wurm_active.getWaffe()==2) g2d.drawImage(img.getImage(granate.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        if (wurm_active.getWaffe()==3) g2d.drawImage(img.getImage(pistole.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);
        if (wurm_active.getWaffe()==4) g2d.drawImage(img.getImage(schwert.getImagePath()), wurm_active.getX(), wurm_active.getY()+20, null);

        if (levelnummer==3) g2d.setColor(Color.WHITE);
        g2d.drawString("X", wurm_active.getX()+25, wurm_active.getY()-10);     // markiert aktuellen Wurm mit einem Buchstaben X
        g2d.drawString("Round: "+round, 250, 15);           // Schreibt oben an Position (250,15) den Text Round: und fügt dann die aktuelle Rundennummer ein
        g2d.drawString("Level: "+levelnummer, 320, 15);     // Schreibt oben an Position (320,15) den Text Level: und fügt dann die aktuelle Levelnummer ein

        // Hitpoints über jeden Wurm schreiben
        if (team1.size()>0) for (int i = 0; i < team1.size(); i++) g2d.drawString("" + team1.get(i).getHitpoints(), team1.get(i).getX() + 20, team1.get(i).getY());
        if (team2.size()>0) for (int i = 0; i < team2.size(); i++) g2d.drawString("" + team2.get(i).getHitpoints(), team2.get(i).getX() + 20, team2.get(i).getY());
        if (team3.size()>0) for (int i = 0; i < team3.size(); i++) g2d.drawString("" + team3.get(i).getHitpoints(), team3.get(i).getX() + 20, team3.get(i).getY());
        if (team4.size()>0) for (int i = 0; i < team4.size(); i++) g2d.drawString("" + team4.get(i).getHitpoints(), team4.get(i).getX() + 20, team4.get(i).getY());
        if (team5.size()>0) for (int i = 0; i < team5.size(); i++) g2d.drawString("" + team5.get(i).getHitpoints(), team5.get(i).getX() + 20, team5.get(i).getY());


        if(dead_wurm.size()>0) for (int i = 0; i < dead_wurm.size(); i++) g2d.drawImage(img.getImage(dead_wurm.get(i).getImagePath()), dead_wurm.get(i).getX(), dead_wurm.get(i).getY(), null);

        if(gameover) {
            g2d.drawString("GAME OVER", 300, 100);
            g2d.drawString(winner + " is winner of the game!", 300, 115);
        }
        if(!gameover)g2d.drawString(team_active.getName() + "'s turn!", 15, 15);
        g2d.drawImage(img.getImage(bullet.getImagePath()), bullet.getX(), bullet.getY(), null);
    }
    // der oben erstellte KeyListener ("AL") wird hier definiert
    public class AL extends KeyAdapter{
        public void keyReleased(KeyEvent e) {
            wurm_active.keyReleased(e);
        }

        public void keyPressed(KeyEvent e){         // bei gedrücktem Key wird abgefragt, welcher Key es war
            wurm_active.keyPressed(e);
            char key = e.getKeyChar();              // der Eingabewert des Keys wird in einen charakter umgewandelt
            int key_ = e.getKeyCode();              // Eingabewert des Keys wird in KeyCode umgewandelt

            if(levelnummer<=3) {                     // damit nach Level 3 nicht wieder Level 1 kommt oder sonst etwas komisches, stoppen wir hier die Levelerhöhung

                if (key == 'l' && !gameover) {                   // Wenn "l" wie Level gedrückt wurde:
                    if(levelnummer == 3) levelnummer = 1;
                    else levelnummer++;

                    level.level(levelnummer);
                    round=1;
                    wurmnr1=0;
                    wurmnr2=0;
                    wurmnr3=0;
                    wurmnr4=0;
                    wurmnr5=0;

                    if (team1.size()>0) wurm_active=team1.get(0);
                    else if (teams>=3 && team2.size()>0) wurm_active=team2.get(0);
                    else if (teams>=4 && team3.size()>0) wurm_active=team3.get(0);
                    else if (teams==5 && team4.size()>0) wurm_active=team4.get(0);

                    if (team1.size()>0){
                        team1.get(0).setX(level.getWurm1posx());   // Startpositionen der Würmer, die sind hier anders als in Level 1, darum werden sie neu geladen
                        team1.get(0).setY(level.getWurm1posy());
                    if (team1.size()>1){
                        team1.get(1).setX(level.getWurm2posx());
                        team1.get(1).setY(level.getWurm2posy());}}

                    if (team2.size()>0){
                        team2.get(0).setX(level.getWurm3posx());
                        team2.get(0).setY(level.getWurm3posy());
                    if (team2.size()>1){
                        team2.get(1).setX(level.getWurm4posx());
                        team2.get(1).setY(level.getWurm4posy());}}

                    if (team3.size()>1){
                        team3.get(0).setX(level.getWurm5posx());
                        team3.get(0).setY(level.getWurm5posy());
                    if (team3.size()>1){
                        team3.get(1).setX(level.getWurm6posx());
                        team3.get(1).setY(level.getWurm6posy());}}

                    if (team4.size()>1){
                        team4.get(0).setX(level.getWurm7posx());
                        team4.get(0).setY(level.getWurm7posy());
                    if (team4.size()>1){
                        team4.get(1).setX(level.getWurm8posx());
                        team4.get(1).setY(level.getWurm8posy());}}

                    if (team5.size()>1){
                        team5.get(0).setX(level.getWurm9posx());
                        team5.get(0).setY(level.getWurm9posy());
                    if (team5.size()>1){
                        team5.get(1).setX(level.getWurm10posx());
                        team5.get(1).setY(level.getWurm10posy());}}
                }
            }

            if (key == 'r' && !gameover) {      // Wenn "r" wie Round gedrückt wurde:
                nextRound();
            }
            if (key == 'h') {
                show_bg=!show_bg;               // Hintergrund ausblenden
            }
            if(key=='1'){                       // Waffe wechseln
                wurm_active.setWaffe(1);
                waffe_active = bazooka;
                System.out.println("Waffe 1 aktiv! (Bazooka)");
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
            if(key_==KeyEvent.VK_ESCAPE){
                try {
                    SpielMenu pause = new SpielMenu();
                    pause.setBounds(300, 300, 400, 300);
                    pause.setTitle("Menu");
                    PauseScreen menu = new PauseScreen(spiel,pause);
                    pause.add(menu);
                    paused = true;
                    pause.setVisible(true);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    // der oben erstellte MouseListener ("ML") wird hier definiert
    public class ML extends MouseAdapter{
        public void mouseClicked(MouseEvent e) {
            double[] richtungsvektor = new double[2];
            boolean treffer;
            int i;
            if(e.getButton()==MouseEvent.BUTTON1) {         // bei linksklick Mausposition bestimmen
                mausPos = e.getPoint();

                startPos.setLocation(wurm_active.getXoffset(),wurm_active.getYoffset());

                richtungsvektor[0]=(mausPos.getX()-startPos.getX())/(Math.sqrt(Math.pow(mausPos.getX()-startPos.getX(),2)+Math.pow(mausPos.getY()-startPos.getY(),2)));        //normierter Richtungsvektor vom Schuss
                richtungsvektor[1]=(mausPos.getY()-startPos.getY())/(Math.sqrt(Math.pow(mausPos.getX()-startPos.getX(),2)+Math.pow(mausPos.getY()-startPos.getY(),2)));

                zielpunkt.setSize(1,1);
                zielpunkt.setLocation((int) startPos.getX(),(int) startPos.getY());
                treffer=false;
                i=50;   // Offset beim Schießen, damit kein Suizid
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

                    paintImmediately(zielpunkt);

                    if(kollisionsabfrageRectangle(zielpunkt)!=null){
                        treffer=true;
                        nextRound();
                    }
                    if(kollisionsabfrageWurm(zielpunkt)!=null){
                        treffer=true;
                        nextRound();
                    }
                    i++;
                }
            }
        }
    }

    // trifft das Geschoss ein Terrain-Objekt oder ins Wasser?
    public Shape kollisionsabfrageRectangle(Rectangle movingObject){      // mit Shape statt Rectangle2D, damit auch Polygone
        if(levelnummer==1) {                                     // damit Schuss nicht durch objekte hindurch, sondern zerschellt
            if(block1_0_.intersects(movingObject))return block1_0_;
            if(block1_1_.intersects(movingObject))return block1_1_;
            if(block1_2_.intersects(movingObject))return block1_2_;
            if(block1_3_.intersects(movingObject))return block1_3_;
            if(water.intersects(movingObject))return water;
            return null;
        }else if(levelnummer==2){
            if(block2_1_.intersects(movingObject))return block2_1_;
            if(block2_2_.intersects(movingObject))return block2_2_;
            if(block2_3_.intersects(movingObject))return block2_3_;
            if(block2_4_.intersects(movingObject))return block2_4_;
            if(water_2.intersects(movingObject))return water_2;
            return null;
        }else if(levelnummer==3){
            if(block3_1_.intersects(movingObject))return block3_1_;
            if(block3_2_.intersects(movingObject))return block3_2_;
            if(block3_3_.intersects(movingObject))return block3_3_;
            if(water.intersects(movingObject))return water;
            return null;
        }
        return null;
    }
    // trifft das Geschoss einen Wurm?
    public Wurm kollisionsabfrageWurm(Rectangle movingObject){  //für Schuss: Wurm getroffen, dann Schaden an Wurm und Rückgabe getroffener Wurm
        if (team1.size()>0) {
            for(int i=0;i<team1.size();i++) {
                Rectangle wurm = team1.get(i).getBounds();
                if (wurm.intersects(movingObject) &&  team1.get(i).getAlive()) {
                    team1.get(i).takeDamage(waffe_active.angreifen());
                    return team1.get(i);
                }
            }
        }
        if (team2.size()>0) {
            for(int i=0;i<team2.size();i++) {
                Rectangle wurm = team2.get(i).getBounds();
                if (wurm.intersects(movingObject) && team2.get(i).getAlive()) {
                    team2.get(i).takeDamage(waffe_active.angreifen());
                    return team2.get(i);
                }
            }
        }
        if (team3.size()>0) {
            for(int i=0;i<team3.size();i++) {
                Rectangle wurm = team3.get(i).getBounds();
                if (wurm.intersects(movingObject) && team3.get(i).getAlive()) {
                    team3.get(i).takeDamage(waffe_active.angreifen());
                    return team3.get(i);
                }
            }
        }
        if (team4.size()>0) {
            for(int i=0;i<team4.size();i++) {
                Rectangle wurm = team4.get(i).getBounds();
                if (wurm.intersects(movingObject) && team4.get(i).getAlive()) {
                    team4.get(i).takeDamage(waffe_active.angreifen());
                    return team4.get(i);
                }
            }
        }
        if (team5.size()>0) {
            for(int i=0;i<team5.size();i++) {
                Rectangle wurm = team5.get(i).getBounds();
                if (wurm.intersects(movingObject) && team5.get(i).getAlive()) {
                    team5.get(i).takeDamage(waffe_active.angreifen());
                    return team5.get(i);
                }
            }
        }
        return null;
    }

    // trifft der Wurm auf ein Hindernis?
    public int checkCollisions(Wurm wurm){       //für falling()
        Rectangle wurm_ = wurm.getBounds();
        if (levelnummer==1 && (wurm_.intersects(block1_1_)||block1_0_.intersects(wurm_)||wurm_.intersects(block1_2_)||wurm_.intersects(block1_3_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==2 &&(wurm_.intersects(block2_1_)||wurm_.intersects(block2_2_)||wurm_.intersects(block2_3_)||wurm_.intersects(block2_4_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==3 &&(wurm_.intersects(block3_1_)||block3_2_.intersects(wurm_.getBounds())||block3_3_.intersects(wurm_.getBounds())||wurm_.getY()==450)){
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
    // trifft die fallende Wurmleiche auf ein Hindernis?
    public int checkCollisionsLeiche(WurmLeiche wurm){       //für falling()
        Rectangle wurm_ = wurm.getBounds();
        if (levelnummer==1 && (block1_1_.intersects(wurm_)||block1_0_.intersects(wurm_)||block1_2_.intersects(wurm_)||block1_3_.intersects(wurm_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==2 &&(block2_1_.intersects(wurm_)||block2_2_.intersects(wurm_)||block2_3_.intersects(wurm_)||block2_4_.intersects(wurm_)||wurm_.getY()==450)){
            return 1; //boden
        }
        if (levelnummer==3 &&(block3_1_.intersects(wurm_)||block3_2_.intersects(wurm_)||block3_3_.intersects(wurm_)||wurm_.getY()==450)){
            return 1; //boden
        }
        return 0;
    }

    // Dauerschleife fallen für Würmer
    public void falling(Wurm wurm){
        if (checkCollisions(wurm)==0)   //  collisiondetection
            wurm.incY();   //y++
    }
    // Dauerschleife fallen für Wurmleichen
    public void falling(WurmLeiche wurm){
        if (checkCollisionsLeiche(wurm)==0)   //  collisiondetection
            wurm.incY();   //y++
    }

    // Wurm tot?
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
                }
            }
        }
        if (team3.size()>0) {
            for (int i = team3.size() - 1; i >= 0; i--) {
                if (!team3.get(i).getAlive()) {
                    dead_wurm.add(new WurmLeiche(team3.get(i).getFarbe()));
                    dead_wurm.get(dead_wurm.size()-1).setX(team3.get(i).getX());
                    dead_wurm.get(dead_wurm.size()-1).setY(team3.get(i).getY());
                    if (team3.get(i) == wurm_active) nextRound();
                    team3.remove(i);
                }
            }
        }
        if (team4.size()>0) {
            for (int i = team4.size() - 1; i >= 0; i--) {
                if (!team4.get(i).getAlive()) {
                    dead_wurm.add(new WurmLeiche(team4.get(i).getFarbe()));
                    dead_wurm.get(dead_wurm.size()-1).setX(team4.get(i).getX());
                    dead_wurm.get(dead_wurm.size()-1).setY(team4.get(i).getY());
                    if (team4.get(i) == wurm_active) nextRound();
                    team4.remove(i);
                }
            }
        }
        if (team5.size()>0) {
            for (int i = team5.size() - 1; i >= 0; i--) {
                if (!team5.get(i).getAlive()) {
                    dead_wurm.add(new WurmLeiche(team5.get(i).getFarbe()));
                    dead_wurm.get(dead_wurm.size()-1).setX(team5.get(i).getX());
                    dead_wurm.get(dead_wurm.size()-1).setY(team5.get(i).getY());
                    if (team5.get(i) == wurm_active) nextRound();
                    team5.remove(i);
                }
            }
        }
    }
    // Team tot?
    public void checkTeamDead(){
        if (team1.size()==0) teamlist.get(0).setLost(true);
        if (team2.size()==0) teamlist.get(1).setLost(true);
        if (teams>=3) if (team3.size()==0) teamlist.get(2).setLost(true);
        if (teams>=4) if (team4.size()==0) teamlist.get(3).setLost(true);
        if (teams==5) if (team5.size()==0) teamlist.get(4).setLost(true);
    }

    //wer ist als nächstes dran?
    // (ist noch ziemlich lang. das mit dem modulo hab ich noch nicht besser hingekriegt.
    // wenn ihr die rechnung verbessern könnt, nur zu! Kann grad nicht mehr denken ^^")
    public void nextRound() {
        round++;
        if (teams==2){
            if (round % teams == 1 && team1.size()!=0) {
                wurm_active = team1.get((wurmnr1 % team1.size()));
                team_active = teamlist.get(0);
                wurmnr1++;
                wurm_active.getWaffe();
            } else if (round % teams == 0 && team2.size()!=0) {
                wurm_active = team2.get((wurmnr2 % team2.size()));
                team_active = teamlist.get(1);
                wurmnr2++;
                wurm_active.getWaffe();
            }
        }
        if(teams==3){
            if (round % teams == 1 && team1.size()!=0) {
                wurm_active = team1.get((wurmnr1 % team1.size()));
                team_active = teamlist.get(0);
                wurmnr1++;
                wurm_active.getWaffe();
            } else if (round % teams == 2 && team2.size()!=0) {
                wurm_active = team2.get((wurmnr2 % team2.size()));
                team_active = teamlist.get(1);
                wurmnr2++;
                wurm_active.getWaffe();
            } else if (round % teams == 0 && team3.size()!=0) {
                wurm_active = team3.get((wurmnr3 % team3.size()));
                team_active = teamlist.get(2);
                wurmnr3++;
                wurm_active.getWaffe();
            }
        }
        if(teams==4){
            if (round % teams == 1 && team1.size()!=0) {
                wurm_active = team1.get((wurmnr1 % team1.size()));
                team_active = teamlist.get(0);
                wurmnr1++;
                wurm_active.getWaffe();
            } else if (round % teams == 2 && team2.size()!=0) {
                wurm_active = team2.get((wurmnr2 % team2.size()));
                team_active = teamlist.get(1);
                wurmnr2++;
                wurm_active.getWaffe();
            } else if (round % teams == 3 && team3.size()!=0) {
                wurm_active = team3.get((wurmnr3 % team3.size()));
                team_active = teamlist.get(2);
                wurmnr3++;
                wurm_active.getWaffe();
            } else if (round % teams == 0 && team4.size()!=0) {
                wurm_active = team4.get((wurmnr4 % team4.size()));
                team_active = teamlist.get(3);
                wurmnr4++;
                wurm_active.getWaffe();
            }
        }
        if(teams==5){
            if (round % teams == 1 && team1.size()!=0) {
                wurm_active = team1.get((wurmnr1 % team1.size()));
                team_active = teamlist.get(0);
                wurmnr1++;
                wurm_active.getWaffe();
            } else if (round % teams == 2 && team2.size()!=0) {
                wurm_active = team2.get((wurmnr2 % team2.size()));
                team_active = teamlist.get(1);
                wurmnr2++;
                wurm_active.getWaffe();
            } else if (round % teams == 3 && team3.size()!=0) {
                wurm_active = team3.get((wurmnr3 % team3.size()));
                team_active = teamlist.get(2);
                wurmnr3++;
                wurm_active.getWaffe();
            } else if (round % teams == 4 && team4.size()!=0) {
                wurm_active = team4.get((wurmnr4 % team4.size()));
                team_active = teamlist.get(3);
                wurmnr4++;
                wurm_active.getWaffe();
            } else if (round % teams == 0 && team5.size()!=0) {
                wurm_active = team5.get((wurmnr5 % team5.size()));
                team_active = teamlist.get(4);
                wurmnr5++;
                wurm_active.getWaffe();
            }
        }
    }

    // ist ein Team tot? (vielleicht so noch nicht ganz sinnvoll)
    public boolean checkGameOver(){     // fragt, ob für ein team gameover eingetreten ist
        if (teams==2) if (teamlist.get(0).getLost() ^ teamlist.get(1).getLost())return true;
        if (teams==3) if (teamlist.get(0).getLost() ^ teamlist.get(1).getLost() ^ teamlist.get(2).getLost())return true;
        if (teams==4) if (teamlist.get(0).getLost() ^ teamlist.get(1).getLost() ^ teamlist.get(2).getLost() ^ teamlist.get(3).getLost())return true;
        if (teams==5) if (teamlist.get(0).getLost() ^ teamlist.get(1).getLost() ^ teamlist.get(2).getLost() ^ teamlist.get(3).getLost() ^ teamlist.get(4).getLost())return true;
        return false;
    }

    public Mannschaft getTeamActive(){
        return team_active;
    }

    public boolean getHostActive(){
        if (team_active == teamlist.get(0))return true;
        return false;
    }
    // welches Team ist als einziges übrig?
    public String checkWinner(){
        if (team1.size()!=0 && team2.size()==0 && team3.size()==0 && team4.size()==0 && team5.size()==0)return teamlist.get(0).getName();
        if (team1.size()==0 && team2.size()!=0 && team3.size()==0 && team4.size()==0 && team5.size()==0)return teamlist.get(1).getName();
        if (team1.size()==0 && team2.size()==0 && team3.size()!=0 && team4.size()==0 && team5.size()==0)return teamlist.get(2).getName();
        if (team1.size()==0 && team2.size()==0 && team3.size()==0 && team4.size()!=0 && team5.size()==0)return teamlist.get(3).getName();
        if (team1.size()==0 && team2.size()==0 && team3.size()==0 && team4.size()==0 && team5.size()!=0)return teamlist.get(4).getName();
        return null;
    }

    public boolean gameOver(){
        return gameover;
    }
    // hier wollt ich, wenn pause-fenster offen ist, das spiel pausieren lassen, also, dass keiner währenddessen was machen kann.
    // aber ich weiß noch nicht, wie das esc-fenster und das board miteinander kommunizieren können. muss vllt von außerhalb gemacht werden
    public void setContinue(){
        paused = false;
    }
}