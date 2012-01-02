/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import domain.Application;
import domain.Player;

@SuppressWarnings("serial")
public class FilterWindow extends JFrame implements ItemListener, ActionListener{
	JPanel topPanel;
	JTabbedPane filterTab;
	
	JPanel users;
	JScrollPane usersWrapper;
	ArrayList<JCheckBox> userCheckBoxes;
	
	JPanel applications;
	JScrollPane appsWrapper;
	ArrayList<JCheckBox> appsCheckBoxes;
	
	JPanel activities;
	JScrollPane activitiesWrapper;
	ArrayList<JCheckBox> activitiesCheckBoxes;
	
	MainWindow mainForm;
	
	public FilterWindow(MainWindow mainForm){
		initComponents();
		this.mainForm = mainForm;
		this.setTitle("Filters");
	}
	
	private void initComponents(){
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);
		
		filterTab = new JTabbedPane();
		
		users = new JPanel();
		users.setLayout(new GridBagLayout());
		usersWrapper = new JScrollPane();
		usersWrapper = new JScrollPane(users);
		usersWrapper.setPreferredSize(new Dimension(40 ,50));
		usersWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		usersWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		filterTab.addTab("Users", usersWrapper);
		userCheckBoxes = new ArrayList<JCheckBox>();
		
		applications = new JPanel();
		applications.setLayout(new GridBagLayout());
		appsWrapper = new JScrollPane();
		appsWrapper = new JScrollPane(applications);
		appsWrapper.setPreferredSize(new Dimension(40 ,50));
		appsWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		appsWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		filterTab.addTab("Applications", appsWrapper);
		appsCheckBoxes = new ArrayList<JCheckBox>();
		
		activities = new JPanel();
		activities.setLayout(new GridBagLayout());
		activitiesWrapper = new JScrollPane();
		activitiesWrapper = new JScrollPane(activities);
		activitiesWrapper.setPreferredSize(new Dimension(40 ,50));
		activitiesWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		activitiesWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		filterTab.addTab("Activities", activitiesWrapper);
		activitiesCheckBoxes = new ArrayList<JCheckBox>();
		
		topPanel.add(filterTab, BorderLayout.CENTER);
	}
	
	public void initTabs(){
		initUsersPage();
		initAppsPage();
		initActivitiesPage();		
	}
	
	private void initUsersPage(){

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridBagLayout());
		JButton all = new JButton("Select All");
		JButton none = new JButton("Select None");
		//Add Players
		//position: gridx, gridy - numberofCells:gridwidth, gridheight - resizewaigth: waightx, y
	
		buttons.add(all, new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		all.addActionListener(this);
		all.setActionCommand("all-users");
		
		buttons.add(none, new GridBagConstraints(1, 0, 1, 1, 0.5, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		none.addActionListener(this);
		none.setActionCommand("none-users");
		
		int gridy = 0;
		users.add(buttons, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		gridy++;		
		
		for (Player player : mainForm.getSession().getPlayers()) {
			JCheckBox box = new JCheckBox();
			String label = "<html> <table cellpadding=0>" +
					"<tr><td><img src=file:res/" + player.getName() + ".gif" +
					"></td><td width=" + 3 + "><td>" + player.getName() + 
					"</td></tr></table></html>";
			box.setText(label);
			users.add(box, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
							0, 0, 0, 0), 0, 0));
			box.setSelected(true);
			box.addItemListener(this);
			userCheckBoxes.add(box);
			gridy++;
		}
		
		//Add shared Display Icon
		JCheckBox box = new JCheckBox();
		String label = "<html> <table cellpadding=0>" +
				"<tr><td><img src=file:res/" + "sharedDisplay.gif" +
				"></td><td width=" + 3 + "><td>" + "Shared Display" + 
				"</td></tr></table></html>";
		box.setText(label);
		users.add(box, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		box.setSelected(true);
		box.addItemListener(this);
		userCheckBoxes.add(box);
		gridy++;
	}
	
	private void initAppsPage(){
		JPanel buttons = new JPanel();
		buttons.setLayout(null);
		JButton all = new JButton("Select All");
		JButton none = new JButton("Select None");

		all.setBounds(2, 2, 135, 45);
		buttons.add(all);
		all.addActionListener(this);
		all.setActionCommand("all-apps");
		
		none.setBounds((getWidth()-20)/2,2, 135, 45);
		buttons.add(none);
		none.addActionListener(this);
		none.setActionCommand("none-apps");
		
		int gridy = 0;
		applications.add(buttons, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		gridy++;		
		for (Application app : mainForm.getSession().getApplications()) {
			JCheckBox box = new JCheckBox();
			String label = "<html> <table cellpadding=0>" +
					"<tr><td><img src=file:res/" + app.getName() + "2.gif" +
					"></td><td width=" + 3 + "><td>" + app.getName() + 
					"</td></tr></table></html>";
			box.setText(label);
			applications.add(box, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
							0, 0, 0, 0), 0, 0));
			box.setSelected(true);
			box.addItemListener(this);
			appsCheckBoxes.add(box);
			gridy++;
		}
		
	}
	
	private void initActivitiesPage(){
		JPanel buttons = new JPanel();
		buttons.setLayout(null);
		JButton all = new JButton("Select All");
		JButton none = new JButton("Select None");
	
		all.setBounds(2, 2, 135, 45);
		buttons.add(all);
		all.addActionListener(this);
		all.setActionCommand("all-activities");
		
		none.setBounds((getWidth()-20)/2,2, 135, 45);
		buttons.add(none);
		none.addActionListener(this);
		none.setActionCommand("none-activities");
		
		int gridy = 0;
		activities.add(buttons, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		gridy++;		
	
		//Show
		JCheckBox boxShow = new JCheckBox();
		String labelShow = "<html> <table cellpadding=0 bgcolor=#43ba71>" +
				"<tr><td><img src=file:res/show.png" +
				"></td><td width=" + 24 + "><td> Show" +  
				"</td><td width=" + 3 + "></tr></table></html>";
		boxShow.setText(labelShow);
		activities.add(boxShow, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		boxShow.setSelected(true);
		boxShow.addItemListener(this);
		activitiesCheckBoxes.add(boxShow);
		gridy++;
		
		//Share
		JCheckBox boxShare = new JCheckBox();
		String labelShare = "<html> <table cellpadding=0 bgcolor=#9843ba>" +
				"<tr><td><img src=file:res/share.png" +
				"></td><td width=" + 24 + "><td> Share" +  
				"</td><td width=" + 3 + "></tr></table></html>";
		boxShare.setText(labelShare);
		activities.add(boxShare, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		boxShare.setSelected(true);
		boxShare.addItemListener(this);
		activitiesCheckBoxes.add(boxShare);
		gridy++;
		
		//Replicated
		JCheckBox boxReplicated = new JCheckBox();
		String labelReplicated = "<html> <table cellpadding=0 bgcolor=#4379ba>" +
				"<tr><td><img src=file:res/copy2.gif" +
				"></td><td width=" + 3 + "><td> Replicated" +  
				"</td><td width=" + 3 + "></tr></table></html>";
		boxReplicated.setText(labelReplicated);
		activities.add(boxReplicated, new GridBagConstraints(0, gridy, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		boxReplicated.setSelected(true);
		boxReplicated.addItemListener(this);
		activitiesCheckBoxes.add(boxReplicated);
	}

	public void actionPerformed(ActionEvent e) {
		if("all-users".equals(e.getActionCommand())){
			for (JCheckBox user : userCheckBoxes) {
				user.setSelected(true);
			}
		}else if("none-users".equals(e.getActionCommand())){
			for (JCheckBox user : userCheckBoxes) {
				user.setSelected(false);
			}
		}else if("all-apps".equals(e.getActionCommand())){
			for (JCheckBox app : appsCheckBoxes) {
				app.setSelected(true);
			}
		}else if("none-apps".equals(e.getActionCommand())){
			for (JCheckBox app : appsCheckBoxes) {
				app.setSelected(false);
			}	
		}else if("all-activities".equals(e.getActionCommand())){
			for (JCheckBox activity : activitiesCheckBoxes) {
				activity.setSelected(true);
			}
		}else if("none-activities".equals(e.getActionCommand())){
			for (JCheckBox activity : activitiesCheckBoxes) {
				activity.setSelected(false);
			}
		}
	}

	public void itemStateChanged(ItemEvent e){
		JCheckBox source = (JCheckBox) e.getItemSelectable();
		setStateUsers(source);
		setStateApps(source);
		setStateActivities(source);
	}
	
	private void setStateUsers(JCheckBox source){
		for (JCheckBox box : userCheckBoxes) {
			if(source == box){
				int endIndex = box.getText().indexOf(".gif");
				int startIndex = box.getText().indexOf("res/") + 4;
				
				String playerName = box.getText().substring(startIndex, endIndex);
				if(!box.isSelected())
					mainForm.hidePlayer(playerName);
				else
					mainForm.showPlayer(playerName);
			}
		}
	}
	
	private void setStateApps(JCheckBox source){
		for (JCheckBox box : appsCheckBoxes) {
			if(source == box){
				int endIndex = box.getText().indexOf("2.gif");
				int startIndex = box.getText().indexOf("res/") + 4;
				
				String appName = box.getText().substring(startIndex, endIndex);
				if(!box.isSelected())
					mainForm.hideApps(appName);
				else
					mainForm.showApps(appName);

			}
		}		
	}

	private void setStateActivities(JCheckBox source){
		for (JCheckBox box : activitiesCheckBoxes) {
			if(source == box){
				int endIndex = box.getText().indexOf("2.gif");
				int startIndex = box.getText().indexOf("res/") + 4;
				
				String activityName = box.getText().substring(startIndex, endIndex);
				if(!box.isSelected())
					mainForm.hideActivities(activityName);
				else
					mainForm.showActivities(activityName);

			}
		}	
	}

}
