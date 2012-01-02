/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import data.InputReader;
import domain.Player;
import domain.Session;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private JMenuBar menuBar1;

	private JMenu menu1;

	private JMenuItem menuItem1;

	private JMenuItem menuItem2;

	private JMenu menu2;

	private JMenuItem menuItemShowReps;
	
	private JMenuItem menuItemMapView;
	
	private JMenuItem menuItemShowRuler;
	
	private JMenu menu3;
	
	private JMenuItem menuItemFiltering;
	
	private JMenuItem menuItemVideo;
	
	private MapView interactiveMap;
	
	private JFrame interactiveMapFrame;

	private TimeBar timeBar;
	
	private TimeAlignedView timeAlignedView;
	
	private JFrame timeAlignedViewFrame;

	private JScrollPane wrapper;

	private SliderBarMonitor sliderBarValues;
	
	private Session session;
	
	private FilterWindow filterWindow;
	
	private ArrayList<String> hiddenPlayers = new ArrayList<String>();
	
	private ArrayList<String> hiddenApps = new ArrayList<String>();
	
	private boolean isShowHidden = false;
	
	private boolean isShareHidden = false;
	
	private boolean isReplicatedHidden = false;
	
	private boolean isSharedDisplayHidden = false;
	
	private boolean interactiveAreahastext = false;

	private MediaWindow mediaWindow;
	
	private TimeBarPanel timeBarPanel;
	
	private Time startTime;
	
	private Time endTime;
	
	public MainWindow() {
		initComponents();
	}

	private void menuItem2ActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void menuItemLoadSession(ActionEvent e) {
		interactiveMapFrame.setSize(interactiveMap.getWidth(), interactiveMap.getHeight());
		this.getContentPane().remove(interactiveMap);
		this.pack();
		interactiveMapFrame.add(interactiveMap);
		getInteractiveMap().paintPlayers();
		interactiveMapFrame.setVisible(false);
		this.setExtendedState(this.getExtendedState()| Frame.MAXIMIZED_BOTH );
		timeAlignedView.setBounds(0, 0, wrapper.getWidth(), wrapper.getHeight());

		getSliderPart().paintSliderPartComponents();
		configSliderBar();
		mediaWindow.showVideo();
		filterWindow.initTabs();
		timeBarPanel.paintTimeBars(this.getWidth());
		repaint();
		
	}

	private void initComponents() {
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem1 = new JMenuItem();
		menuItem2 = new JMenuItem();
		menu2 = new JMenu();
		menuItemShowReps = new JMenuItem();
		menuItemMapView = new JMenuItem();
		menuItemShowRuler = new JMenuItem();
		menu3 = new JMenu();
		//menuItem3 = new JMenuItem();
		menuItemFiltering = new JMenuItem();
		menuItemVideo = new JMenuItem();

		session = new Session();
		interactiveMap = new MapView(session);
		interactiveMapFrame = new JFrame();
		final int initialWidth = MapView.getBlockSize()*10;
		final int initialHeight =MapView.getBlockSize()*10;
		interactiveMapFrame.addComponentListener(new ComponentAdapter() {
		  public void componentResized(ComponentEvent event) {
			  interactiveMapFrame.setSize(
		      Math.max(initialWidth, interactiveMapFrame.getWidth()),
		      Math.max(initialHeight, interactiveMapFrame.getHeight()));
		  }
		});
		interactiveMapFrame.setTitle("Map View");
		interactiveMap.setBackground(new Color(200, 200, 168));
		
		timeAlignedView = new TimeAlignedView(session);
		timeAlignedViewFrame = new JFrame();
		final int initialWidth1 = MapView.getBlockSize()*10;
		final int initialHeight1 =MapView.getBlockSize()*3;
		timeAlignedViewFrame.addComponentListener(new ComponentAdapter() {
		  public void componentResized(ComponentEvent event) {
			  timeAlignedViewFrame.setSize(
		      Math.max(initialWidth1, timeAlignedViewFrame.getWidth()),
		      Math.max(initialHeight1, timeAlignedViewFrame.getHeight()));
		  }
		});
		timeAlignedViewFrame.setTitle("Time Aligned View");
	
		InputReader reader = new InputReader();
		reader.parseInputFile("res/data.xml", this, session);
		
		sliderBarValues = new SliderBarMonitor(this);
	
		timeBar = new TimeBar(session);
		timeBarPanel = new TimeBarPanel(session, this);
		
		interactiveMap.setMainForm(this);
        timeAlignedView.setMainForm(this);
		// ======== this ========
		setTitle("VICPAM");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());

		filterWindow = new FilterWindow(this);
		filterWindow.setSize(300, 500);
		filterWindow.setVisible(false);
		
		mediaWindow = new MediaWindow();
        mediaWindow.setSize(300, 500);
        mediaWindow.setVisible(false);

		  

		// ======== menuBar1 ========
		{

			// ======== menu1 ========
			{
				menu1.setText("File");
				menu1.setMnemonic(KeyEvent.VK_F);
				// ---- menuItem1 ----
				menuItem1.setText("Load Session");
				menuItem1.setMnemonic(KeyEvent.VK_L);
				menuItem1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						menuItemLoadSession(e);
					}
				});
				menu1.add(menuItem1);

				// ---- menuItem2 ----
				menuItem2.setText("Exit");
				menuItem2.setMnemonic(KeyEvent.VK_X);
				menuItem2.setEnabled(false);
				menuItem2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						menuItem2ActionPerformed(e);
					}
				});
				menu1.add(menuItem2);
			}
			menuBar1.add(menu1);

			// ======== menu2 ========
			{
				menu2.setText("View");
				menu2.setMnemonic(KeyEvent.VK_V);

				// ---- menuItem4 ----
				menuItemShowReps.setText("Use Bright Colors");
				menuItemShowReps.setEnabled(true);
				menuItemShowReps.setMnemonic(KeyEvent.VK_B);
				menuItemShowReps.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
							//showAllReps();
						changeTheme();
					}
				});			
				menuItemMapView.setText("Show Map View");
				menuItemMapView.setEnabled(true);
				menuItemMapView.setMnemonic(KeyEvent.VK_M);
				menuItemMapView.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						showMap();
					}
				});
				menu2.add(menuItemMapView);

				menuItemShowRuler.setText("Show Ruler");
				menuItemShowRuler.setEnabled(true);
				menuItemShowRuler.setMnemonic(KeyEvent.VK_R);
				menuItemShowRuler.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						showRuler();
					}
				});
				menu2.add(menuItemShowRuler);

			}
			menuBar1.add(menu2);
			// ======== menu3 ========
			{
				menu3.setText("Window");
				menu3.setMnemonic(KeyEvent.VK_W);

				// ---- menuItem3 ----
				menuItemFiltering.setText("Open Filters Window");
				menuItemFiltering.setMnemonic(KeyEvent.VK_O);
				menuItemFiltering.setEnabled(true);
				menuItemFiltering.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						handleFilteringWindow();
					}
				});
				menu3.add(menuItemFiltering);
				
				menuItemVideo.setText("Open Video Player");
				menuItemVideo.setMnemonic(KeyEvent.VK_P);
				menuItemVideo.setEnabled(true);
				menuItemVideo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						handleVideoPlayer();
					}
				});
				menu3.add(menuItemVideo);
				 
				filterWindow.addWindowListener(new WindowListener(){
					public void windowClosing(WindowEvent e) {
					    handleFilteringWindow();
					}
					public void windowClosed(WindowEvent e) {
				    }
					public void windowOpened(WindowEvent e){	
					}
					public void windowIconified(WindowEvent e) {   
				    }
				    public void windowDeiconified(WindowEvent e) {   
				    }
				    public void windowActivated(WindowEvent e) {
				    }
				    public void windowDeactivated(WindowEvent e) {  
				    }
				});
				
				mediaWindow.addWindowListener(new WindowListener(){
					public void windowClosing(WindowEvent e) { 
						handleVideoPlayer();
					}
					public void windowClosed(WindowEvent e) {
				    }
					public void windowOpened(WindowEvent e){	
					}
					public void windowIconified(WindowEvent e) {   
				    }
				    public void windowDeiconified(WindowEvent e) {   
				    }
				    public void windowActivated(WindowEvent e) {
				    }
				    public void windowDeactivated(WindowEvent e) {  
				    }
				});
			}
			menuBar1.add(menu3);

		
		}
		setJMenuBar(menuBar1);
		
		//position: gridx, gridy - numberofCells:gridwidth, gridheight - resizewaigth: waightx, y 
		this.getContentPane().add(interactiveMap, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		
		wrapper = new JScrollPane(timeAlignedView);
		wrapper.setPreferredSize(new Dimension(40 ,100));
		wrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		wrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	   
		contentPane.add(wrapper, new GridBagConstraints(0, 1, 3, 1,
				1.0, 0.7, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		contentPane.add(timeBarPanel, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.055,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Dimension dimension = toolkit.getScreenSize();
	    
		final int initialWidth2 = new Double(dimension.getWidth()/2).intValue();
		final int initialHeight2 = MapView.getBlockSize()*6;//new Double(3*dimension.getHeight()/4).intValue();;
		this.addComponentListener(new ComponentAdapter() {
		  public void componentResized(ComponentEvent event) {
			  setSize(
		      Math.max(initialWidth2, getWidth()),
		      Math.max(initialHeight2, getHeight()));
			  
		  }
		});

		pack();
		setLocationRelativeTo(getOwner());
		
	
	}
	
	private void configSliderBar() {
		sliderBarValues.setBounds(0,interactiveMap.getHeight()+100,interactiveMap.getWidth(), interactiveMap.getHeight()+150);
		sliderBarValues.setPreferredSize(new Dimension(50, 50));
		sliderBarValues.configureMinMaxSpinners(session.getStart(), session.getEnd());
	}

	private void updateTimeInterval(Time startTime, Time endTime){
		this.startTime = startTime;
		this.endTime = endTime;
		timeAlignedView.updateAllComponents();
		interactiveMap.updateAllComponents();
	}
	
	private void changeTheme(){
		if (!menuItemShowReps.getText().equals("Use Bright Colors")) {
			menuItemShowReps.setText("Use Bright Colors");
			session.changeColorToBlue();
			interactiveMap.setThemeColor(session.getColor());//new Color(0x0c439c));
			timeAlignedView.setThemeColor(session.getColor());//new Color(0x0c439c));
			sliderBarValues.setThemeColor(session.getColor());//new Color(0x0c439c));
			repaint();
		} else {
			menuItemShowReps.setText("Use Dark Colors");
			session.changeColorToRed();
			interactiveMap.setThemeColor(session.getColor());//new Color(0x9e0000));
			timeAlignedView.setThemeColor(session.getColor());//new Color(0x9e0000));
			sliderBarValues.setThemeColor(session.getColor());//new Color(0x9e0000));
			repaint();
		}	
	}

	public void updateSliderBar(Time min, Time max) {
		timeBar.setStartValue(min);
		timeBar.setEndValue(max);
		timeBar.repaint();
		updateTimeInterval(min, max);
	}

	private void showMap(){
		if (menuItemMapView.getText().equals("Show Map View")) {
			menuItemMapView.setText("Hide Map View");
			this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			interactiveMapFrame.setVisible(false);
			this.getContentPane().add(interactiveMap, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
							0, 0, 0, 0), 0, 0));
			this.pack();
			this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			interactiveMapFrame.remove(this);			
			repaint();
		} else {
			menuItemMapView.setText("Show Map View");
			this.setExtendedState(this.getExtendedState()| Frame.MAXIMIZED_BOTH );
			interactiveMapFrame.setSize(interactiveMap.getWidth(), interactiveMap.getHeight());
			this.getContentPane().remove(interactiveMap);
			this.pack();
			this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			interactiveMapFrame.add(interactiveMap);
			interactiveMapFrame.setVisible(false);
			repaint();
		}	
	}

	private void showRuler(){
		if (menuItemShowRuler.getText().equals("Show Ruler")) {
			menuItemShowRuler.setText("Hide Ruler");
			timeAlignedView.showRulers();
			repaint();
		} else {
			menuItemShowRuler.setText("Show Ruler");
			timeAlignedView.hideRulers();
			repaint();
		}	
	}

	private void handleFilteringWindow(){
		if (menuItemFiltering.getText().equals("Close Filters Window")) {
			menuItemFiltering.setText("Open Filters Window");
			filterWindow.setVisible(false);
			repaint();
		} else {
			menuItemFiltering.setText("Close Filters Window");
			filterWindow.setVisible(true);
			repaint();
		}
	}
	
	private void handleVideoPlayer(){
		if (menuItemVideo.getText().equals("Close Video Player")) {
			menuItemVideo.setText("Open Video Player");
			mediaWindow.setVisible(false);
			repaint();
		} else {
			menuItemVideo.setText("Close Video Player");
			mediaWindow.setVisible(true);
			repaint();
		}
		
	}
	
	public MapView getInteractiveMap() {
		return interactiveMap;
	}

	public TimeAlignedView getSliderPart() {
		return timeAlignedView;
	}
	
	public void setInteractionAreaText(String text) {
		if(text == null || text.equals(""))
			interactiveAreahastext = false;
		else
			interactiveAreahastext = true;
	}
	
	public boolean isInteractiveAreaHasText(){
		return interactiveAreahastext;
	}

	public JFrame getInteractiveMapFrame() {
		return interactiveMapFrame;
	}

	public void setInteractiveMapFrame(JFrame interactiveMapFrame) {
		this.interactiveMapFrame = interactiveMapFrame;
	}

	public JFrame getSliderPartFrame() {
		return timeAlignedViewFrame;
	}

	public void setSliderPartFrame(JFrame sliderPartFrame) {
		this.timeAlignedViewFrame = sliderPartFrame;
	}
	
	public JScrollPane getWrapper() {
		return wrapper;
	}

	public boolean isShareHidden(){
		return isShareHidden;
	}

	public boolean isShowHidden(){
		return isShowHidden;
	}

	public boolean isReplicatedHidden(){
		return isReplicatedHidden;
	}

	public void setMediaWindow(MediaWindow mediaWindow) {
		this.mediaWindow = mediaWindow;
	}

	public MediaWindow getMediaWindow(){
		return mediaWindow;
	}
	
	public ArrayList<String> getHiddenApps() {
		return hiddenApps;
	}

	public void addHiddenApp(String appName) {
		this.hiddenApps.add(appName);
	}

	public void removeHiddenApp(String appName){
		for (String app : hiddenApps) {
			if(app.equals(appName)){
				hiddenApps.remove(app);
				return;
			}
		}
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
		interactiveMap.setSession(session);
		timeAlignedView.setSession(session);
		this.startTime = session.getStart();
		this.endTime = new Time(session.getStart().getTime() + session.getSelectedDuration());
	}

	public FilterWindow getFilterWindow() {
		return filterWindow;
	}

	public void setFilterWindow(FilterWindow filterWindow) {
		this.filterWindow = filterWindow;
	}

	public TimeBar getTimeBar(){
		return timeBar;
	}
	
	public SliderBarMonitor getSliderBadValues(){
		return sliderBarValues;
	}
	
	public void resizeIfNeeded() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Dimension dimension = toolkit.getScreenSize();

		if(timeAlignedViewFrame == timeAlignedView.getCurrentFrame() && interactiveMapFrame == interactiveMap.getCurrentFrame()) {
			this.setSize(new Double(dimension.getWidth()).intValue(), 50);
			this.repaint();
		}
		
	}

	public void hidePlayer(String playerName) {
		if(playerName.equals("sharedDisplay")){
			isSharedDisplayHidden = true;
			interactiveMap.hideSharedDisplay();
			timeAlignedView.hideSharedDisplay();
			repaint();
		}else{
			Player player = session.getPlayerByName(playerName);
			interactiveMap.hidePlayer(player);
			timeAlignedView.hidePlayer(player);
			hiddenPlayers.add(playerName);
			repaint();
		}
	}
	
	public void showPlayer(String playerName){
		if(playerName.equals("sharedDisplay")){
			isSharedDisplayHidden = false;
			interactiveMap.showSharedDisplay();
			timeAlignedView.showSharedDisplay();
		}else{
			Player player = session.getPlayerByName(playerName);
			interactiveMap.showPlayer(player);
			timeAlignedView.showPlayer(player);
			hiddenPlayers.remove(playerName);
			repaint();
		}
	}

	public void hideApps(String appName) {
		addHiddenApp(appName);
		interactiveMap.hideApps(appName);
		timeAlignedView.hideApps(appName);
	}
	
	public void showApps(String appName) {
		removeHiddenApp(appName);
		interactiveMap.showApps(appName);
		timeAlignedView.showApps(appName);		
	}

	public void hideActivities(String activityName) {
		if(activityName.equals("share"))
			isShareHidden = true;
		else if(activityName.equals("show"))
			isShowHidden = true;
		else if(activityName.equals("copy"))
			isReplicatedHidden = true;

		interactiveMap.hideActivity(activityName);
		timeAlignedView.hideActivity(activityName);
	}

	public void showActivities(String activityName) {
		if(activityName.equals("share"))
			isShareHidden = false;
		else if(activityName.equals("show"))
			isShowHidden = false;
		else if(activityName.equals("copy"))
			isReplicatedHidden = false;

		interactiveMap.showActivity(activityName);
		timeAlignedView.showActivity(activityName);
	}

	public boolean isSharedDisplayHidden() {
		return isSharedDisplayHidden;
	}
	
	public Time getStartTime(){
		return startTime;
	}
	
	public Time getEndTime(){
		return endTime;
	}
	
	public boolean isPlayerHidden(String playerName){
		for (String player : hiddenPlayers) {
			if(player.equals(playerName))
				return true;
		}	
		return false;
	}
}
