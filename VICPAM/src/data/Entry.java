/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package data;
/**
 * @author Roshanak Zilouchian
 */
import java.sql.Time;

public class Entry {

	private String entryID = "";
	private String user = "";
	private String application = "";
	private String activity = "";
	private String fromEntryID = "";
	private String device = "";
	private Time startTime;
	private Time endTime;

	public Entry(){
	}
	public String getEntryID(){
		return entryID;
	}
	public void setEntryID(String entryID){
		this.entryID = entryID;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getFromEntryID(){
		return fromEntryID;
	}
	public void setFromEntryID(String fromEntryID){
		this.fromEntryID = fromEntryID;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}
