/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.JPanel;

import domain.Player;
import domain.Share;
import domain.Show;
import domain.ShowShare;

@SuppressWarnings("serial")
public class SharedDisplay extends JPanel{

	private ArrayList<Point> possiblePoints = new ArrayList<Point>();
	private int possiblePointIndex = 0;
	
	private ArrayList<SharedDisplayComponent> allComponents = new ArrayList<SharedDisplayComponent>();
	
	private ArrayList<SharedDisplayComponent> hiddenComponents = new ArrayList<SharedDisplayComponent>();
	
	private ArrayList<SharedDisplayComponent> hiddenActivities = new ArrayList<SharedDisplayComponent>();
	
	private Color themeColor;

	private MapView map;
	
	public SharedDisplay(MapView map) {
		this.setLayout(null);
		themeColor = map.getSession().getColor();//new Color(0x0c439c);
		this.map = map;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setColor(new Color(0xf8f5f4));
		g2.fillRoundRect(0, 0, getWidth()-5, getHeight()-5, 15, 15);
		
		g2.setStroke(new BasicStroke(3f));
		g2.setColor(themeColor);
		g2.drawString("Shared Display", getWidth()/3, 15);
		g2.drawLine(0, 20, getWidth()-5, 20);
		g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 15, 15); 

		updateSize();
		g2.dispose();
	}

	public MapView getMap(){
		return map;
	}
	
	public void paintPlayerActivityInSharedDisplay(Player player, Time startTime, Time endTime) {
		ArrayList<ShowShare> itemsInRange = player.getItemsInSharedDisplayInRange(startTime, endTime, map.getHiddenApps());
		for(int i = 0; i < itemsInRange.size(); i++) {
			SharedDisplayComponent comp = new SharedDisplayComponent(itemsInRange.get(i), player, themeColor, this);
			comp.setLocation(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y);
			comp.setBounds(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y, 2*MapView.getBlockSize()+20, MapView.getBlockSize()+5);
			comp.placeActivityAndPlayer();
			this.add(comp);
			possiblePointIndex++;
			allComponents.add(comp);
		}
	}
	
	public void removeAllActivitiesInSharedDisplay() {
		for (SharedDisplayComponent comp : allComponents) {
			this.remove(comp);
		}
		allComponents.clear();
		possiblePointIndex = 0;
	}
	
	public void findPlacesForPlayersActivities() {
		int maxWidth = getWidth()/(2*MapView.getBlockSize()+20);
		int maxHeight = getHeight()/(MapView.getBlockSize());
		
		int xCoordinate = 5;
		int yCoordinate = 25;
		
		for(int i = 0; i < maxHeight; i++) {
			for(int j = 0; j < maxWidth; j++) {
				possiblePoints.add(new Point(xCoordinate, yCoordinate));
				xCoordinate += 2*MapView.getBlockSize()+20;
			}
			yCoordinate += MapView.getBlockSize()+5;
			xCoordinate = 5;
		}
	}
	
	public void updateSize() {
		possiblePoints.clear();
		possiblePointIndex = 0;
		findPlacesForPlayersActivities();
		for (SharedDisplayComponent comp: allComponents) {
			comp.setLocation(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y);
			comp.setBounds(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y, 2*MapView.getBlockSize()+20, MapView.getBlockSize()+5);
			possiblePointIndex++;
		}
	}

	public void setThemeColor(Color color) {
		this.themeColor = color;
		for (SharedDisplayComponent comp: allComponents) {
			comp.setThemeColor(color);
		}
		this.repaint();
	}

	public void removeAllActivitiesOfPlayerInSharedDisplay(Player player) {
		ArrayList<SharedDisplayComponent> tobeDeleted = new ArrayList<SharedDisplayComponent>();
		for (SharedDisplayComponent comp : allComponents) {
			if(comp.player.getName().equals(player.getName())){
				this.remove(comp);
				tobeDeleted.add(comp);
				possiblePointIndex--;
			}
		}
		
		for(int i = 0; i < tobeDeleted.size(); i++)
			allComponents.remove(tobeDeleted.get(i));
		
		repaint();
	}
	
	public void hideApp(String appName){
		ArrayList<SharedDisplayComponent> tobeDeleted = new ArrayList<SharedDisplayComponent>();
		for (SharedDisplayComponent comp : allComponents) {
			if(comp.showShare.getApp().getName().equals(appName)){
				this.remove(comp);
				tobeDeleted.add(comp);
				hiddenComponents.add(comp);
				possiblePointIndex--;
			}
		}
		
		for(int i = 0; i < tobeDeleted.size(); i++)
			allComponents.remove(tobeDeleted.get(i));
		
		repaint();
	}
	
	public void showApp(String appName){
		ArrayList<SharedDisplayComponent> tobeDeleted = new ArrayList<SharedDisplayComponent>();

		for (SharedDisplayComponent comp : hiddenComponents) {
			if(comp.showShare.getApp().getName().equals(appName)){
				comp.setLocation(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y);
				this.add(comp);
				allComponents.add(comp);
				tobeDeleted.add(comp);
				possiblePointIndex++;
			}
		}
		
		for(int i = 0; i < tobeDeleted.size(); i++)
			hiddenComponents.remove(tobeDeleted.get(i));
		
		repaint();
	}

	public void hideActivity(String activityName){
		ArrayList<SharedDisplayComponent> tobeDeleted = new ArrayList<SharedDisplayComponent>();
		for (SharedDisplayComponent comp : allComponents) {
			if((comp.showShare instanceof Show && activityName.equals("show")) || (comp.showShare instanceof Share && activityName.equals("share"))){
				this.remove(comp);
				tobeDeleted.add(comp);
				hiddenActivities.add(comp);
				possiblePointIndex--;
			}
		}
		
		for(int i = 0; i < tobeDeleted.size(); i++)
			allComponents.remove(tobeDeleted.get(i));
		
		repaint();
	}

	public void showActivity(String activityName) {
		ArrayList<SharedDisplayComponent> tobeDeleted = new ArrayList<SharedDisplayComponent>();

		for (SharedDisplayComponent comp : hiddenActivities) {
			if((comp.showShare instanceof Show && activityName.equals("show")) || (comp.showShare instanceof Share && activityName.equals("share"))){
				comp.setLocation(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y);
				this.add(comp);
				allComponents.add(comp);
				tobeDeleted.add(comp);
				possiblePointIndex++;
			}
		}
		
		for(int i = 0; i < tobeDeleted.size(); i++)
			hiddenActivities.remove(tobeDeleted.get(i));
		
		repaint();		
	}
	
}
