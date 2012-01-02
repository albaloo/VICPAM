/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.sql.Time;

import pres.ShowShareBox;


public class UserActivity implements Comparable<UserActivity>{
	protected Time start;
	
	protected Time end;
	
	protected Application app;

	protected Player owner;

	protected String activityIconPath;
	
	protected SharedDevice sharedDevice;
	
	protected ShowShareBox showShareBox;

	protected Color activityColor;
	
	public enum ActivityType{
		SHOW, SHARE, COPY
	}
	
	public enum SharedDevice{
		PERSONAL, SHAREDDISPLAY
	}
	public UserActivity(Time start, Time end, Player owner, Application app, String activityIconPath, SharedDevice sharedDevice) {
		super();
		this.start = start;
		this.end = end;
		this.owner = owner;
		this.app = app;
		this.activityIconPath = activityIconPath;
		this.sharedDevice = sharedDevice;
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
	
	public void setApp(Application app) {
		this.app = app;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Application getApp() {
		return app;
	}
	
	public double getDuration() {
		return end.getTime() - start.getTime();
	}
	
	public SharedDevice getSharedDevice() {
		return sharedDevice;
	}

	public void setSharedDevice(SharedDevice sharedDevice) {
		this.sharedDevice = sharedDevice;
	}

	public void setShowShareBox(ShowShareBox showShareBox) {
		this.showShareBox = showShareBox;
	}
	
	public ShowShareBox getShowShareBox(){
		return showShareBox;
	}

	public String getActivityIconPath() {
		return activityIconPath;
	}

	public int compareTo(UserActivity arg0) {
		if(this.getStart() == arg0.getStart()) {
			if(this.getEnd() == arg0.getEnd())
				return 0;
			else if (this.getEnd().getTime() < arg0.getEnd().getTime())
				return -1;
			else
				return 1;
		}
		else if(this.getStart().getTime() < arg0.getStart().getTime())
			return -1;
		else //if(this.getStart() > ((UserActivity)arg0).getStart())
			return 1;
	}

	public void setColor(Color color){
		this.activityColor = color;
	}
	
	public Color getColor(){
		return activityColor;
	}
}
