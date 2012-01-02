/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package utils;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static BufferedImage loadImage(String filePath) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading the image file:"
					+ filePath);
		}

		return bimg;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}

	public static BufferedImage makeColorTransparent(BufferedImage image,
			Color color) {
		BufferedImage dimg = new BufferedImage(image.getWidth(), image
				.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dimg.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(image, null, 0, 0);
		g.dispose();

		for (int i = 0; i < dimg.getHeight(); i++) {
			for (int j = 0; j < dimg.getWidth(); j++) {
				if (dimg.getRGB(j, i) == color.getRGB()) {
					dimg.setRGB(j, i, 0x8F1C1C);
				}
			}
		}
		
		return dimg;
	}
}
