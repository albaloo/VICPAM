/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Point;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import domain.UserActivity.SharedDevice;

import pres.MapView;
import pres.JIcon;

public class Player {
	private ArrayList<Show> shows = new ArrayList<Show>();

	private ArrayList<Share> shares = new ArrayList<Share>();

	private ArrayList<Replication> replications = new ArrayList<Replication>();

	private ArrayList<ShowShare> inSharedDisplay = new ArrayList<ShowShare>();
	
	private ArrayList<ShowShare> hiddenShowShares = new ArrayList<ShowShare>();
	
	private ArrayList<ShowShare> hiddenActivities = new ArrayList<ShowShare>();
	
	private String name;
	
	private JIcon mapIcon;
	
	private int mapIconX;

	private int mapIconY;

	private JIcon timeAlignedViewIcon;
	
	private int timeAlignedViewIconX;

	private int timeAlignedViewIconY;

	private JIcon sharedDisplayIcon;

	private int sharedDisplayIconX;

	private int sharedDisplayIconY;

	private boolean showingShowShares = true;

	private boolean showingRays = false;

	private boolean playerIsShown = true;

	public void init() {
		mapIcon
				.setToolTipText("<html><b>Name:</b> " + name
						+ "<br><b>Number of Shows:</b> " + shows.size()
						+ "<br><b>Number of Shares:</b> " + shares.size()
						+ "<br><b>Number of Replications:</b> " + replications.size()
						+ "</html>");
		timeAlignedViewIcon
		.setToolTipText("<html><b>Name:</b> " + name
				+ "<br><b>Number of Shows:</b> " + shows.size()
				+ "<br><b>Number of Shares:</b> " + shares.size()
				+ "<br><b>Number of Replications:</b> " + replications.size()
				+ "</html>");

	}

	public Player(String name) {
		this.name = name;

		mapIcon = new JIcon("res/" + name + ".gif", "player");
		mapIcon.setSize(mapIcon.getImageSize(), mapIcon.getIconHeight());//mapIcon.getImageSize());

		timeAlignedViewIcon = new JIcon("res/" + name + ".gif", "player");
		timeAlignedViewIcon.setSize(timeAlignedViewIcon.getImageSize(), timeAlignedViewIcon.getIconHeight());//timeAlignedViewIcon.getImageSize());

		sharedDisplayIcon = new JIcon("res/" + name + ".gif", "player");
		sharedDisplayIcon.setSize(sharedDisplayIcon.getImageSize(), sharedDisplayIcon.getIconHeight());//sharedDisplayIcon.getImageSize());
		
		shows = new ArrayList<Show>();
		shares = new ArrayList<Share>();
		replications = new ArrayList<Replication>();
		inSharedDisplay = new ArrayList<ShowShare>();
	}

	public int getNumShows() {
		return shows.size();
	}

	public Show getShow(int index) {
		return shows.get(index);
	}

	public int getNumShares() {
		return shares.size();
	}

	public Share getShare(int index) {
		return shares.get(index);
	}

	public int getNumReplications() {
		return replications.size();
	}

	public Replication getReplication(int index) {
		return replications.get(index);
	}
	
	public int getNumItemsInSharedDisplay() {
		return inSharedDisplay.size();
	}
	
	public ShowShare getFromSharedDisplay(int index) {
		return inSharedDisplay.get(index);
	}

	public String getName() {
		return name;
	}

	public int getMapIconX() {
		return mapIconX;
	}

	public int getMapIconY() {
		return mapIconY;
	}

	public void setMapIconLocation(int x, int y) {
		mapIconX = x;
		mapIconY = y;
		mapIcon.setLocation(new Point(x, y));
	}

	public int getTimeAlignedViewIconX() {
		return timeAlignedViewIconX;
	}

	public int getTimeAlignedViewIconY() {
		return timeAlignedViewIconY;
	}

	public void setTimeAlignedViewLocation(int x, int y) {
		timeAlignedViewIconX = x;
		timeAlignedViewIconY = y;
		timeAlignedViewIcon.setLocation(new Point(x, y));
	}

	public int getSharedDisplayIconX() {
		return sharedDisplayIconX;
	}

	public int getSharedDisplayIconY() {
		return sharedDisplayIconY;
	}

	public void setSharedDisplayIconLocation(int x, int y) {
		sharedDisplayIconX = x;
		sharedDisplayIconY = y;
		sharedDisplayIcon.setLocation(new Point(x, y));
	}

	public void addShow(Show show) {
		shows.add(show);
	}

	public void addShare(Share share) {
		shares.add(share);
	}

	public void addReplication(Replication replication) {
		replications.add(replication);
	}

	public void addToSharedDisplay(ShowShare showShare) {
		inSharedDisplay.add(showShare);
	}
	
	public Point getCoordinates() {
		return new Point(getMapIconX(), getMapIconY());
	}

	public Point getConnectingPoint() {
		return new Point(mapIconX + getMapIcon().getWidth() / 2, mapIconY
				+ getMapIcon().getHeight() / 2);
	}

	public JIcon getMapIcon() {
		return mapIcon;
	}

	public JIcon getTimeAlignedViewIcon() {
		return timeAlignedViewIcon;
	}

	public JIcon getSharedDisplayIcon(){
		return sharedDisplayIcon;
	}
	
	public void hideShowShares(MapView map) {
		showingShowShares = false;
	
		for (Show showShare : shows) {
			showShare.hide(map);
		}

		for (Share showShare : shares) {
			showShare.hide(map);
		}

		map.repaint();
	}

	public void showShowShares(MapView map) {
		showingShowShares = true;
		for (Show showShare : shows) {
			showShare.show(map);
		}

		for (Share showShare : shares) {
			showShare.show(map);
		}
		map.repaint();
	}

	public boolean isShowingShowShares() {
		return showingShowShares;
	}

	public ArrayList<Replication> getReplications() {
		return replications;
	}

	public boolean isShowingRays() {
		return showingRays;
	}

	public void setShowingRays(boolean showingRays) {
		this.showingRays = showingRays;
	}

	public ArrayList<Show> getShows() {
		return shows;
	}

	public ArrayList<Share> getShares() {
		return shares;
	}
	
	public static ArrayList<UserActivity> sort(ArrayList<UserActivity> allActivities) {
		UserActivity[] activitiesArray = allActivities.toArray(new UserActivity[allActivities.size()]);
		Arrays.sort(activitiesArray);
		allActivities = new ArrayList<UserActivity>(Arrays.asList(activitiesArray));
		
		return allActivities;
	}

	public ArrayList<UserActivity> getActivitiesInRange(Time startTime, Time endTime, ArrayList<String> hideApps, boolean isShowHidden, boolean isShareHidden, boolean isReplicatedHidden, boolean isSharedDisplayHidden) {
		ArrayList<UserActivity> allActivities = new ArrayList<UserActivity>();
		allActivities.addAll(shows);
		allActivities.addAll(shares);
		allActivities.addAll(replications);
		allActivities = sort(allActivities);
		
		ArrayList<UserActivity> selectedActivitues = findActivitiesInRange(allActivities, startTime, endTime, hideApps, isShowHidden, isShareHidden, isReplicatedHidden, isSharedDisplayHidden);
		return selectedActivitues;
	}
	
	public static ArrayList<UserActivity> findActivitiesInRange(ArrayList<UserActivity> allActivities, Time startTime, Time endTime, ArrayList<String> hideApps, boolean isShowHidden, boolean isShareHidden, boolean isReplicatedHidden, boolean isSharedDisplayHidden){
		ArrayList<UserActivity> selectedActivities = new ArrayList<UserActivity>();
		
		for (UserActivity activity : allActivities) {
			if(!(activity.start.getTime() >= endTime.getTime() || activity.end.getTime() <= startTime.getTime())){
				boolean isHiddenApp = false;
				for(int i = 0; i < hideApps.size(); i++){
					if(activity.app.getName().equals(hideApps.get(i)))
						isHiddenApp = true;
				}
				boolean isHiddenActivity = false;
				if(((activity instanceof Replication) && isReplicatedHidden) || ((activity instanceof Share) && isShareHidden) || ((activity instanceof Show) && isShowHidden))
					isHiddenActivity = true;
				
				boolean isHiddenSharedDisplay = false;
				if((activity.getSharedDevice() == SharedDevice.SHAREDDISPLAY) && isSharedDisplayHidden)
					isHiddenSharedDisplay = true;
				
				if(!isHiddenApp && !isHiddenActivity && !isHiddenSharedDisplay)
					selectedActivities.add(activity);
			}
		}
		return selectedActivities;

	}
	
	public ArrayList<ShowShare> getShowSharesInRange(Time startTime, Time endTime, ArrayList<String> hideApps, boolean isShowHidden, boolean isShareHidden, boolean isReplicatedHidden, boolean isSharedDisplayHidden) {
		ArrayList<UserActivity> allShowShares = new ArrayList<UserActivity>();
		allShowShares.addAll(shows);
		allShowShares.addAll(shares);
		allShowShares = sort(allShowShares);
	
		ArrayList<UserActivity> tempSelectedActivities = findActivitiesInRange(allShowShares, startTime, endTime, hideApps, isShowHidden, isShareHidden, isReplicatedHidden, isSharedDisplayHidden);
		
		ArrayList<ShowShare> selectedActivities = new ArrayList<ShowShare>();
		for (UserActivity showShare : tempSelectedActivities) {
			selectedActivities.add((ShowShare)showShare);
		}
		
		return selectedActivities;
	}
	
	public ArrayList<ShowShare> getItemsInSharedDisplayInRange(Time startTime, Time endTime, ArrayList<String> hideApps) {
		ArrayList<ShowShare> selectedActivities = new ArrayList<ShowShare>();
		
		for (ShowShare item : inSharedDisplay) {
			if(!(item.start.getTime() >= endTime.getTime() || item.end.getTime() <= startTime.getTime())){
				boolean isHidden = false;
				for(int i = 0; i < hideApps.size(); i++){
					if(item.getApp().getName().equals(hideApps.get(i)))
						isHidden = true;
				}
				if(!isHidden)
					selectedActivities.add(item);
			}
		}
		return selectedActivities;
	}

	public void highlight() {
		mapIcon.highlight();
		timeAlignedViewIcon.highlight();
	}
	
	public void undoHighlight() {
		mapIcon.undoHighlight();
		timeAlignedViewIcon.undoHighlight();
	}
	
	public void highlightSharedDisplay() {
		sharedDisplayIcon.highlight();
	}
	
	public void undoHighlightSharedDisplay() {
		sharedDisplayIcon.undoHighlight();
	}
	
	public boolean playerIsShown(){
		return playerIsShown;
	}

	public void setPlayerIsShown(boolean playerIsShown){
		this.playerIsShown = playerIsShown;
	}
	
	public void addHiddenShowShare(ShowShare showShare){
		hiddenShowShares.add(showShare);
	}
	
	public void removeHiddenShowShare(ShowShare showShare){
		hiddenShowShares.remove(showShare);
	}
	
	public ShowShare getHiddenShowShare(int index){
		return hiddenShowShares.get(index);
	}
	
	public int hiddenShowShareSize(){
		return hiddenShowShares.size();
	}

	public ArrayList<ShowShare> hasHiddenApp(String appName) {
		ArrayList<ShowShare> toBeRemoved = new ArrayList<ShowShare>();
		for (ShowShare hiddenShowShare : hiddenShowShares) {
			if(hiddenShowShare.getApp().getName().equals(appName)){
				toBeRemoved.add(hiddenShowShare);
			}
		}
		
		for (ShowShare showShare : toBeRemoved) {
			hiddenShowShares.remove(showShare);
		}
		
		return toBeRemoved;
	}

	public void addHiddenActivity(ShowShare showShare){
		hiddenActivities.add(showShare);
	}
	
	public void removeHiddenActivity(ShowShare showShare){
		hiddenActivities.remove(showShare);
	}
	
	public ShowShare getHiddenActivity(int index){
		return hiddenActivities.get(index);
	}
	
	public int hiddenActivitySize(){
		return hiddenActivities.size();
	}

	public ArrayList<ShowShare> hasHiddenActivity(String activityName) {
		ArrayList<ShowShare> toBeRemoved = new ArrayList<ShowShare>();
		for (ShowShare hiddenShowShare : hiddenActivities) {
			if((hiddenShowShare instanceof Show && activityName.equals("show")) || (hiddenShowShare instanceof Share && activityName.equals("share"))){
				toBeRemoved.add(hiddenShowShare);
			}
		}
		
		for (ShowShare showShare : toBeRemoved) {
			hiddenActivities.remove(showShare);
		}
		
		return toBeRemoved;
	}
}
