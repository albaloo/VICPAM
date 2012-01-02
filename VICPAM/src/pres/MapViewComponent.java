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
import domain.UserActivity;

@SuppressWarnings("serial")
public class MapViewComponent extends JPanel{

	private ArrayList<ShowShare> allShowShares = new ArrayList<ShowShare>();
	int allShowSharesIndex = 0;
		
	private Color themeColor;

	private MapView map;
	
	private Player currentPlayer;
	
	public MapViewComponent(MapView map, Player player) {
		this.setLayout(null);
		themeColor = map.getSession().getColor();//new Color(0x0c439c);
		this.map = map;
		this.currentPlayer = player;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.setColor(new Color(0xf8f5f4));
		g2.fillRoundRect(0, 0, getWidth()-5, getHeight()-5, 15, 15);
		
		g2.setStroke(new BasicStroke(3f));
		g2.setColor(themeColor);
		g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 15, 15); 

		updateSize();
		g2.dispose();
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	public void updateSize() {
		ArrayList<Point> possiblePoints = findShowSharePoints(currentPlayer);
		int possiblePointIndex = 0;

		for (ShowShare comp: allShowShares) {
			comp.setMapIconLocation(possiblePoints.get(possiblePointIndex).x, possiblePoints.get(possiblePointIndex).y);
			possiblePointIndex++;
		}
	}

	public void setThemeColor(Color color) {
		this.themeColor = color;
		this.repaint();
	}

	public void paintPlayersAndActivities(Time startTime, Time endTime) {
		currentPlayer.setMapIconLocation(3, 3);

		currentPlayer.getMapIcon().addMouseListener(
				new PlayerHover(currentPlayer, map));

		this.add(currentPlayer.getMapIcon());		
		
		paintShowSharesOfPlayer(startTime, endTime);

	}

	void paintShowSharesOfPlayer(Time startTime, Time endTime) {
		ArrayList<UserActivity> activitiesInRange = currentPlayer.getActivitiesInRange(startTime, endTime, map.getMainForm().getHiddenApps(), map.getMainForm().isShowHidden(), map.getMainForm().isShareHidden(), map.getMainForm().isReplicatedHidden(), map.getMainForm().isSharedDisplayHidden());
		ArrayList<Point> possiblePoints = findShowSharePoints(currentPlayer);
		
		int possiblePointsIndex = 0;
		
		for(int i = 0; i < activitiesInRange.size(); i++) {
			if(activitiesInRange.get(i) instanceof Show && !map.getMainForm().isShowHidden()) {
				if(possiblePointsIndex > possiblePoints.size())
					System.out.println("Number of showed applications was greater than number of possible points");
				
				paintShowShare(((Show)activitiesInRange.get(i)), possiblePoints.get(possiblePointsIndex));
				allShowShares.add(((Show)activitiesInRange.get(i)));
				allShowSharesIndex++;
				possiblePointsIndex ++;
				
			}
			if(activitiesInRange.get(i) instanceof Share && !map.getMainForm().isShareHidden()) {
				if(possiblePointsIndex > possiblePoints.size())
					System.out.println("Number of shared applications was greater than number of possible points");
				
				paintShowShare(((Share)activitiesInRange.get(i)), possiblePoints.get(possiblePointsIndex));
				allShowShares.add(((Share)activitiesInRange.get(i)));
				allShowSharesIndex++;
				possiblePointsIndex ++;	
				
			}
		}
	}

	private void paintShowShare(ShowShare showShare, Point paintPoint) {
		showShare.setMapIconLocation(paintPoint.x, paintPoint.y);
		
		if(showShare.getMapIcon().getMouseListeners().length == 2)
			showShare.getMapIcon().addMouseListener(new ShowShareHover(map, showShare));

		this.add(showShare.getMapIcon());
	}	

	private ArrayList<Point> findShowSharePoints(Player player){
		ArrayList<Point> points = new ArrayList<Point>();
		
		if(getWidth() < MapView.getBlockSize()*4 || getHeight() < MapView.getBlockSize()*4) {
		}
		
		int maxWidthSize = (getWidth())/MapView.getBlockSize();
		int maxHeightSize = (getHeight())/MapView.getBlockSize();
		
		int xCoordinate = player.getMapIconX();
		int yCoordinate = player.getMapIconY();
		
		for(int i = 0; i < maxHeightSize; i++) {
			for(int j = 0; j < maxWidthSize; j++) {
				if(i==0 && j==0) {
					xCoordinate += MapView.getBlockSize();
					continue;
				}
				else {
					points.add(new Point(xCoordinate, yCoordinate));
					xCoordinate += MapView.getBlockSize();
				}
			}
			yCoordinate += MapView.getBlockSize();
			xCoordinate = player.getMapIconX();
		}
		
		return points;
	}

	public void updateLocation() {
		ArrayList<Point> possiblePoints = findShowSharePoints(currentPlayer);	
		int possiblePointsIndex = 0;
		
		for (ShowShare showShare : allShowShares) {
			showShare.setMapIconLocation(possiblePoints.get(possiblePointsIndex).x, possiblePoints.get(possiblePointsIndex).y);
			possiblePointsIndex++;
		}
		
	}
	
	public void removeShowShares() {

		for (ShowShare showShare : allShowShares) {
			this.remove(showShare.getMapIcon());
		}
		
		allShowShares.clear();
		allShowSharesIndex = 0;
	}

	public void hideApps(String appName) {
		ArrayList<ShowShare> tobeDeleted = new ArrayList<ShowShare>();

		for (ShowShare showShare : allShowShares) {
			if(showShare.getApp().getName().equals(appName)){
				this.remove(showShare.getMapIcon());
				tobeDeleted.add(showShare);
				showShare.getOwner().addHiddenShowShare(showShare);
			}
		}
		
		for (int j = 0; j < tobeDeleted.size(); j++) {		
			boolean isRemoved = allShowShares.remove(tobeDeleted.get(j));
			if(isRemoved){
				allShowSharesIndex--;
			}

		}

	}

	public void showApps(String appName) {
		int possiblePointsIndex = allShowShares.size();
		ArrayList<ShowShare> hiddenShowShares = currentPlayer.hasHiddenApp(appName);
		if(hiddenShowShares != null && hiddenShowShares.size() != 0){
			for (ShowShare showShare : hiddenShowShares) {
				allShowShares.add(showShare);
				allShowSharesIndex++;
				ArrayList<Point> possiblePoints = findShowSharePoints(currentPlayer);
				showShare.setMapIconLocation(possiblePoints.get(possiblePointsIndex).x, possiblePoints.get(possiblePointsIndex).y);
				this.add(showShare.getMapIcon());
				possiblePointsIndex++;
			}
		}
				
	}

	public void hideActivity(String activityName) {
		ArrayList<ShowShare> tobeRemoved = new ArrayList<ShowShare>();
		
		for (ShowShare showShare : allShowShares) {
			if(showShare instanceof Show && map.getMainForm().isShowHidden()){
				tobeRemoved.add(showShare);
				this.remove(showShare.getMapIcon());
				showShare.getOwner().addHiddenActivity(showShare);
			}else if(showShare instanceof Share && map.getMainForm().isShareHidden()){
				tobeRemoved.add(showShare);
				this.remove(showShare.getMapIcon());
				showShare.getOwner().addHiddenActivity(showShare);
			}
		}
		
		for (int j = 0; j < tobeRemoved.size(); j++) {		
			boolean isRemoved = allShowShares.remove(tobeRemoved.get(j)); 
			if(isRemoved){
				allShowSharesIndex--;
			}
		}

	}

	public void showActivity(String activityName) {
		ArrayList<ShowShare> hiddenShowShares = currentPlayer.hasHiddenActivity(activityName);
		if(hiddenShowShares != null && hiddenShowShares.size() != 0){
			int possiblePointsIndex = allShowShares.size();
			for (ShowShare showShare : hiddenShowShares) {
				allShowShares.add(showShare);
				allShowSharesIndex++;
				ArrayList<Point> possiblePoints = findShowSharePoints(currentPlayer);
				showShare.setMapIconLocation(possiblePoints.get(possiblePointsIndex).x, possiblePoints.get(possiblePointsIndex).y);
				this.add(showShare.getMapIcon());
				possiblePointsIndex++;
			}
		}
	
	}

}
