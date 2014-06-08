package com.thai.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class changeName {

	private List<ClientName> array;
	public List<ClientName> getArray() {
		return array;
	}
	public void setArray(List<ClientName> array) {
		this.array = array;
	}
	public static String neueName(String name)
	{
		
		
		changeName change = getList();
		for(ClientName client : change.getArray())
		{
			if(client.getName().equals(name)&&client.getNeuName()!=null)
			{
				return client.getNeuName();
			}
		}
		
		
		return null;
	}
	public static void changeName(ClientName user)
	{
	
		List<ClientName>list = new ArrayList<ClientName>();
	changeName	change = getList();
	 list.add(user);	 
		 
	 
	 changeName.saveList(list);
	}
	
	public static void saveList(List<ClientName>array)
	{
		Gson g = new Gson();
		
		changeName changeName = new changeName();
		changeName.setArray(array);
		String json = g.toJson(changeName);
		FileWriter out = null;
		
		// Schreibt die Informationen in eine Datei
		try{
			out = new FileWriter("src/res/changeName.json");
			// Schreibt, die Informationen aus dem konvertierten Objekt in eine json-Datei			
			out.write(json);
			
			// Test print
			System.out.println("HIER WICHTIG "+json);
			
			out.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}		
	}
	public static void createList(List<String>array){
		// Gson ist eine Java Bibliothek die das Java Objekte in ihr JSON-Format konvertiert
		
		
		List<ClientName>list = new ArrayList<ClientName>();
		
	
		Gson g = new Gson();
		
		changeName changeName = new changeName();
		changeName.setArray(list);
		String json = g.toJson(changeName);
		FileWriter out = null;
		
		// Schreibt die Informationen in eine Datei
		try{
			out = new FileWriter("src/res/changeName.json");
			// Schreibt, die Informationen aus dem konvertierten Objekt in eine json-Datei			
			out.write(json);
			
			// Test print
			System.out.println("HIER WICHTIG "+json);
			
			out.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	public static changeName getList(){
		Gson g = new Gson();
		changeName obj = null;
		
		try{
			BufferedReader brdata = new BufferedReader(new FileReader("src/res/changeName.json"));
	 
			// konvertiert den json-String wieder in ein Objekt
			obj = (changeName) g.fromJson(brdata, changeName.class);
	 
			// Test print
		

			// gebe Informationen weiter und lasse andere Klassen methode verarbeiten
			return obj;
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return obj;
	}
}
