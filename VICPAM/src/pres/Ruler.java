/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Time;
import java.text.DateFormat;

import javax.swing.JButton;


@SuppressWarnings("serial")
public class Ruler extends JButton{
	final static int RULER_Height = 18;
	
	Font f;

	//Scale of the vrtical lines of the ruler
	private int scale = 10;
	
	private int startTime = 0;
	
	private int endTime = 100;
	
	private int timeStep = 10;
	
	private int distanceStep = 10;
	
	public Ruler() {
		f = new Font("sans-serif", Font.PLAIN, 9);
	}

	protected void paintComponent(Graphics g) {
		g.setFont(f);
		g.setColor(Color.BLACK);

		int time = startTime;
		g.drawLine(1, RULER_Height, 1, RULER_Height-8);
		String timeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Time(time));
		g.drawString("" + timeString, 0, 13);
		
		for (int x = distanceStep; x < getWidth(); x += distanceStep) {
			time += timeStep;
			if(time > endTime)
				break;
			g.drawLine(x, RULER_Height, x, RULER_Height - 3);
			if (x % (5*distanceStep) == 0) {
				g.drawLine(x, RULER_Height, x, RULER_Height-8);
				timeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Time(time));
				g.drawString("" + timeString, x - 6, 13);
			}
			
		}
	} 
	
	public void zoomIn() {
		scale = scale * 2;
	}
	
	public void zoomOut() {
		if(scale > 10)
			scale = scale / 2;
	}
	
	public void setStartAndEndTime(int startTime, int endTime, int timeStep, int distanceStep) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeStep = timeStep;
		this.distanceStep = distanceStep;		
	}
}
