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
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import domain.Player;
import domain.Session;

@SuppressWarnings("serial")
public class MapView extends JPanel implements SwingConstants, ActionListener {

	public static int MAX_ICON_SIZE = 60;

	private Session session;

	private int numOfShades = 0;

	private MainWindow mainForm;
	
	private SharedDisplay sharedDisplay;
	
	private JPopupMenu popup;
	
	private JMenuItem popupMenuItem;
	
	private JFrame currentFrame;
	
	private boolean playersHasPainted = false;

	private Color themeColor;

	private ArrayList<MapViewComponent> interactiveMapComponents = new ArrayList<MapViewComponent>();

	private ArrayList<MapViewComponent> hideComponents = new ArrayList<MapViewComponent>();

	private int sharedDisplayXCoordinate = 0;
	
	private int sharedDisplayYCoordinate = 0;
	
	private boolean sharedDisplayIsShown = true;
	
	private int blockHeight = 0;
	 
	private int blockWidth = 0;
	
	public MapView(Session session) {
		this.setLayout(null);
		configPopupMenu();
		themeColor = session.getColor();//new Color(0x5032e6);//(0x0c439c);
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setColor(new Color(0xf8f5f4));
		g2.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15);

		g2.setStroke(new BasicStroke(3f));
		g2.setColor(themeColor);
		g2.drawRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15); 

		g2.dispose();
		
		updateLocations();
	}

	private void updateLocations() {
		if (sharedDisplay != null) {
			sharedDisplay.setLocation(sharedDisplayXCoordinate, sharedDisplayYCoordinate);
			sharedDisplay.setBounds(sharedDisplayXCoordinate, sharedDisplayYCoordinate, blockWidth, blockHeight);//it was -5 for the last two +5 for the first two
		}
		
		if(playersHasPainted) {
			ArrayList<Point> points = findPlaceOfPlayers(session.getNumOfPlayers());
			MapViewComponent currentComponent = null;

			for (int i = 0; i < interactiveMapComponents.size(); i++) {
				currentComponent = interactiveMapComponents.get(i);
				if(currentComponent.getCurrentPlayer().playerIsShown()){
					currentComponent.setLocation(points.get(i).x, points.get(i).y);
					currentComponent.setBounds(points.get(i).x, points.get(i).y, blockWidth, blockHeight);//it was -5 for the last two
					//currentComponent.updateLocation();
				}
			}
		}
		
	}
	
	private void configPopupMenu() {
		popup = new JPopupMenu();
		popupMenuItem = new JMenuItem("Open in New Window");
		popupMenuItem.addActionListener(this);
		popup.add(popupMenuItem);
		
		MouseListener popupListener = new PopupListener();
		this.addMouseListener(popupListener);
	}
	
	public double getDuration() {
		return getStartTime().getTime() - getEndTime().getTime();
	}

	public static int getBlockSize() {
		return MAX_ICON_SIZE;
	}

	void paintPlayers() {
		int numPlayers = session.getNumOfPlayers();

		ArrayList<Point> points = findPlaceOfPlayers(numPlayers);

		paintSharedDisplay();

		Player currentPlayer = null;

		MapViewComponent currentInteractiveMapComponent = null;
		
		for (int i = 0; i < numPlayers; i++) {
			currentPlayer = session.getPlayer(i);
			currentInteractiveMapComponent = new MapViewComponent(this, currentPlayer);
			if(currentPlayer.playerIsShown()){
				currentInteractiveMapComponent.setLocation(points.get(i).x, points.get(i).y);
				currentInteractiveMapComponent.setBounds(points.get(i).x, points.get(i).y, blockWidth, blockHeight);//it was -5 for the last two
				currentInteractiveMapComponent.paintPlayersAndActivities(getStartTime(), getEndTime());
				interactiveMapComponents .add(currentInteractiveMapComponent);
				this.add(currentInteractiveMapComponent);
			}
		}

		if(sharedDisplayIsShown){
			for (int i = 0; i < numPlayers; i++) {
				currentPlayer = session.getPlayer(i);
				if(currentPlayer.playerIsShown()){
					sharedDisplay.paintPlayerActivityInSharedDisplay(currentPlayer, getStartTime(), getEndTime());
				}
			}
		}
		
		playersHasPainted = true;
	}

	private void paintSharedDisplay() {
		sharedDisplay = new SharedDisplay(this);
		sharedDisplay.setLocation(sharedDisplayXCoordinate, sharedDisplayYCoordinate);
		sharedDisplay.setBounds(sharedDisplayXCoordinate, sharedDisplayYCoordinate, blockWidth, blockHeight);
		this.add(sharedDisplay);
		sharedDisplay.findPlacesForPlayersActivities();
	}
	
	private ArrayList<Point> findPlaceOfPlayers(int numPlayers) {
		ArrayList<Point> points = new ArrayList<Point>();	
		int numActualBlocks = numPlayers + 1;//for Shared display
		ArrayList<Integer> sequence = new ArrayList<Integer>();
		int initialA = 2;
		int initialB = 2;
		boolean turnB = true;
		for(int i = 0; i < numActualBlocks; i++){
			sequence.add(new Integer(initialA*initialB));
			if(turnB){
				initialB++;
				turnB = false;
			}else{
				initialA++;
				turnB = true;				
			}
		}
			
		int numDrawnBlocks = numActualBlocks;
		
		for(int i = 0; i < sequence.size(); i++){
			if(numDrawnBlocks <= sequence.get(i)){
				numDrawnBlocks = sequence.get(i);
				break;
			}
		}
		
		int row = new Double(Math.sqrt(numDrawnBlocks)).intValue();
		int column = numDrawnBlocks / row;
		
		int prevX = 5;
		int prevY = 5;
		for(int i = 0; i < row; i++){
			for(int j = 0; j < column; j++){
				if(i == 0 && j == column/2){
					sharedDisplayXCoordinate = prevX;
					sharedDisplayYCoordinate = prevY;
				}else
				points.add(new Point(prevX, prevY));
				
				prevX += getWidth()/column; 
			}
			prevX = 5;
			prevY += getHeight()/row;
		}
		
		blockHeight = getHeight()/row - 7; //-5 for better look
		blockWidth = getWidth()/column - 7;
		return points;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public int getNumOfShades() {
		return numOfShades;
	}

	public void setMainForm(MainWindow form) {
		mainForm = form;

	}

	public MainWindow getMainForm() {
		return mainForm;
	}
	
	public Time getStartTime(){
		return mainForm.getStartTime();
	}
	
	public Time getEndTime(){
		return mainForm.getEndTime();
	}
	public void updateAllComponents() {
		if(sharedDisplayIsShown)
			sharedDisplay.removeAllActivitiesInSharedDisplay();
		for (int i = 0; i < interactiveMapComponents.size(); i++) {
			MapViewComponent currentComp = interactiveMapComponents.get(i);
			currentComp.removeShowShares();
			if(currentComp.getCurrentPlayer().playerIsShown()){
				currentComp.paintShowSharesOfPlayer(getStartTime(), getEndTime());
				if(sharedDisplayIsShown)
					sharedDisplay.paintPlayerActivityInSharedDisplay(currentComp.getCurrentPlayer(), getStartTime(), getEndTime());
			}
		}
		repaint();
	}
	

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			if(e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		if (popupMenuItem.getText().equals("Open in New Window")) {
			popupMenuItem.setText("Back to the Main Window");
			currentFrame = mainForm.getInteractiveMapFrame();
			currentFrame.setSize(getWidth(), getHeight());
			mainForm.remove(this);
			mainForm.pack();
			mainForm.resizeIfNeeded();
			currentFrame.add(this);
			currentFrame.setVisible(true);
		}
		else {
			popupMenuItem.setText("Open in New Window");
			currentFrame.setVisible(false);
			mainForm.setExtendedState(mainForm.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			mainForm.getContentPane().add(this, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
							0, 0, 0, 0), 0, 0));
			mainForm.pack();
			currentFrame.remove(this);
			currentFrame = mainForm;
		}
	}
	
	public JFrame getCurrentFrame() {
		return currentFrame;
	}


	public void setThemeColor(Color color) {
		this.themeColor = color;
		sharedDisplay.setThemeColor(color);
		this.repaint();
	}


	public void hidePlayer(Player player) {
		for (MapViewComponent component : interactiveMapComponents) {
			if(component.getCurrentPlayer().getName().equals(player.getName())){
				this.remove(component);
				interactiveMapComponents.remove(component);
				hideComponents.add(component);
				break;
			}
		}	
		player.setPlayerIsShown(false);
		if(sharedDisplayIsShown)
			sharedDisplay.removeAllActivitiesOfPlayerInSharedDisplay(player);
		repaint();
	}
	
	public void showPlayer(Player player){
		for (MapViewComponent component : hideComponents) {
			if(component.getCurrentPlayer().getName().equals(player.getName())){
				hideComponents.remove(component);
				interactiveMapComponents.add(component);
				
				component.setLocation(component.getX(), component.getY());
				component.setBounds(component.getX(), component.getY(), getWidth()/3 - 5, getHeight()/3 - 5);
				this.add(component);
				break;
			}
		}	

		player.setPlayerIsShown(true);
		if(sharedDisplayIsShown)
			sharedDisplay.paintPlayerActivityInSharedDisplay(player, getStartTime(), getEndTime());
		repaint();
	}

	public void hideSharedDisplay() {
		sharedDisplay.removeAllActivitiesInSharedDisplay();
		sharedDisplayIsShown = false;
		repaint();
	}
	
	public void showSharedDisplay(){
		for (int i = 0; i < interactiveMapComponents.size(); i++) {
			MapViewComponent currentComp = interactiveMapComponents.get(i);
			if(currentComp.getCurrentPlayer().playerIsShown()){
				sharedDisplay.paintPlayerActivityInSharedDisplay(currentComp.getCurrentPlayer(), getStartTime(), getEndTime());
			}
		}
		sharedDisplayIsShown = true;
		repaint();
	}

	public void hideApps(String appName) {
		for (int i = 0; i < interactiveMapComponents.size(); i++) {
			interactiveMapComponents.get(i).hideApps(appName);
		}
		
		if(sharedDisplayIsShown)
			sharedDisplay.hideApp(appName);
		repaint();
	}
	
	public void showApps(String appName) {
		for (int i = 0; i < interactiveMapComponents.size(); i++) {
			interactiveMapComponents.get(i).showApps(appName);
		}
		
		if(sharedDisplayIsShown)
			sharedDisplay.showApp(appName);
		repaint();
	}
	
	public ArrayList<String> getHiddenApps() {
		return mainForm.getHiddenApps();
	}


	public void hideActivity(String activityName) {
		for (int i = 0; i < interactiveMapComponents.size(); i++) {
			interactiveMapComponents.get(i).hideActivity(activityName);
		}

		if(sharedDisplayIsShown)
			sharedDisplay.hideActivity(activityName);
		repaint();
	}

	public void showActivity(String activityName) {
		for (int i = 0; i < interactiveMapComponents.size(); i++) {
			interactiveMapComponents.get(i).showActivity(activityName);
		}

		if(sharedDisplayIsShown)
			sharedDisplay.showActivity(activityName);
		repaint();
	}
}
