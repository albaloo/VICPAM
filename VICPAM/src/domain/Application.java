/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package domain;
/**
 * @author Roshanak Zilouchian
 */
public class Application implements Comparable<Application>{
	private String iconName;
	
	public Application(String iconName){
		this.iconName = iconName;
	}	
	public String getIconName() {
		return iconName;
	}
	public String getName() {
		if(iconName.charAt(iconName.length()-6)=='B')
			return iconName.substring(4, iconName.length()-7);
		else
			return iconName.substring(4, iconName.length()-4);
	}
	public int compareTo(Application arg0) {
		return getName().compareTo(arg0.getName());
	}
}
