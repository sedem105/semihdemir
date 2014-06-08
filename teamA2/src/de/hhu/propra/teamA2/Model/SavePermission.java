package de.hhu.propra.teamA2.Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.thai.client.Permission;

public class SavePermission {
	public static  void savePermission(Permission per){
		// Gson ist eine Java Bibliothek die das Java Objekte in ihr JSON-Format konvertiert
		Gson g = new Gson();
		
		String json = g.toJson(per);
		FileWriter out = null;
		
		// Schreibt die Informationen in eine Datei
		try{
			out = new FileWriter("src/res/Permission.json");
			// Schreibt, die Informationen aus dem konvertierten Objekt in eine json-Datei			
			out.write(json);
			
			// Test print
			System.out.println("HIER WICHTIG "+json);
			
			out.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static Permission loadPermission(){
		
		Gson g = new Gson();
		Permission obj = null;
		
		try{
			BufferedReader brdata = new BufferedReader(new FileReader("src/res/Permission.json"));
	 
			// konvertiert den json-String wieder in ein Objekt
			obj = (Permission) g.fromJson(brdata, Permission.class);
	 
			// Test print
		

			// gebe Informationen weiter und lasse andere Klassen methode verarbeiten
			return obj;
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return obj;
	}
}
