package de.hhu.propra.teamA2.View;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class LastScreen extends JPanel{

	private SpielMain spielfeld,ende;
	private JPanel controlPanel;
	private JButton restartButton;
	private JButton endButton;
	
	public LastScreen(SpielMain spiel, SpielMain exit){
		exit.getContentPane().setLayout(new GridLayout(5,1));
		spielfeld = spiel;
        ende=exit;
		controlPanel = new JPanel();
		exit.getContentPane().add(controlPanel);
		keyAdapter();
		exit.setVisible(true);
	}
	
	public void keyAdapter(){

		restartButton = new JButton("New Game");
		endButton = new JButton("Exit");
		
		controlPanel.add(restartButton);
		controlPanel.add(endButton);
        spielfeld.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ende.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ende.getContentPane().add(controlPanel);
		
		restartButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                try{
                    StartView start = new StartView();
                    start.setVisible(true);
                    start.setBounds(100, 100, 500, 300);
                    spielfeld.dispose();
                    ende.dispose();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
			}
		});
		
		endButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
                System.exit(0);     // wenn nur EXIT_ON_CLOSE bleibt der Prozess (also das Spiel) offen, aber kein Fenster ist mehr offen, nicht mehr ansprechbar
                spielfeld.dispose();
                ende.dispose();
			}
		});
	}
	
}
