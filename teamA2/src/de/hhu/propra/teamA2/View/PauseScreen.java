package de.hhu.propra.teamA2.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by yuana on 07.06.14.
 */
public class PauseScreen extends JPanel{

    private SpielMain spielfeld;
    private SpielMenu menu;
    private JPanel controlPanel;
    private JButton restartButton;
    private JButton continueButton;
    private JButton endButton;

    public PauseScreen(SpielMain spiel, SpielMenu pause){
        pause.getContentPane().setLayout(new GridLayout(5,1));
        spielfeld = spiel;
        menu=pause;
        controlPanel = new JPanel();
        pause.getContentPane().add(controlPanel);
        keyAdapter();
        pause.setVisible(true);
    }

    public void keyAdapter(){

        restartButton = new JButton("New Game");
        continueButton = new JButton("Continue");
        endButton = new JButton("Exit");

        controlPanel.add(restartButton);
        controlPanel.add(continueButton);
        controlPanel.add(endButton);
        spielfeld.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.getContentPane().add(controlPanel);

        restartButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    StartView start = new StartView();
                    start.setVisible(true);
                    start.setBounds(100, 100, 500, 300);
                    spielfeld.dispose();
                    menu.dispose();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        });

        continueButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                menu.dispose();
            }
        });

        endButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);     // wenn nur EXIT_ON_CLOSE bleibt der Prozess (also das Spiel) offen, aber kein Fenster ist mehr offen, nicht mehr ansprechbar
                spielfeld.dispose();
                menu.dispose();
            }
        });
    }

}


