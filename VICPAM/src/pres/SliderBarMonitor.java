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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Time;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.Date;

@SuppressWarnings("serial")
public class SliderBarMonitor extends JPanel implements ChangeListener{
		
	JSpinner minValue = new JSpinner();
	JSpinner maxValue = new JSpinner();
	JLabel minValueLabel = new JLabel();
	JLabel maxValueLabel = new JLabel();
	MainWindow mainFrame;
	private Color themeColor;
	
	public SliderBarMonitor(MainWindow mainFrame) {
		this.setLayout(new GridBagLayout());
		this.mainFrame = mainFrame;
		this.themeColor = mainFrame.getSession().getColor();//new Color(0x0c439c);
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
			
		g2.setColor(new Color(0xf8f5f4));
		g2.fillRoundRect(0, 0, getWidth()-3, getHeight()-3, 15, 15);
			
		g2.setStroke(new BasicStroke(3f));
		g2.setColor(themeColor);

		g2.dispose();
	}
		
	public void configureMinMaxSpinners(Time startTime, Time endTime) {
		Time v = mainFrame.getSession().getStart();
	    SpinnerDateModel spinnerModel = new SpinnerDateModel(v, startTime, endTime, Calendar.MINUTE);
	    minValue.setModel(spinnerModel);
	    minValue.setEditor(new JSpinner.DateEditor(minValue, "HH:mm:ss a"));
	    minValue.addChangeListener(this);

	    Time v2 = new Time(mainFrame.getSession().getSelectedDuration() + mainFrame.getSession().getStart().getTime());
	    SpinnerDateModel spinnerModel2 = new SpinnerDateModel(v2, startTime, endTime, Calendar.MINUTE);
	    maxValue.setModel(spinnerModel2);
	    maxValue.setEditor(new JSpinner.DateEditor(maxValue, "HH:mm:ss a"));
	    maxValue.addChangeListener(this);

	    //position: gridx, gridy - numberofCells:gridwidth, gridheight - resizewaigth: waightx, y 
	    this.add(minValueLabel, new GridBagConstraints(0, 0, 1, 1, 0.7, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(minValue, new GridBagConstraints(1, 0, 1, 1,
				0.5, 0.2, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(maxValueLabel, new GridBagConstraints(2, 0, 1, 1, 0.2, 0.12,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(maxValue, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.2,
			GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		
        minValueLabel.setText("<html><body> <b>Start: </b></body></html>");
        maxValueLabel.setText("<html><body> <b>End: </b></body></html>");
			
	}
	
	public void setSpinnersValue(double minVal, double maxVal) {
		minValue.setValue(new Double(minVal));
		maxValue.setValue(new Double(maxVal));
	}
	
	public double getStartTime() {
		return ((Double)minValue.getValue()).doubleValue();
	}

	public double getEndTime() {
		return ((Double)maxValue.getValue()).doubleValue();
	}
	
	 public void stateChanged(ChangeEvent e) {
	        SpinnerModel maxNumModel = maxValue.getModel();
	        SpinnerModel minNumModel = minValue.getModel();
	        if (maxNumModel instanceof SpinnerDateModel && minNumModel instanceof SpinnerDateModel) {
	        	Date minDate = ((Date)((SpinnerDateModel)minNumModel).getValue());
	        	Date maxDate = (Date)((((SpinnerDateModel)maxNumModel).getValue()));
	        	mainFrame.updateSliderBar(new Time(minDate.getTime()), new Time(maxDate.getTime()));
	        }
	}

	public void setThemeColor(Color color) {
		themeColor = color;
		repaint();
	}

}
