package de.hhu.propra.teamA2.Model;

import java.util.ArrayList;

public class Spielstand {

	private int aktuelle_Level;
	private int aktuelle_Runde;
	private ArrayList<Mannschaft>team;
	
	public ArrayList<Mannschaft> getTeam() {
		return team;
	}
	public void setTeam(ArrayList<Mannschaft> team) {
		this.team = team;
	}

	public int getAktuelle_Level() {
		return aktuelle_Level;
	}
	public void setAktuelle_Level(int aktuelle_Level) {
		this.aktuelle_Level = aktuelle_Level;
	}

	public int getAktuelle_Runde() {
		return aktuelle_Runde;
	}
	public void setAktuelle_Runde(int aktuelle_Runde) {
		this.aktuelle_Runde = aktuelle_Runde;
	}
	
}
