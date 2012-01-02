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

import pres.JIcon;
import pres.MapView;

public class ShowShare extends UserActivity{

	private ArrayList<Replication> replications = new ArrayList<Replication>();

	private JIcon mapIcon;
	
	private int mapIconX;

	private int mapIconY;

	private JIcon timeAlignedViewIcon;
	
	private int timeAlignedViewIconX;

	private int timeAlignedViewIconY;	
	
	private JIcon sharedDisplayIcon;
	
	private int sharedDisplayIconX;

	private int sharedDisplayIconY;	

	private boolean showingRays = false;

	private long totalDuration;

	public ShowShare(Application app, Player owner, Time start, Time end,
			long totalDuration, String activityIconPath, SharedDevice sharedDevice) {
		super(start, end, owner, app, activityIconPath, sharedDevice);
		this.totalDuration = totalDuration;

		mapIcon = new JIcon(getAppIconName(), "app");
		mapIcon.setScale((double) (end.getTime() - start.getTime()) / totalDuration);
		mapIcon.setSize(mapIcon.getImageSize(), mapIcon.getImageSize());
		mapIcon.setToolTipText("<html><b>Start Time:</b> " + start + " s<br>" + "<b>End Time:</b> "
				+ end + " s<br>" + "<b>Application Name:</b> " + getApp().getName() +"</html>");
		
		timeAlignedViewIcon = new JIcon(getAppIconName(), "app");
		timeAlignedViewIcon.setScale((double) (end.getTime() - start.getTime()) / totalDuration);
		timeAlignedViewIcon.setSize(timeAlignedViewIcon.getImageSize(), timeAlignedViewIcon.getImageSize());
		timeAlignedViewIcon.setToolTipText("<html><b>Start Time:</b> " + start + " s<br>" + "<b>End Time:</b> "
				+ end + " s<br>" + "<b>Application Name:</b> " + getApp().getName() +"</html>");

		sharedDisplayIcon = new JIcon(getAppIconName(), "app");
		sharedDisplayIcon.setScale((double) (end.getTime() - start.getTime()) / totalDuration);
		sharedDisplayIcon.setSize(sharedDisplayIcon.getImageSize(), sharedDisplayIcon.getImageSize());
		sharedDisplayIcon.setToolTipText("<html><b>Start Time:</b> " + start + " s<br>" + "<b>End Time:</b> "
				+ end + " s<br>" + "<b>Application Name:</b> " + getApp().getName() +"</html>");

	}

	public boolean isReplicated() {
		if (replications.size() > 0)
			return true;
		else
			return false;
	}

	public void setMapIconLocation(int x, int y) {
		mapIconX = x;
		mapIconY = y;
		mapIcon.setLocation(x, y);
	}

	public void setTimeAlignedViewIconLocation(int x, int y) {
		timeAlignedViewIconX = x;
		timeAlignedViewIconY = y;
		timeAlignedViewIcon.setLocation(x, y);
	}

	public void setSharedDisplayIconLocation(int x, int y) {
		sharedDisplayIconX = x;
		sharedDisplayIconY = y;
		sharedDisplayIcon.setLocation(x, y);
	}

	public String getAppIconName() {
		return app.getIconName();
	}

	public int getMapIconX() {
		return mapIconX;
	}

	public int getMapIconY() {
		return mapIconY;
	}

	public void setMapIconX(int x) {
		mapIconX = x;
	}

	public void setMapIconY(int y) {
		mapIconY = y;
	}

	public int getTimeAlignedViewIconX() {
		return timeAlignedViewIconX;
	}

	public void setTimeAlignedViewIconX(int timeAlignedViewIconX) {
		this.timeAlignedViewIconX = timeAlignedViewIconX;
	}

	public int getTimeAlignedViewIconY() {
		return timeAlignedViewIconY;
	}

	public void setTimeAlignedViewIconY(int timeAlignedViewIconY) {
		this.timeAlignedViewIconY = timeAlignedViewIconY;
	}

	public int getSharedDisplayIconX() {
		return sharedDisplayIconX;
	}

	public void setSharedDisplayIconX(int sharedDisplayIconX) {
		this.sharedDisplayIconX = sharedDisplayIconX;
	}

	public int getSharedDisplayIconY() {
		return sharedDisplayIconY;
	}

	public void setSharedDisplayIconY(int sharedDisplayIconY) {
		this.sharedDisplayIconY = sharedDisplayIconY;
	}

	public Point getConnectingPoint() {
		if (owner.isShowingShowShares()) {
			return new Point(mapIconX + getMapIcon().getWidth() / 2,
					mapIconY + getMapIcon().getHeight() / 2);
		} else
			return owner.getConnectingPoint();
	}

	public ArrayList<Replication> getReplications() {
		return replications;
	}

	public void setReplicators(ArrayList<Replication> replicators) {
		this.replications = replicators;
	}

	public void addReplication(Replication replication) {
		replications.add(replication);
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
	
	public boolean isShowingRays() {
		return showingRays;
	}

	public void setShowingRays(boolean showingRays) {
		this.showingRays = showingRays;
	}

	public void hide(MapView map) {
		getMapIcon().setVisible(false);
		map.repaint();
	}

	public void show(MapView map) {
		getMapIcon().setVisible(true);
		map.repaint();
	}

	public boolean isItAGoodPoint(Point offeredPosition, Point playerPosition) {
		return true;
	}

	public boolean isGoodUpper() {
		return true;
	}

	public long getTotalDuration() {
		return totalDuration;
	}	

	public void highlight() {
		this.mapIcon.highlight();
		showShareBox.highlight();
		this.sharedDisplayIcon.highlight();
	}
	
	public void undoHighlight() {
		this.mapIcon.undoHighlight();
		showShareBox.undoHighlight();
		this.sharedDisplayIcon.undoHighlight();
	}

	public ArrayList<Replication> getReplicationsInRange(Time startTime, Time endTime,
			ArrayList<String> hiddenApps, boolean isShowHidden,
			boolean isShareHidden, boolean isReplicatedHidden,
			boolean isSharedDisplayHidden) {
		
		ArrayList<UserActivity> allReplications = new ArrayList<UserActivity>();
		allReplications.addAll(replications);
		allReplications = Player.sort(allReplications);
	
		ArrayList<UserActivity> tempSelectedActivities = Player.findActivitiesInRange(allReplications, startTime, endTime, hiddenApps, isShowHidden, isShareHidden, isReplicatedHidden, isSharedDisplayHidden);
		
		ArrayList<Replication> selectedActivities = new ArrayList<Replication>();
		for (UserActivity rep : tempSelectedActivities) {
			selectedActivities.add((Replication)rep);
		}
		
		return selectedActivities;
	}

}
