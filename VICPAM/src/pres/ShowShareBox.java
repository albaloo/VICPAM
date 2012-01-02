/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import pres.TimeAlignedViewComponent.ThemeColors;

import domain.UserActivity.ActivityType;
import domain.UserActivity.SharedDevice;

import sun.awt.image.BufferedImageGraphicsConfig;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class ShowShareBox extends JButton implements Comparable<ShowShareBox>{

    private BufferedImage programIcon;
    
    private BufferedImage activityIcon;
    
    private BufferedImage deviceIcon;
    
    private ActivityType activityType;
    
    private SharedDevice sharedDevice;
    
	private ThemeColors themeColor;
    
    private String appIconPath;

		public ShowShareBox(String iconPath, String bgPath, ActivityType activityType, ThemeColors selectedThemeColor, SharedDevice sharedDevice) {
		super();
		this.appIconPath = iconPath;
		this.programIcon = ImageUtils.loadImage(iconPath);
		this.themeColor = selectedThemeColor;
		this.sharedDevice = sharedDevice;
		
		if(bgPath == null || bgPath == "")
			this.activityIcon = null;
		else
			this.activityIcon = ImageUtils.loadImage(bgPath);
		this.activityType = activityType;
		if(sharedDevice == SharedDevice.SHAREDDISPLAY){
			deviceIcon = ImageUtils.loadImage("res/sharedDisplay2.gif");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		if(activityType == ActivityType.SHOW){
			if(themeColor == ThemeColors.RED)
				g2.setColor(new Color(0x517800));//new Color(0x8ff069));//new Color(0xde594c));
			else
				g2.setColor(new Color(0x43ba71));//0x32e632));//0xffff33));
			//Draw rectangle
			g2.fillRect(0, 0, getWidth(), getHeight());
			this.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));

		}else if(activityType == ActivityType.SHARE){
			//set color
			if(themeColor == ThemeColors.RED)
				g2.setColor(new Color(0x7d4374));//new Color(0xf069bc));//(0xd84837));//(0xc74245));//(0x4268af));
			else
				g2.setColor(new Color(0x9843ba));//0xaa32e6));//0x99ccff));//2fb9f5));
			
			//Draw rectangle
			g2.fillRect(0, 0, getWidth(), getHeight());
			this.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));			
		}else{
			//set color
			if(themeColor == ThemeColors.RED)
				g2.setColor(new Color(0x7474b5));//(new Color(0x6aa5ef));//(0xf59e13));
			else
				g2.setColor(new Color(0x4379ba));//0x32aae6));//0x99ff33));
			
			//Draw rectangle
			g2.fillRect(0, 0, getWidth(), getHeight());
			this.setBorder(BorderFactory.createLineBorder(new Color(0x999999)));
		}

		//Draw app icon
		int minDimension = Math.min(getWidth(), getHeight());
		BufferedImage resizedImage = resizeTrick(programIcon, minDimension, minDimension);
		BufferedImage transparentImage = ImageUtils.makeColorTransparent(
				resizedImage, Color.BLACK);
		g2.drawImage(transparentImage, 0, 0, null);
		//Draw device icon
		if(sharedDevice == SharedDevice.SHAREDDISPLAY) {
			BufferedImage resizedDeviceIcon= resizeTrick(deviceIcon, minDimension, minDimension);
			if(minDimension == getWidth()) {
				g.drawImage(resizedDeviceIcon, 0, getHeight()/2, null);
			}
			else {
				g.drawImage(resizedDeviceIcon, getWidth()-2*minDimension, 0, null);
			}
		}
		if(activityIcon != null) {
			BufferedImage resizedActivityIcon;
			if(activityType == ActivityType.SHOW || activityType == ActivityType.SHARE)
				resizedActivityIcon= resize(activityIcon, minDimension, minDimension);
			else
				resizedActivityIcon= resizeTrick(activityIcon, minDimension, minDimension);
			if(minDimension == getWidth()) {
				g.drawImage(resizedActivityIcon, 0, getHeight(), null);
			}
			else {
				g.drawImage(resizedActivityIcon, getWidth()-minDimension, 0, null);
			}
		}
		
	} 

	public SharedDevice getDevice(){
		return sharedDevice;
	}
	public String getAppName() {
		if(appIconPath.charAt(appIconPath.length()-6)=='B')
			return appIconPath.substring(4, appIconPath.length()-7);
		else
			return appIconPath.substring(4, appIconPath.length()-4);
	}

    public ActivityType getActivityType() {
		return activityType;
	}
	
	private static BufferedImage resizeTrick(BufferedImage image, int width, int height) {
		image = createCompatibleImage(image);
		image = resize(image, 100, 100);
		image = blurImage(image);
		image = resize(image, width, height);
		return image;
	}
	
	private static BufferedImage resize(BufferedImage image, int width, int height) {
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	} 
	
	public static BufferedImage blurImage(BufferedImage image) {
		float ninth = 1.0f/9.0f;
		float[] blurKernel = {
		ninth, ninth, ninth,
		ninth, ninth, ninth,
		ninth, ninth, ninth
		};

		HashMap<Key, Object> map = new HashMap<Key, Object>();

		map.put(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
		return op.filter(image, null);		
	} 
	
	private static BufferedImage createCompatibleImage(BufferedImage image) {
		GraphicsConfiguration gc = BufferedImageGraphicsConfig.getConfig(image);
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage result = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		Graphics2D g2 = result.createGraphics();
		g2.drawRenderedImage(image, null);
		g2.dispose();
		return result;
	}

	public void setThemeColor(ThemeColors selectedThemeColor) {
		this.themeColor = selectedThemeColor;
		this.repaint();
	}

	public int compareTo(ShowShareBox arg0) {
		if(this.getBounds().x == ((ShowShareBox)arg0).getBounds().x) {
			if(this.getBounds().width == ((ShowShareBox)arg0).getBounds().width)
				return 0;
			else if (this.getBounds().width < ((ShowShareBox)arg0).getBounds().width)
				return -1;
			else
				return 1;
		}
		else if(this.getBounds().x < ((ShowShareBox)arg0).getBounds().x)
			return -1;
		else //if(this.getStart() > ((UserActivity)arg0).getStart())
			return 1;
	}

	public void highlight() {
		this.setBounds(getX(), getY(), getWidth()+10, getHeight()+5);
		repaint();
	}

	public void undoHighlight() {
		this.setBounds(getX(), getY(), getWidth()-10, getHeight()-5);
		repaint();		
	}

	public void halfHighlight() {
		this.setBounds(getX(), getY(), getWidth()+5, getHeight()+2);
		repaint();
	}

	public void undoHalfHighlight() {
		this.setBounds(getX(), getY(), getWidth()-5, getHeight()-2);
		repaint();
	}
}
