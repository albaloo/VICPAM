/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Color;
import java.sql.Time;

public class Indicator implements Comparable<Indicator>{
	private Time value;
	private Color color;
	public Indicator(Time value, Color color){
		this.value = value;
		this.color = color;
	}
	public Time getValue(){
		return value;
	}
	public Color getColor(){
		return this.color;
	}
	
	public int compareTo(Indicator arg0) {
		if(this.getValue() == arg0.getValue()) {
				return 0;
		}
		else if(this.getValue().getTime() < ((Indicator)arg0).getValue().getTime())
			return -1;
		else //if(this.getValue() > ((Indicator)arg0).getValue())
			return 1;
	}
}
