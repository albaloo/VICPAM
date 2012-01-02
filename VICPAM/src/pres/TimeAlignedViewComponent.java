/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import domain.Player;
import domain.Replication;
import domain.Share;
import domain.Show;
import domain.ShowShare;
import domain.UserActivity;
import domain.UserActivity.ActivityType;
import domain.UserActivity.SharedDevice;

@SuppressWarnings("serial")
public class TimeAlignedViewComponent extends JPanel implements Transferable {
	public static int MAX_ICON_SIZE = 40;

	public static int MAX_COMP_SIZE = 20;

	public static int START_TIME_INTERVAL = 50;

	private Time startTime;

	private Time endTime;

	private Player currentPlayer;

	private int startx;

	private int starty;
	
	private Ruler ruler;

	private boolean rulerShowed = false;
	
	private int timeStep;
	
	private int distanceStep;
	
	private int startTimeForRuler;
	
	private int endTimeForRuler;

	private ArrayList<ShowShareBox> showShareBoxes;
	
	private ArrayList<ShowShareBox> hiddenShowShareBoxes = new ArrayList<ShowShareBox>();
	
	private ArrayList<Integer> rowEndTime = new ArrayList<Integer>();
	
	private boolean isSharedDisplayShown = true;
	
	private JScrollPane wrapper;

	private Color themeColor;
	
	private ThemeColors selectedThemeColor;
	
	private TimeAlignedView parentComponent;
	
	public enum ThemeColors{
		RED, BLUE
	}
	// private Dimension size;
	public TimeAlignedViewComponent(Player currentPlayer, int startx, int starty, Time startTime, Time endTime, TimeAlignedView parentComponent) {
		this.currentPlayer = currentPlayer;
		this.startx = startx;
		this.starty = starty;
		this.setLayout(null);
		
		this.startTime = startTime;
		this.endTime = endTime;

		this.addMouseListener(new DraggableMouseListener());
		this.setTransferHandler(new DragAndDropTransferableHandler());
		
		this.parentComponent = parentComponent;
		
		this.themeColor = parentComponent.getSession().getColor();//new Color(0x0c439c);
		selectedThemeColor = ThemeColors.BLUE;
	}

	public double getDuration() {
		return startTime.getTime() - endTime.getTime();
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setColor(new Color(0xf8f5f4));
		g2.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15);

		g2.setStroke(new BasicStroke(3f));
		g2.setColor(themeColor);

		if(rulerShowed)
			updateRuler();
		g2.dispose();
	}

	void paintPlayersActivities() {

		currentPlayer.setTimeAlignedViewLocation(startx+3, starty+3);
		this.add(currentPlayer.getTimeAlignedViewIcon());

		calculateTimeAndDistanceStep();
		paintShowSharesOfPlayer();
	}
	
	
	private void paintShowSharesOfPlayer() {
		rowEndTime = new ArrayList<Integer>();
		showShareBoxes = new ArrayList<ShowShareBox>();
		ArrayList<UserActivity> allActivities = currentPlayer.getActivitiesInRange(startTime, endTime, parentComponent.getHiddenApps(), parentComponent.getMainForm().isShowHidden(), parentComponent.getMainForm().isShareHidden(), parentComponent.getMainForm().isReplicatedHidden(), parentComponent.getMainForm().isSharedDisplayHidden());

		int numComponents = allActivities.size();

		int boxStartY = 0;
		rowEndTime.add(new Integer(0));
		
		double startOfInterval = startTime.getTime();//If some part of an interval is not inside the selected time interval
		double endOfInterval = endTime.getTime();

		UserActivity currentMinActivity;

		for (int i = 0; i < numComponents; i++) {
			currentMinActivity = allActivities.get(i);
			String activityIconPath = "";
			
			activityIconPath = currentMinActivity.getActivityIconPath();
			ShowShareBox showShareBox;
			
			if(currentMinActivity instanceof Replication) {
				showShareBox = new ShowShareBox(currentMinActivity.getApp().getIconName(), activityIconPath, ActivityType.COPY, selectedThemeColor, currentMinActivity.getSharedDevice());
				showShareBox.setToolTipText("<html><b>Start Time:</b> " + currentMinActivity.getStart() + " s<br>" + "<b>End Time:</b> "
						+ currentMinActivity.getEnd() + " s<br>" + "<b>Application Name:</b> " + currentMinActivity.getApp().getName() + "<br>" + "<b>Replicated From:</b> " + currentMinActivity.getOwner().getName() +"</html>");
				showShareBox.addMouseListener(new ReplicationHover((Replication)currentMinActivity, showShareBox));
			}
			else {
				if(currentMinActivity instanceof Show)
					showShareBox = new ShowShareBox(currentMinActivity.getApp().getIconName(), activityIconPath, ActivityType.SHOW, selectedThemeColor, currentMinActivity.getSharedDevice());
				else
					showShareBox = new ShowShareBox(currentMinActivity.getApp().getIconName(), activityIconPath, ActivityType.SHARE, selectedThemeColor, currentMinActivity.getSharedDevice());

				showShareBox.addMouseListener(new ShowShareBoxHover((ShowShare) currentMinActivity, showShareBox));
				if(currentMinActivity.getSharedDevice() == SharedDevice.SHAREDDISPLAY)
					showShareBox.setToolTipText("<html><b>Start Time:</b> " + currentMinActivity.getStart() + " s<br>" + "<b>End Time:</b> "
						+ currentMinActivity.getEnd() + " s<br>" +"<b>Application Name:</b> " + currentMinActivity.getApp().getName() + "<br>" + "<b>Shared on:</b> Shared Display" + "<br>" + "</html>");
				else
					showShareBox.setToolTipText("<html><b>Start Time:</b> " + currentMinActivity.getStart() + " s<br>" + "<b>End Time:</b> "
							+ currentMinActivity.getEnd() + " s<br>" +"<b>Application Name:</b> " + currentMinActivity.getApp().getName() + "<br>" + "<b>Shared on:</b> Personal Display" + "<br>" + "</html>");
			}

			currentMinActivity.setShowShareBox(showShareBox);
			startOfInterval = currentMinActivity.getStart().getTime(); 
			if( startOfInterval < startTimeForRuler)
				startOfInterval = startTimeForRuler;
				
			endOfInterval = currentMinActivity.getEnd().getTime();
			if(endOfInterval > endTimeForRuler)
				endOfInterval = endTimeForRuler;
			
			boxStartY = findBoxStartY((int)currentMinActivity.getStart().getTime(), (int)endOfInterval);
					
			int showShareBoxWidth = (((int)endOfInterval - (int)startOfInterval) / timeStep)*distanceStep;
			
			if (showShareBoxWidth == 0)
				showShareBoxWidth = distanceStep / 2;
			
			int showShareBoxLocation= START_TIME_INTERVAL + (((int)startOfInterval - startTimeForRuler)/timeStep)*distanceStep;
			if(showShareBoxLocation == 0)
				showShareBoxLocation = START_TIME_INTERVAL + startTimeForRuler + distanceStep/2;
			
			showShareBox.setLocation(showShareBoxLocation, boxStartY);
			showShareBox.setBounds(showShareBoxLocation, boxStartY,
					showShareBoxWidth,
					MAX_COMP_SIZE);
			this.add(showShareBox);
			showShareBoxes.add(showShareBox);
			
			if(boxStartY > getHeight() - 20) {
				setPreferredSize(new Dimension(getWidth(), 2*getHeight()));
			}

			if(currentMinActivity.getOwner().getName().equals("Susan") && parentComponent.getMainForm().isShowHidden() && parentComponent.getMainForm().isReplicatedHidden() && parentComponent.getMainForm().isSharedDisplayHidden()){
				System.out.println("currentMinActivit: " + currentMinActivity);
			}
		}
		
		repaint();
	}

	private int findBoxStartY(int start, int end){
		int i = 0;
		for (i = 0; i < rowEndTime.size(); i++) {
			if(start > rowEndTime.get(i)){
				rowEndTime.remove(i);
				rowEndTime.add(i, end);
				return MAX_COMP_SIZE*i;
			}
				
		}
		rowEndTime.add(end);
		return MAX_COMP_SIZE*i;
	}
	
	public void showRuler() {	
		ruler = new Ruler();
		ruler.setLocation(START_TIME_INTERVAL, getHeight()-20);
		ruler.setBounds(START_TIME_INTERVAL, getHeight()-20, getWidth()-70, getHeight());
		
		ruler.setStartAndEndTime(startTimeForRuler, endTimeForRuler, timeStep, distanceStep);
		this.add(ruler);
		rulerShowed = true;
	}
	
	private void calculateTimeAndDistanceStep() {
		startTimeForRuler = (new Double(startTime.getTime()).intValue()/10)*10;// - insent;
		endTimeForRuler = (new Double(endTime.getTime()).intValue()/10)*10 + 10;// + insent;
		
		timeStep = (endTimeForRuler - startTimeForRuler) / 40;
		if(timeStep > 2 && timeStep < 5)
			timeStep = 5;
		
		timeStep = (timeStep / 5) * 5;
		
		if(timeStep == 0)
			timeStep = 1;
		else
			timeStep = timeStep + 5;
		
		distanceStep = ((getWidth() - 70 - START_TIME_INTERVAL) * timeStep) / (endTimeForRuler  - startTimeForRuler);
		
	}
	
	public Object getTransferData(DataFlavor flavor) {

		DataFlavor thisFlavor = null;

		try {
			thisFlavor = TimeAlignedView.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}

		// For now, assume wants this class... see loadDnD
		if (thisFlavor != null && flavor.equals(thisFlavor)) {
			return this;
		}

		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {

		DataFlavor[] flavors = { null };

		try {
			flavors[0] = TimeAlignedView.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}

		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {

		DataFlavor[] flavors = { null };
		try {
			flavors[0] = TimeAlignedView.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return false;
		}

		for (DataFlavor f : flavors) {
			if (f.equals(flavor)) {
				return true;
			}
		}

		return false;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public void updateTimeInterval(Time startTime, Time endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		removeShowSharesofPlayer();
		calculateTimeAndDistanceStep();
		paintShowSharesOfPlayer();
		if(rulerShowed)
			updateRuler();
	}
	
	private void updateRuler() {
		this.remove(ruler);
		showRuler();
	}
	
	public void hideRuler(){
		this.remove(ruler);
		rulerShowed = false;
	}
	
	private void removeShowSharesofPlayer() {
		for (ShowShareBox currentBox : showShareBoxes) {
			this.remove(currentBox);
		}
		showShareBoxes.clear();
	}

	public class ReplicationHover implements MouseListener{
		private Replication rep;
		private ShowShareBox showShareBox;
		
		public ReplicationHover(Replication rep, ShowShareBox showSharebox) {
			this.rep = rep;
			this.showShareBox = showSharebox;
		}
		
		public void mouseClicked(MouseEvent mouseEvent) {
		}

		public void mouseEntered(MouseEvent mouseEvent) {
			rep.getOwner().highlight();
			//TODO: decide about highlighting showshare in map area.
			if(!isHiddenApp(rep.getShowShareBox().getAppName()) && isSharedDisplayShown && !isHiddenActivity(rep.getShowShareBox().getActivityType()) && !parentComponent.getMainForm().isPlayerHidden(rep.getShowShare().getOwner().getName()))
				rep.getShowShare().highlight();
			if(rep.getShowShare().getSharedDevice() == SharedDevice.SHAREDDISPLAY && isSharedDisplayShown)
				rep.getOwner().highlightSharedDisplay();
			showShareBox.highlight();
		}

		public void mouseExited(MouseEvent mouseEvent) {
			rep.getOwner().undoHighlight();
			if(!isHiddenApp(rep.getShowShareBox().getAppName()) && isSharedDisplayShown && !isHiddenActivity(rep.getShowShareBox().getActivityType()) && !parentComponent.getMainForm().isPlayerHidden(rep.getShowShare().getOwner().getName()))
				rep.getShowShare().undoHighlight();
			if(rep.getShowShare().getSharedDevice() == SharedDevice.SHAREDDISPLAY)
				rep.getOwner().undoHighlightSharedDisplay();
			showShareBox.undoHighlight();
			parentComponent.getMainForm().getInteractiveMap().repaint();
		}

		public void mousePressed(MouseEvent mouseEvent) {
		}

		public void mouseReleased(MouseEvent mouseEvent) {
		}
	
	}

	public class ShowShareBoxHover implements MouseListener{
		private ShowShare showShare;
		private ShowShareBox showShareBox;
		
		public ShowShareBoxHover(ShowShare showShare, ShowShareBox showShareBox) {
			this.showShare = showShare;
			this.showShareBox = showShareBox;
		}
		
		public void mouseClicked(MouseEvent mouseEvent) {
		}

		public void mouseEntered(MouseEvent mouseEvent) {
			showShare.getOwner().highlight();
			showShare.getMapIcon().highlight();
			showShare.getSharedDisplayIcon().highlight();
			if(showShare.getSharedDevice() == SharedDevice.SHAREDDISPLAY)
				showShare.getOwner().highlightSharedDisplay();
			for (Replication rep : showShare.getReplicationsInRange(startTime, endTime, parentComponent.getHiddenApps(), parentComponent.getMainForm().isShowHidden(), parentComponent.getMainForm().isShareHidden(), parentComponent.getMainForm().isReplicatedHidden(), parentComponent.getMainForm().isSharedDisplayHidden())) {
				ActivityType activityType = ActivityType.COPY;
				if(rep.getShowShare() instanceof Share)
					activityType = ActivityType.SHARE;
				else if(rep.getShowShare() instanceof Show)
					activityType = ActivityType.SHOW;
					
				if(rep.getShowShareBox() != null && !isHiddenApp(rep.getShowShare().getApp().getName()) && isSharedDisplayShown && !isHiddenActivity(activityType) && !parentComponent.getMainForm().isPlayerHidden(rep.getShowShare().getOwner().getName()))
					rep.getShowShareBox().halfHighlight();
			}
			showShareBox.highlight();
		}

		public void mouseExited(MouseEvent mouseEvent) {
			showShare.getOwner().undoHighlight();
			//showShare.undoHighlight();
			showShare.getMapIcon().undoHighlight();
			showShare.getSharedDisplayIcon().undoHighlight();
			if(showShare.getSharedDevice() == SharedDevice.SHAREDDISPLAY)
				showShare.getOwner().undoHighlightSharedDisplay();
			for (Replication rep : showShare.getReplicationsInRange(startTime, endTime, parentComponent.getHiddenApps(), parentComponent.getMainForm().isShowHidden(), parentComponent.getMainForm().isShareHidden(), parentComponent.getMainForm().isReplicatedHidden(), parentComponent.getMainForm().isSharedDisplayHidden())) {
				ActivityType activityType = ActivityType.COPY;
				if(rep.getShowShare() instanceof Share)
					activityType = ActivityType.SHARE;
				else if(rep.getShowShare() instanceof Show)
					activityType = ActivityType.SHOW;
					
				if(rep.getShowShareBox() != null && !isHiddenApp(rep.getShowShare().getApp().getName()) && isSharedDisplayShown && !isHiddenActivity(activityType) && !parentComponent.getMainForm().isPlayerHidden(rep.getShowShare().getOwner().getName()))
					rep.getShowShareBox().undoHalfHighlight();
			}
			showShareBox.undoHighlight();
			parentComponent.getMainForm().getInteractiveMap().repaint();
		}

		public void mousePressed(MouseEvent mouseEvent) {
		}

		public void mouseReleased(MouseEvent mouseEvent) {
		}
	
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public JScrollPane getWrapper() {
		return wrapper;
	}
	
	public void setWrapper(JScrollPane wrapper) {
		this.wrapper = wrapper;
	}

	public void setThemeColor(Color color) {
		this.themeColor = color;
		if(selectedThemeColor == ThemeColors.BLUE)
			selectedThemeColor = ThemeColors.RED;
		else
			selectedThemeColor = ThemeColors.BLUE;
		
		for (ShowShareBox box : showShareBoxes) {
			box.setThemeColor(selectedThemeColor);
		}
		this.repaint();
	}

	private void relayout(){
		rowEndTime = new ArrayList<Integer>();
		int boxStartY = 0;
		rowEndTime.add(new Integer(0));

		sortShowShareBoxes();
		for (int i = 0; i < showShareBoxes.size(); i++) {
			boxStartY = findBoxStartY(showShareBoxes.get(i).getBounds().x, showShareBoxes.get(i).getBounds().width + showShareBoxes.get(i).getBounds().x);
			showShareBoxes.get(i).setLocation(showShareBoxes.get(i).getX(), boxStartY);
						
		}
	}
	public void hideSharedDisplay() {
		ArrayList<ShowShareBox> tobeDeleted = new ArrayList<ShowShareBox>();
		
		for (ShowShareBox box : showShareBoxes) {
			if(box.getDevice() == SharedDevice.SHAREDDISPLAY){
				this.remove(box);
				tobeDeleted.add(box);
				hiddenShowShareBoxes.add(box);
			}
		}
		
		for (ShowShareBox showShareBox : tobeDeleted) {
			showShareBoxes.remove(showShareBox);
		}
		relayout();
		repaint();
	}

	//TODO: make it right.
	public void showSharedDisplay() {
		ArrayList<ShowShareBox> tobeDeleted = new ArrayList<ShowShareBox>();

		for (ShowShareBox box : hiddenShowShareBoxes) {
			if(box.getDevice() == SharedDevice.SHAREDDISPLAY &&  !isHiddenActivity(box.getActivityType()) && !isHiddenApp(box.getAppName())){
				this.add(box);
				showShareBoxes.add(box);
				tobeDeleted.add(box);
			}
		} 
		
		for (ShowShareBox box : tobeDeleted) {
			hiddenShowShareBoxes.remove(box);
		}
		
		relayout();
		repaint();
	}

	public void hideApp(String appName) {
		ArrayList<ShowShareBox> tobeDeleted = new ArrayList<ShowShareBox>();
		
		for (ShowShareBox box : showShareBoxes) {
			if(box.getAppName().equals(appName)){
				this.remove(box);
				tobeDeleted.add(box);
				hiddenShowShareBoxes.add(box);
			}
		}
		
		for (ShowShareBox showShareBox : tobeDeleted) {
			showShareBoxes.remove(showShareBox);
		}
		
		relayout();
		repaint();
	}
	
	public void showApp(String appName) {
		ArrayList<ShowShareBox> tobeDeleted = new ArrayList<ShowShareBox>();

		for (ShowShareBox box : hiddenShowShareBoxes) {
			if(box.getAppName().equals(appName) && !isHiddenActivity(box.getActivityType()) && isSharedDisplayShown){
				this.add(box);
				showShareBoxes.add(box);
				tobeDeleted.add(box);
			}
		} 
		
		for (ShowShareBox box : tobeDeleted) {
			hiddenShowShareBoxes.remove(box);
		}
		
		relayout();
		repaint();
	}

	public void sortShowShareBoxes() {
		ArrayList<ShowShareBox> allActivities = new ArrayList<ShowShareBox>();
		allActivities.addAll(showShareBoxes);
		
		ShowShareBox[] activitiesArray = allActivities.toArray(new ShowShareBox[allActivities.size()]);
		Arrays.sort(activitiesArray);
		showShareBoxes = new ArrayList<ShowShareBox>(Arrays.asList(activitiesArray));
	}

	public void hideActivity(String activityName) {
		ArrayList<ShowShareBox> tobeDeleted = new ArrayList<ShowShareBox>();
		
		for (ShowShareBox box : showShareBoxes) {
			if(box.getActivityType().toString().equals(activityName.toUpperCase())){
				this.remove(box);
				tobeDeleted.add(box);
				hiddenShowShareBoxes.add(box);
			}
		}
		
		for (ShowShareBox showShareBox : tobeDeleted) {
			showShareBoxes.remove(showShareBox);
		}
		
		relayout();
		repaint();		
	}

	public void showActivity(String activityName) {
		ArrayList<ShowShareBox> tobeDeleted = new ArrayList<ShowShareBox>();

		for (ShowShareBox box : hiddenShowShareBoxes) {
			if(box.getActivityType().toString().equals(activityName.toUpperCase()) && !isHiddenApp(box.getAppName()) && isSharedDisplayShown){
				this.add(box);
				showShareBoxes.add(box);
				tobeDeleted.add(box);
			}
		} 
		
		for (ShowShareBox box : tobeDeleted) {
			hiddenShowShareBoxes.remove(box);
		}
		
		relayout();
		repaint();
	}

	private boolean isHiddenApp(String appName){
		for (String name : parentComponent.getHiddenApps()) {
			if(name.equals(appName))
				return true;
		}
		
		return false;
	}
	
	private boolean isHiddenActivity(ActivityType activityType){
		if(activityType == ActivityType.COPY && parentComponent.getMainForm().isReplicatedHidden())
			return true;
		else if(activityType == ActivityType.SHARE && parentComponent.getMainForm().isShareHidden())
			return true;
		else if(activityType == ActivityType.SHOW && parentComponent.getMainForm().isShowHidden())
			return true;
		else
			return false;
	}
	
}
