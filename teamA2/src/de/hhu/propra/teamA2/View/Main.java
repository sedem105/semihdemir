package de.hhu.propra.teamA2.View;

import javax.swing.*;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		StartView start = new StartView();      // Erstellt ein neues Fenster (StartView ist ein JFrame) mit dem Startmenü
		start.setVisible(true);                 // und macht es sichtbar, das ist wichtig. ohne diese Zeile würde es nicht geöffnet werden.
        start.setTitle("Worms Start");          // Es kann und wird nämlich nach klicken auf den Button "lokales Spiel" wieder auf invisible gesetzt
	    start.setBounds(100, 100, 500, 300);
        start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }                                           // und ein neues Fenster (SpielMain, ebenfalls ein JFrame) geöffnet.

}
