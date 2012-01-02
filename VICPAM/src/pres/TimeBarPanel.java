/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;


import domain.Session;

@SuppressWarnings("serial")
public class TimeBarPanel extends JPanel {//implements ActionListener{

	private Session session;
	private Color themeColor;
	private MainWindow mainWindow;
	private final int INCENTS = 5;
	
	public TimeBarPanel(Session session, MainWindow mainWindow) {
		this.session = session;
		this.setLayout(new GridBagLayout());
		this.themeColor = session.getColor();//new Color(0x5032e6);//(0x0c439c);
		this.mainWindow = mainWindow;
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setColor(new Color(0xf8f5f4));
		g2.fillRect(0, 0, getWidth()-3, getHeight()-3);//, 15, 15);

		g2.setColor(themeColor);
		g2.drawRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15); 

		g2.dispose();
		
	}
	
	public void paintTimeBars(int outerWidth){
		TimeBar timeBar = mainWindow.getTimeBar();
		timeBar.setTimeBarRulerSteps(new Double((outerWidth - 2*INCENTS)*1000*60/session.getDuration()).intValue(), 60*1000);
	    //position: gridx, gridy - numberofCells:gridwidth, gridheight - resizewaigth: waightx, y 
	    this.add(timeBar, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		SliderBarMonitor sliderValues = mainWindow.getSliderBadValues();
		this.add(sliderValues, new GridBagConstraints(0, 1, 1, 1,
				1.0, 0.012, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

	}

}
