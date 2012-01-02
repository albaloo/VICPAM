/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import domain.Player;
import domain.Replication;
import domain.Session;

@SuppressWarnings("serial")

public class TimeAlignedView extends JPanel implements SwingConstants, ActionListener {
		public static int MAX_ICON_SIZE = 60;
		
		public static int PANEL_INSENTS = 2;

		private Session session;

		private MainWindow mainForm;
		
		private static DataFlavor dragAndDropPanelDataFlavor = null;
		
		ArrayList<TimeAlignedViewComponent> allComponents = new ArrayList<TimeAlignedViewComponent>();

		ArrayList<TimeAlignedViewComponent> hideComponents = new ArrayList<TimeAlignedViewComponent>();
		
		private JPopupMenu popup;
		
		private JMenuItem popupMenuItem;
		
		private JFrame currentFrame;
		
		private MouseListener popupListener;

		//private Color themeColor;
		
		public TimeAlignedView(Session session) {
			this.session = session;
			this.setLayout(new GridBagLayout());
			this.setTransferHandler(new DragAndDropTransferableHandler());
			this.setDropTarget(new DropTarget(this, new PanelDropTargetListener(this)));
			configPopupMenu();
		}

		private void configPopupMenu() {
			popup = new JPopupMenu();
			popupMenuItem = new JMenuItem("Open in New Window");
			popupMenuItem.addActionListener(this);
			popup.add(popupMenuItem);
			
			popupListener = new PopupListener();
			this.addMouseListener(popupListener);
		}
		
		public double getDuration() {
			return getStartTime().getTime() - getEndTime().getTime();
		}
		
		public ArrayList<TimeAlignedViewComponent> getAllComponents(){			
			return allComponents;
		}
		
		void paintSliderPartComponents() {		
			int numPlayers = session.getNumOfPlayers();
			
			Player currentPlayer = null;
			int prevY = 0;

			for (int i = 0; i < numPlayers; i++) {
				currentPlayer = session.getPlayer(i);//.clone();//halfClone();
				TimeAlignedViewComponent currentComponent = new TimeAlignedViewComponent(currentPlayer, 0, 0, getStartTime(), getEndTime(), this);
				JScrollPane wrapper = new JScrollPane();
				wrapper.setViewportView(currentComponent);
				wrapper.setPreferredSize(new Dimension(40 ,5));//(getHeight()-(numPlayers*PANEL_INSENTS*2))/numPlayers - 6));
				wrapper.setBounds(0,prevY,this.getWidth()-PANEL_INSENTS,MAX_ICON_SIZE+10);//(getHeight()-(numPlayers*PANEL_INSENTS*2))/numPlayers - 6);
				wrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				wrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				wrapper.getVerticalScrollBar().setUnitIncrement(16);
				wrapper.addMouseListener(popupListener);
				currentComponent.setLocation(0, prevY);
				currentComponent.addMouseListener(popupListener);
				currentComponent.setBounds(0,prevY,this.getWidth()-PANEL_INSENTS,MAX_ICON_SIZE+10);//(getHeight()-(numPlayers*PANEL_INSENTS*2))/numPlayers);
				this.add(wrapper, new GridBagConstraints(0, prevY, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
								PANEL_INSENTS, PANEL_INSENTS, PANEL_INSENTS, PANEL_INSENTS), 0, 0));
				currentComponent.paintPlayersActivities();
				currentComponent.setWrapper(wrapper);
				allComponents.add(currentComponent);
				prevY += MAX_ICON_SIZE+10;
			}
			
			setOwnerOfReplications();
		}
		
		public void setOwnerOfReplications(){
			for (TimeAlignedViewComponent comp : allComponents) {
				for (Replication rep : comp.getCurrentPlayer().getReplications()) {
					for(int i = 0; i < allComponents.size(); i++){
						if(rep.getOwner().getName().equals(allComponents.get(i).getCurrentPlayer().getName())){
							rep.setOwner(allComponents.get(i).getCurrentPlayer());
						}	
					}
				}
			}
		}
		
		public void updateAllComponents() {
			for (TimeAlignedViewComponent component : allComponents) {
				component.updateTimeInterval(getStartTime(), getEndTime());
			}
		}

		public Session getSession() {
			return session;
		}

		public void setSession(Session session) {
			this.session = session;
		}

		public void setMainForm(MainWindow form) {
			mainForm = form;

		}

		public MainWindow getMainForm() {
			return mainForm;
		}
		
		protected void relayout() {
	        // Clear out all previously added items
	        this.removeAll();	        
	        int prevY = 0;
	        // Add the panels, if any
	        for (TimeAlignedViewComponent currentComponent : allComponents) {
	        	JScrollPane wrapper = new JScrollPane();
				wrapper.setViewportView(currentComponent);
				wrapper.setPreferredSize(new Dimension(40 ,5));//(getHeight()-(numPlayers*PANEL_INSENTS*2))/numPlayers - 6));
				wrapper.setBounds(0,prevY,this.getWidth()-PANEL_INSENTS,MAX_ICON_SIZE+10);//(getHeight()-(numPlayers*PANEL_INSENTS*2))/numPlayers - 6);
				wrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				wrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				currentComponent.setLocation(0, prevY);
				currentComponent.setBounds(0,prevY,this.getWidth()-PANEL_INSENTS,MAX_ICON_SIZE+10);//(getHeight()-(numPlayers*PANEL_INSENTS*2))/numPlayers);
				this.add(wrapper, new GridBagConstraints(0, prevY, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
								PANEL_INSENTS, PANEL_INSENTS, PANEL_INSENTS, PANEL_INSENTS), 0, 0));
				currentComponent.setWrapper(wrapper);
	        	prevY += MAX_ICON_SIZE+10;
			}

	        this.validate();
	        this.repaint();
	    }

		public static DataFlavor getDragAndDropPanelDataFlavor() {
			 if (dragAndDropPanelDataFlavor == null) {
		            try {
						dragAndDropPanelDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=pres.TimeAlignedViewComponent");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }

		        return dragAndDropPanelDataFlavor;
		}

		public Time getEndTime() {
			return mainForm.getEndTime();
		}

		public Time getStartTime() {
			return mainForm.getStartTime();
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
				currentFrame = mainForm.getSliderPartFrame();
				currentFrame.setSize(getWidth(), getHeight());
				mainForm.remove(mainForm.getWrapper());
				mainForm.pack();
				mainForm.resizeIfNeeded();
				currentFrame.add(mainForm.getWrapper());
				currentFrame.setVisible(true);
			}
			else {
				popupMenuItem.setText("Open in New Window");
				currentFrame.setVisible(false);
				mainForm.getContentPane().add(mainForm.getWrapper(), new GridBagConstraints(0, 1, 3, 1,
						1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				mainForm.setExtendedState(mainForm.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				mainForm.pack();
				currentFrame.remove(mainForm.getWrapper());
				currentFrame = mainForm;
			}
		}

		public JFrame getCurrentFrame() {
			return currentFrame;
		}

		public void setThemeColor(Color color) {
			for (TimeAlignedViewComponent component : allComponents) {
				component.setThemeColor(color);
			}
			this.repaint();
		}

		public void showRulers() {
			for (TimeAlignedViewComponent component : allComponents) {
				component.showRuler();
			}			
		}
		
		public void hideRulers() {
			for (TimeAlignedViewComponent component : allComponents) {
				component.hideRuler();
			}			
		}

		public void hidePlayer(Player player) {
			for (TimeAlignedViewComponent component : allComponents) {
				if(component.getCurrentPlayer().getName().equals(player.getName())){
					this.remove(component.getWrapper());
					allComponents.remove(component);
					hideComponents.add(component);
					break;
				}
			}	
			relayout();
		}
		
		public void showPlayer(Player player) {
			for (TimeAlignedViewComponent component : hideComponents) {
				if(component.getCurrentPlayer().getName().equals(player.getName())){
					hideComponents.remove(component);
					allComponents.add(component);
					break;
				}
			}	
			relayout();
		}

		public void hideSharedDisplay() {
			for (TimeAlignedViewComponent component : allComponents) {
					component.hideSharedDisplay();
			}
			repaint();
		}

		public void showSharedDisplay() {
			for (TimeAlignedViewComponent component : allComponents) {
				component.showSharedDisplay();
			}
			repaint();
		}

		public void hideApps(String appName) {
			for (TimeAlignedViewComponent component : allComponents) {
					component.hideApp(appName);
			}
			repaint();
		}

		public void showApps(String appName) {
			for (TimeAlignedViewComponent component : allComponents) {
				component.showApp(appName);
			}
			repaint();
		}

		public void hideActivity(String activityName) {
			for (TimeAlignedViewComponent component : allComponents) {
				component.hideActivity(activityName);
			}
			repaint();
		}

		public void showActivity(String activityName) {
			for (TimeAlignedViewComponent component : allComponents) {
				component.showActivity(activityName);
			}
			repaint();
		}

		public ArrayList<String> getHiddenApps() {
			return mainForm.getHiddenApps();
		}

}
