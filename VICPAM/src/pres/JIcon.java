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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import utils.ImageUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import domain.Session;

@SuppressWarnings("serial")
public class JIcon extends JButton {
	public BufferedImage image;

	private final int PLAYERS_LEN = 40;
	
	private final int ICONS_LEN = 20;
	
	private final int TEXT_LEN = 20;
	
	private String imagePath;

	private int enlargementAmount = 0;
	
	private boolean isPlayer = false;
	
	public JIcon(String imagePath, String iconType) {
		super();
		this.image = ImageUtils.loadImage(imagePath);
		this.imagePath = imagePath;
		if(iconType.equals("player"))
			isPlayer = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		BufferedImage resizedImage;
		try {
			resizeFromFile(new File(imagePath), new File(imagePath+"0"), getImageSize(), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		resizedImage = ImageUtils.loadImage(imagePath+"0");
	
		BufferedImage transparentImage;
		if(imagePath.contains("share") || imagePath.contains("show"))
			transparentImage = resizedImage;
		else
			transparentImage = ImageUtils.makeColorTransparent(
				resizedImage, Color.BLACK);

		image = transparentImage;
		
		g2.drawImage(image, 0, 0, null);

		if(isPlayer){
			g2.setColor(Color.black);		
			int y = getImageSize()+TEXT_LEN/2;
			g2.drawString(imagePath.substring(imagePath.indexOf('/')+1, imagePath.indexOf('.')), 0, y);
		}
		
	}

	public void setScale(double size){
		if(size < Session.getMaxDuration()/4)
			enlargementAmount+=5;
		else if(size < Session.getMaxDuration()/2)
			enlargementAmount+=10;
		else if(size < 3*Session.getMaxDuration()/4)
			enlargementAmount+=15;
		else
			enlargementAmount+=20;
	}
	
	public int getImageSize() {
		if(isPlayer)
			return PLAYERS_LEN + enlargementAmount;
		else
			return ICONS_LEN + enlargementAmount;
	}

	public int getIconHeight(){
		if(isPlayer)
			return TEXT_LEN + getImageSize();
		else
			return getImageSize();
	}
	
	public int getWidth() {
		return getImageSize();
	}

	public int getHeight() {
		return getIconHeight();
	}
	
	public void highlight(){
		enlargementAmount += 5;
		this.setBounds(getX(), getY(), getImageSize(), getIconHeight());
		repaint();
	}

	public void undoHighlight(){
		enlargementAmount -= 5;
		this.setBounds(getX(), getY(), getImageSize(), getIconHeight());
		repaint();
	}

	public static void resizeFromFile(File originalFile, File resizedFile, int newWidth, float quality) throws IOException {
		 
        if (quality < 0 || quality > 1) {
            throw new IllegalArgumentException("Quality has to be between 0 and 1");
        }
 
        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
        Image i = ii.getImage();
        Image resizedImage = null;
 
        int iWidth = i.getWidth(null);
        int iHeight = i.getHeight(null);
 
        if (iWidth > iHeight) {
            resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
        } else {
            resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
        }
 
        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(resizedImage).getImage();
 
        //BufferedImage originalImageBuffer = toBufferedImage(temp);
        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
                                                        BufferedImage.TYPE_INT_ARGB);
 
        // Copy image to buffered image.
        Graphics2D g = bufferedImage.createGraphics();
 
        
        // Clear background and paint the image.
        g.setColor(new Color(0,0,0,0));
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));

        g.drawImage(temp, 0, 0, null);
        g.dispose();
 
        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor, 1-(softenFactor*4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);
 
        // Write the jpeg to a file.
        FileOutputStream out = new FileOutputStream(resizedFile);
 
        // Encodes image as a JPEG data stream
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
 
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
 
        param.setQuality(quality, true);
 
        encoder.setJPEGEncodeParam(param);
        encoder.encode(bufferedImage);
    }
}


