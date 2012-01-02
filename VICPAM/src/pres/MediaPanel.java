/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MediaPanel extends JPanel {
	public MediaPanel() {
		setLayout(new BorderLayout()); // use a BorderLayout
	}

	//public void show(URL mediaURL) {
	public void showVideo(URL mediaURL) {

		// Use lightweight components for Swing compatibility
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);

		int width = 200;
		int height = 0;
		int videoWidth = 0;
		int videoHeight = 0;
		try {
			// create a player to play the media specified in the URL
			Player mediaPlayer = Manager.createRealizedPlayer(mediaURL);

			// get the components for the video and the playback controls
			Component video = mediaPlayer.getVisualComponent();
			Component controls = mediaPlayer.getControlPanelComponent();

			if (video != null){
				add(video, BorderLayout.CENTER); // add video component
			    Dimension videoSize = video.getPreferredSize();
			    videoWidth = videoSize.width;
			    videoHeight = videoSize.height;
			    width = videoWidth;
			    height += videoHeight;
			    video.setBounds(0, 0, videoWidth, videoHeight);

			}

			int controlPanelHeight = 0;
			if (controls != null){
			    controlPanelHeight = controls.getPreferredSize().height;
			    height += controlPanelHeight;
				add(controls, BorderLayout.SOUTH); // add controls
			}

			this.setBounds(0, 0, width, height);
			if (controls != null) 
				controls.setBounds(0, videoHeight,width, controlPanelHeight);

			//mediaPlayer.start(); // start playing the media clip
		} // end try
		catch (NoPlayerException noPlayerException) {
			System.err.println("No media player found");
		} // end catch
		catch (CannotRealizeException cannotRealizeException) {
//			System.err.println("Could not realize media player");
			cannotRealizeException.printStackTrace();
		} // end catch
		catch (IOException iOException) {
			System.err.println("Error reading from the source");
		} // end cath
	} // end MediaPanel constructor
} // end class MediaPanel