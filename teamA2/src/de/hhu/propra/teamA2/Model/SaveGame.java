package de.hhu.propra.teamA2.Model;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import java.io.BufferedReader;


public class SaveGame {

	private Spielstand spielstand;
	
	public void setSpielstand(Spielstand spielstand){
		this.spielstand = spielstand;
	}
	
	public Spielstand getSpielstand(){
		return this.spielstand;
	}
	
	
	public static  void saveGame(Spielstand spielstand){
		// Gson ist eine Java Bibliothek die das Java Objekte in ihr JSON-Format konvertiert
		Gson g = new Gson();
		
		String json = g.toJson(spielstand);
		FileWriter out = null;
		
		// Schreibt die Informationen in eine Datei
		try{
			out = new FileWriter("src/res/spielstand.json");
			// Schreibt, die Informationen aus dem konvertierten Objekt in eine json-Datei			
			out.write(json);
			
			// Test print
			System.out.println("HIER WICHTIG "+json);
			
			out.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static Spielstand loadGame(){
		
		Gson g = new Gson();
		Spielstand obj = null;
		
		try{
			BufferedReader brdata = new BufferedReader(new FileReader("src/res/spielstand.json"));
	 
			// konvertiert den json-String wieder in ein Objekt
			obj = (Spielstand) g.fromJson(brdata, Spielstand.class);
	 
			// Test print
			System.out.println("Aktuelles Level: "+obj.getAktuelle_Level());
			System.out.println("Aktuelle Runde: "+obj.getAktuelle_Runde());

			// gebe Informationen weiter und lasse andere Klassen methode verarbeiten
			return obj;
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return obj;
	}
}

