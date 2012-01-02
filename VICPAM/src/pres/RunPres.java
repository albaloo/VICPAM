/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RunPres {

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		
		  MainWindow mainForm = new MainWindow(); 
		
		  mainForm.setExtendedState(mainForm.getExtendedState()| Frame.MAXIMIZED_BOTH );
          mainForm.setVisible(true);
		  mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
