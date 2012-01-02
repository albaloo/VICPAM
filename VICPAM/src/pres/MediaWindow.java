/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Container;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MediaWindow extends JFrame {
		public MediaWindow() {
			initComponents();
		}

		private void initComponents() {
			mediaPanel = new MediaPanel();
	
			setTitle("Video");
			Container contentPane = getContentPane();
			contentPane.setLayout(null);

			mediaPanel.setLocation(0, 0);
			mediaPanel.setBounds(0, 0, 300, 200);

			contentPane.add(mediaPanel);
					
		}

		
		private MediaPanel mediaPanel;
		
		public MediaPanel getMediaPanel() {
			return mediaPanel;
		}
		
		public void showVideo(){
			File file = new File("res/videoFile.mpg");
			URL url = null;
			URI uri = null;
			try {
				uri = file.toURI();
				url = uri.toURL();
				getMediaPanel()
						.showVideo(url);
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			}
		}

}
