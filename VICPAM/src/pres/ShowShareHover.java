/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import domain.Replication;
import domain.ShowShare;

public class ShowShareHover implements MouseListener {
	
	private ShowShare showShare;
	
	private MapView map;
	
	public ShowShareHover(MapView map, ShowShare showShare) {
		super();
		this.showShare = showShare;
		this.map = map;
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		
	}

	public void mouseEntered(MouseEvent mouseEvent) {
		showShare.highlight();
		for (Replication rep : showShare.getReplicationsInRange(map.getStartTime(), map.getEndTime(), map.getHiddenApps(), map.getMainForm().isShowHidden(), map.getMainForm().isShareHidden(), map.getMainForm().isReplicatedHidden(), map.getMainForm().isSharedDisplayHidden())) {
			rep.getShowShareBox().halfHighlight();
		}
		showShare.getOwner().highlight();
	}

	public void mouseExited(MouseEvent mouseEvent) {
		showShare.undoHighlight();
		for (Replication rep : showShare.getReplicationsInRange(map.getStartTime(), map.getEndTime(), map.getHiddenApps(), map.getMainForm().isShowHidden(), map.getMainForm().isShareHidden(), map.getMainForm().isReplicatedHidden(), map.getMainForm().isSharedDisplayHidden())) {
			rep.getShowShareBox().undoHalfHighlight();
		}
		showShare.getOwner().undoHighlight();
		map.repaint();
	}

	public void mousePressed(MouseEvent mouseEvent) {
	}

	public void mouseReleased(MouseEvent mouseEvent) {
	}

}
