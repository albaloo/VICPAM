/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class DragAndDropTransferableHandler extends TransferHandler implements DragSourceMotionListener {

	    public DragAndDropTransferableHandler() {
	        super();
	    }

	    /**
	     * <p>This creates the Transferable object. In our case, RandomDragAndDropPanel implements Transferable, so this requires only a type cast.</p>
	     * @param c
	     * @return
	     */
	    @Override()
	    public Transferable createTransferable(JComponent c) {
 
	        // TaskInstancePanel implements Transferable
	        if (c instanceof TimeAlignedViewComponent) {
	            Transferable tip = (TimeAlignedViewComponent) c;
	            return tip;
	        }

	        // Not found
	        return null;
	    }

	    public void dragMouseMoved(DragSourceDragEvent dsde) {}

	    /**
	     * <p>This is queried to see whether the component can be copied, moved, both or neither. We are only concerned with copying.</p>
	     * @param c
	     * @return
	     */
	    @Override()
	    public int getSourceActions(JComponent c) {
	        	        
	        if (c instanceof TimeAlignedViewComponent) {
	            return TransferHandler.COPY;
	        }
	        
	        return TransferHandler.NONE;
	    }
}
