/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;


import javax.swing.JSlider;

import domain.Indicator;
import domain.Session;



@SuppressWarnings("serial")
public class TimeBar extends JSlider{

	private Time startValue;
	private Time endValue;
	
	private Time minValue;
	private Time maxValue;

	private final static int SELECTOR_HEIGHT = 45;//60
	private final static int RULER_HEIGHT = 8;
	private final static int INDICATOR_WIDTH = 10;
	
	private final static int KNOB_WIDTH = 10;
	private final static int KNOB_HEIGHT = 20;
	//private Rectangle selectedRegion;
	
	int rulerTimeStep = 60*1000;
	int rulerDistanceStep = 10;
	
	private Session session;
	
	private ArrayList<Indicator> indicators;
	private final int INCENTS = 10;
	
	private final int RULER_INCENTS = 15;

	public TimeBar(Session session){
		this.session = session;
		this.startValue = session.getStart();
		Time time = new Time(session.getStart().getTime() + session.getSelectedDuration());
		this.endValue = time;
		this.minValue = session.getStart();
		this.maxValue = session.getEnd();
		this.indicators = session.getIndicators();
	}

	//TODO: set distance and time step before calling this.
	protected void paintComponent (Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		drawRuler(g2);
		drawIndicators(g2);
		
		drawDefaultTimeBar(g2);
		drawSelectedRegion(g2);
	}

	private void drawRuler(Graphics2D g){
		Font f = new Font("sans-serif", Font.PLAIN, 9);
		g.setFont(f);
		g.setColor(Color.BLACK);

		int time = new Double(minValue.getTime()).intValue();
		g.drawLine(RULER_INCENTS, RULER_HEIGHT + INCENTS, RULER_INCENTS, RULER_HEIGHT-8 + INCENTS);
		String timeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Time(time));
		g.drawString("" + timeString, RULER_INCENTS, RULER_HEIGHT + 10 + INCENTS);
		
		for (int x = rulerDistanceStep; x < getWidth(); x += rulerDistanceStep) {
			time += rulerTimeStep;
			if(time > maxValue.getTime())
				break;
			g.drawLine(x, INCENTS , x, INCENTS + 3);
			if (x % (5*rulerDistanceStep) == 0) {
				g.drawLine(x, RULER_HEIGHT + INCENTS, x, RULER_HEIGHT-8 + INCENTS);
				timeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Time(time));
				g.drawString("" + timeString, x - 6, RULER_HEIGHT + 10 + INCENTS);

			}
			
			
		}

	}
	
	private void drawIndicators(Graphics2D g){
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3));
		
		for (Indicator ind : indicators) {
			int start = new Long(ind.getValue().getTime() - session.getStart().getTime()).intValue() / rulerTimeStep * rulerDistanceStep + RULER_INCENTS;
			g.setColor(ind.getColor());
			g.fillRect(start, 0 + RULER_INCENTS + RULER_HEIGHT + 10, INDICATOR_WIDTH, INDICATOR_WIDTH);
		}
		
	}

	private void drawDefaultTimeBar(Graphics2D g){
		g.setColor(Color.gray);
		g.setStroke(new BasicStroke(1));
		g.drawRect(INCENTS, INCENTS, (new Double(maxValue.getTime()).intValue() - new Double(minValue.getTime()).intValue())/ rulerTimeStep * rulerDistanceStep + INCENTS, SELECTOR_HEIGHT);
	}
	
	public void drawSelectedRegion(Graphics2D g){
		//draw the selected region
		int startMarkX1 = new Long(startValue.getTime() - session.getStart().getTime()).intValue() / rulerTimeStep * rulerDistanceStep + INCENTS;
		int startMarkY1 = INCENTS;
		int startMarkY2 = SELECTOR_HEIGHT + INCENTS;

		int endMarkX1 = (new Long(endValue.getTime() - session.getStart().getTime()).intValue())/ rulerTimeStep * rulerDistanceStep + INCENTS; //-new Double(startValue).intValue()
		int endMarkY1 = INCENTS;
		//int endMarkX2 = SELECTOR_WIDTH;//(new Double(endValue).intValue()-new Double(startValue).intValue())/ rulerTimeStep * rulerDistanceStep + 10;
		int endMarkY2 = SELECTOR_HEIGHT + INCENTS;

		g.setStroke(new BasicStroke(1));
		g.setColor(Color.black);
		g.drawLine(startMarkX1, startMarkY1, startMarkX1, startMarkY2);
		g.drawLine(endMarkX1, endMarkY1, endMarkX1, endMarkY2);
		g.drawLine(startMarkX1, startMarkY1, endMarkX1, startMarkY1);
		g.drawLine(startMarkX1, endMarkY2, endMarkX1, endMarkY2);
		
		g.setColor(new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 95));
		g.fillRect(INCENTS, INCENTS, startMarkX1 - INCENTS, startMarkY2 - INCENTS);
		g.fillRect(endMarkX1, endMarkY1, (new Double(maxValue.getTime()).intValue() - new Double(minValue.getTime()).intValue())/ rulerTimeStep * rulerDistanceStep - endMarkX1 + 2 * INCENTS, endMarkY2 - INCENTS);		

		g.setColor(new Color(0xf8f5f4));
		g.fillRect(startMarkX1 - KNOB_WIDTH/2, (startMarkY1 + startMarkY2)/2 - KNOB_HEIGHT/2, KNOB_WIDTH, KNOB_HEIGHT);//(startMarkX1 + startMarkX2)/2 + KNOB_WIDTH/2, (startMarkY1 + startMarkY2)/2 + KNOB_HEIGHT/2);
		g.fillRect(endMarkX1 - KNOB_WIDTH/2, (endMarkY1 + endMarkY2)/2 - KNOB_HEIGHT/2, KNOB_WIDTH, KNOB_HEIGHT);//(endMarkX1 + endMarkX2)/2 + KNOB_WIDTH/2, (endMarkY1 + endMarkY2)/2 + KNOB_HEIGHT/2);
		
		g.setColor(Color.black);
		g.drawRect(startMarkX1 - KNOB_WIDTH/2, (startMarkY1 + startMarkY2)/2 - KNOB_HEIGHT/2, KNOB_WIDTH, KNOB_HEIGHT);//(startMarkX1 + startMarkX2)/2 + KNOB_WIDTH/2, (startMarkY1 + startMarkY2)/2 + KNOB_HEIGHT/2);
		g.drawRect(endMarkX1 - KNOB_WIDTH/2, (endMarkY1 + endMarkY2)/2 - KNOB_HEIGHT/2, KNOB_WIDTH, KNOB_HEIGHT);//(endMarkX1 + endMarkX2)/2 + KNOB_WIDTH/2, (endMarkY1 + endMarkY2)/2 + KNOB_HEIGHT/2);
	}
	
	public Time getStartValue() {
		return startValue;
	}

	public void setStartValue(Time startValue) {
		this.startValue = startValue;
	}

	public Time getEndValue() {
		return endValue;
	}

	public void setEndValue(Time endValue) {
		this.endValue = endValue;
	}

	public Time getMinValue() {
		return minValue;
	}

	public void setMinValue(Time minValue) {
		this.minValue = minValue;
	}

	public Time getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Time maxValue) {
		this.maxValue = maxValue;
	}

	public void setTimeBarRulerSteps(int rulerDistanceStep, int rulerTimeStep){
		this.rulerDistanceStep = rulerDistanceStep;
		this.rulerTimeStep = rulerTimeStep;
	}
	
}
