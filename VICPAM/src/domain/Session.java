/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import domain.Indicator;

public class Session {
	private ArrayList<Player> players;

	private long selectedDuration;
	
	private Time start;
	
	private Time end;

	private Color themeColor = new Color(0x828790);
	
	private static int maxDuration = 0;
	
	public Session() {
		players = new ArrayList<Player>();
	}

	public Color getColor(){
		return themeColor;
	}
	
	public void setColor(Color color){
		themeColor = color;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}

	public int getNumOfPlayers() {
		return players.size();
	}

	public Player getPlayer(int index) {
		return players.get(index);
	}

	public long getSelectedDuration() {
		return selectedDuration;
	}

	public void setSelectedDuration(long duration) {
		if(duration == 0)
			this.selectedDuration = 1;
		else
			this.selectedDuration = duration;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}
	
	public long getDuration(){
		return end.getTime() - start.getTime();
	}

	public ArrayList<Application> getApplications() {
		ArrayList<Application> apps = new ArrayList<Application>();
		for (Player player : players) {
			for (Share share : player.getShares()) {
				apps.add(share.getApp());
			}
			for (Show show : player.getShows()) {
				apps.add(show.getApp());
			}
		}
		Collections.sort(apps);
		
		ArrayList<Application> selectedApps = new ArrayList<Application>();
		selectedApps.add(apps.get(0));
		int selectedIndex = 0;
		for (int i = 1; i < apps.size(); i++) {
			if(selectedApps.get(selectedIndex).getName().equals(apps.get(i).getName()))
				continue;
			else{
				selectedApps.add(apps.get(i));
				selectedIndex++;
			}
		}
		return selectedApps;
	}

	public Player getPlayerByName(String playerName) {
		for (Player player : players) {
			if (player.getName().equals(playerName))
				return player;
		}
		
		return null;
	}

	public void changeColorToBlue() {
		themeColor = new Color(0x354d94);
	}
	
	public void changeColorToRed() {
		themeColor = new Color(0x354d94);
	}

	public static void setMaxDuration(int maxDur) {
		maxDuration = maxDur;
	}
	
	public static int getMaxDuration(){
		return maxDuration;
	}

	public ArrayList<Indicator> getIndicators() {
		ArrayList<Indicator> indicators = new ArrayList<Indicator>();
	
		for (Player player : players) {
			for (Share share : player.getShares()) {
				indicators.add(new Indicator(share.getStart(), share.getColor()));
			}
			for (Show show : player.getShows()) {
				indicators.add(new Indicator(show.getStart(), show.getColor()));
			}
			for (Replication rep : player.getReplications()) {
				indicators.add(new Indicator(rep.getStart(), rep.getColor()));
			}
		}
		
		Indicator[] indicatorsArray = indicators.toArray(new Indicator[indicators.size()]);
		Arrays.sort(indicatorsArray);
		indicators = new ArrayList<Indicator>(Arrays.asList(indicatorsArray));

		return indicators;
	}

}
