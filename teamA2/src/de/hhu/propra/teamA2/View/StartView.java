package de.hhu.propra.teamA2.View;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StartView extends JFrame {

	StartView() throws IOException{
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 500, 300); // positioniert das Fenster je 100 px von der linken oberen Ecke entfernt

        getContentPane().setLayout(null);

        JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/res/start_v3.png"))));
		background.setBounds(0, 0, 500, 300);  // positioniert den Hintergrund
		getContentPane().add(background);

        JButton netz = new JButton();
		netz.setBounds(57, 221, 176, 44);
        netz.setIcon(new ImageIcon("src/res/netzwerk.png"));
        getContentPane().add(netz);
        netz.setFocusPainted(false);
		//netz.setHideActionText(true);
        //netz.setBackground(UIManager.getColor("Button.background"));

        JButton local = new JButton();
		local.setBounds(275, 221, 176, 44);
        local.setIcon(new ImageIcon("src/res/lokal.png"));
        getContentPane().add(local);
		//local.setHideActionText(true);

		netz.addActionListener(new jumptoNetzGame());
		local.addActionListener(new jumptoLocalGame());
		}

	class jumptoNetzGame implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

            try {
            CheckConnect check = new CheckConnect();
			setVisible(false);
			check.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}

	class jumptoLocalGame implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
			    SpielMain spiel = new SpielMain(); // neues spiel-fenster oeffnen
				spiel.setTitle("Worms");
				spiel.setVisible(true);
                spiel.setBounds(100, 100, 800, 450);
                TextFieldEvent t = new TextFieldEvent(spiel);
				spiel.add(t);
				setVisible(false); // startfenster ausblenden
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
