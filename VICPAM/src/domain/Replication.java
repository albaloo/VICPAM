/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.sql.Time;

public class Replication extends UserActivity{
	private ShowShare showShare;

	private Player receiver;

	private boolean showing = false;

	public Replication(ShowShare showShare, Player receiver, Time start, Time end, SharedDevice sharedDevice) {
		super(start, end, showShare.getOwner(), showShare.getApp(), "res/copy.gif", sharedDevice);
		this.showShare = showShare;
		this.receiver = receiver;
		receiver.addReplication(this);
		this.setColor(new Color(0x4379ba));

	}
	public Replication(ShowShare showShare, Player receiver, Time start, Time end, boolean showing, String activityIconPath, SharedDevice sharedDevice) {
		super(start, end, showShare.getOwner(), showShare.getApp(), activityIconPath, sharedDevice);
		this.showShare = showShare;
		this.receiver = receiver;
		this.showing = showing;
	}
	public Player getReceiver() {
		return receiver;
	}
	public boolean isShowing() {
		return showing;
	}
	public ShowShare getShowShare() {
		return showShare;
	}	
}
