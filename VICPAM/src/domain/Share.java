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

public class Share extends ShowShare{

	public Share(Application app, Player owner, Time start, Time end,
			long totalDuration, SharedDevice sharedDevice) {
		super(app, owner, start, end, totalDuration, "res/share.png", sharedDevice);
		this.setColor(new Color(0x9843ba));
	}
	public boolean isItAGoodPoint(Point offeredPosition, Point playerPosition) {
		return (offeredPosition.y > playerPosition.y);
	}
	public boolean isGoodUpper() {
		return false;
	}
}
