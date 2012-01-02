/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.awt.Point;
import java.sql.Time;

public class Show extends ShowShare{

	public Show(Application app, Player owner, Time start, Time end, long totalDuration, SharedDevice sharedDevice) {
		super(app, owner, start, end, totalDuration, "res/show.png", sharedDevice);
		this.setColor(new Color(0x43ba71));
	}
	public boolean isItAGoodPoint(Point offeredPosition, Point playerPosition) {
		return (offeredPosition.y < playerPosition.y);
	}
	public boolean isGoodUpper() {
		return true;
	}	
	public String getBackgroundImagePath() {
		return "res/show-bg1.gif";
	}
}
