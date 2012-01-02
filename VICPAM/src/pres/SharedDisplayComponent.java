/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import domain.Player;
import domain.ShowShare;

@SuppressWarnings("serial")
public class SharedDisplayComponent extends JPanel {
	
	Player player;
	ShowShare showShare;
	private Color themeColor;
	private SharedDisplay parentComponent;
	
	public SharedDisplayComponent(ShowShare showshare, Player player, Color themeColor, SharedDisplay parentComponent) {
		this.player = player;
		this.showShare = showshare;
		this.setLayout(null);
		this.themeColor = themeColor;
		this.parentComponent = parentComponent;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setStroke(new BasicStroke(2f));
		g2.setColor(themeColor);
		g2.drawRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15);
	}
	
	public void placeActivityAndPlayer() {
		player.setSharedDisplayIconLocation(3, 3);
		this.add(player.getSharedDisplayIcon());
		
		JIcon showShareIcon = showShare.getSharedDisplayIcon();
		showShareIcon.setLocation(player.getSharedDisplayIcon().getWidth()+10, 0);
		if(showShare.getSharedDisplayIcon().getMouseListeners().length == 2)
			showShare.getSharedDisplayIcon().addMouseListener(new ShowShareHover(parentComponent.getMap(), showShare));
		this.add(showShareIcon);
	}

	public void setThemeColor(Color color) {
		themeColor = color;
		repaint();
	}

}
