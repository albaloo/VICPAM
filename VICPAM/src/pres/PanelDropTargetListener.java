/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package pres;
/**
 * @author Roshanak Zilouchian
 */

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



public class PanelDropTargetListener implements DropTargetListener {

	private final TimeAlignedView rootPanel;
	    
	  private static final Cursor droppableCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
	           notDroppableCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

	  public PanelDropTargetListener(TimeAlignedView rootPanel) {
	        this.rootPanel = rootPanel;
	    }

	    // Could easily find uses for these, like cursor changes, etc.
	    public void dragEnter(DropTargetDragEvent dtde) {
	        if (!this.rootPanel.getCursor().equals(droppableCursor)) {
	            this.rootPanel.setCursor(droppableCursor);
	            System.out.println("drag entered");
	        }
	        
	    }
	    public void dragOver(DropTargetDragEvent dtde) {
	        if (!this.rootPanel.getCursor().equals(droppableCursor)) {
	            this.rootPanel.setCursor(droppableCursor);
	        }
	    }
	    public void dropActionChanged(DropTargetDragEvent dtde) {
	    	this.rootPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }
	    public void dragExit(DropTargetEvent dte) {
	        this.rootPanel.setCursor(notDroppableCursor);
	    }

	    /**
	     * <p>The user drops the item. Performs the drag and drop calculations and layout.</p>
	     * @param dtde
	     */
	    public void drop(DropTargetDropEvent dtde) {
	        
	        // Done with cursors, dropping
	        this.rootPanel.setCursor(Cursor.getDefaultCursor());
	        
	        // Just going to grab the expected DataFlavor to make sure
	        // we know what is being dropped
	        DataFlavor dragAndDropPanelFlavor = null;
	        
	        Object transferableObj = null;
	        Transferable transferable = null;
	        
	        try {
	            // Grab expected flavor
	            dragAndDropPanelFlavor = TimeAlignedView.getDragAndDropPanelDataFlavor();
	            
	            transferable = dtde.getTransferable();
	           // DropTargetContext c = dtde.getDropTargetContext();
	            
	            // What does the Transferable support
	            if (transferable.isDataFlavorSupported(dragAndDropPanelFlavor)) {
	                transferableObj = dtde.getTransferable().getTransferData(dragAndDropPanelFlavor);
	            } 
	            
	        } catch (Exception ex) { /* nope, not the place */ }
	        
	        // If didn't find an item, bail
	        if (transferableObj == null) {
	            return;
	        }
	        
	        // Cast it to the panel. By this point, we have verified it is 
	        // a RandomDragAndDropPanel.
	        TimeAlignedViewComponent droppedPanel = (TimeAlignedViewComponent)transferableObj;
	        
	        // Get the y offset from the top of the WorkFlowSheetPanel
	        // for the drop option (the cursor on the drop)
	        final int dropYLoc = dtde.getLocation().y;

	        // We need to map the Y axis values of drop as well as other
	        // RandomDragAndDropPanel so can sort by location.
	        java.util.Map<Integer, TimeAlignedViewComponent> yLocMapForPanels = new HashMap<Integer, TimeAlignedViewComponent>();
	        yLocMapForPanels.put(dropYLoc, droppedPanel);

	        // Iterate through the existing demo panels. Going to find their locations.
	        for (TimeAlignedViewComponent nextPanel : rootPanel.getAllComponents()) {

	            // Grab the y value
	            int y = nextPanel.getWrapper().getY()+1;

	            // If the dropped panel, skip
	            if (!nextPanel.equals(droppedPanel)) {
	                yLocMapForPanels.put(y, nextPanel);
	            }
	        }

	        // Grab the Y values and sort them
	        ArrayList<Integer> sortableYValues = new ArrayList<Integer>();
	        sortableYValues.addAll(yLocMapForPanels.keySet());
	        Collections.sort(sortableYValues);

	        // Put the panels in list in order of appearance
	        ArrayList<TimeAlignedViewComponent> orderedPanels = new ArrayList<TimeAlignedViewComponent>();
	        for (Integer i : sortableYValues) {
	            orderedPanels.add(yLocMapForPanels.get(i));
	        }
	        
	        // Grab the in-memory list and re-add panels in order.
	        ArrayList<TimeAlignedViewComponent> inMemoryPanelList = this.rootPanel.getAllComponents();
	        inMemoryPanelList.clear();
	        inMemoryPanelList.addAll(orderedPanels);
	    
	        // Request relayout contents, or else won't update GUI following drop.
	        // Will add back in the order to which we just sorted
	        this.rootPanel.relayout();
	    }
}
