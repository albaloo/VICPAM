/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import domain.Player;
import domain.ShowShare;

public class PlayerHover implements MouseListener {

	Player player;

	MapView map;

	public PlayerHover(Player player, MapView map) {
		super();
		this.player = player;
		this.map = map;
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		if(map.getMainForm().isInteractiveAreaHasText())
			map.getMainForm().setInteractionAreaText("");
		
		if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
			if (player.isShowingShowShares()) {
				player.hideShowShares(map);
			} else {
				player.showShowShares(map);
			}
		}
	}

	public void mouseEntered(MouseEvent mouseEvent) {
		for (ShowShare showShare : player.getShowSharesInRange(map.getStartTime(), map.getEndTime(), map.getMainForm().getHiddenApps(), map.getMainForm().isShowHidden(), map.getMainForm().isShareHidden(), map.getMainForm().isReplicatedHidden(), map.getMainForm().isSharedDisplayHidden())) {
			showShare.highlight();
		}
	}

	public void mouseExited(MouseEvent mouseEvent) {
		for (ShowShare showShare : player.getShowSharesInRange(map.getStartTime(), map.getEndTime(), map.getMainForm().getHiddenApps(), map.getMainForm().isShowHidden(), map.getMainForm().isShareHidden(), map.getMainForm().isReplicatedHidden(), map.getMainForm().isSharedDisplayHidden())) {
			showShare.undoHighlight();
		}
	}

	public void mousePressed(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub

	}

}
