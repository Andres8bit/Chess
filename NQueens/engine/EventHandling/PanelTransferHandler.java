package eventHandling;

import java.awt.Component;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import com.chess.engine.board.BoardUtils;
import com.chess.gui.Table.TilePanel;
import com.chess.gui.TileLabel;

public class PanelTransferHandler extends TransferHandler {
	private static final DataFlavor SUPPORTED_FLAVOR = DataFlavor.imageFlavor;
	
	public PanelTransferHandler() {
		
	}
	
	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		if(!info.isDrop()) {
			return false;
		}
		
		TilePanel temp = (TilePanel)info.getComponent();
		//System.out.println("Tile can import? " + BoardUtils.getPos(temp.getTileId()));
		
		if(!info.isDataFlavorSupported(DataFlavor.imageFlavor)) {
			return false;
		}
		return true;	
	}
	
	

	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		boolean success = false;
		//TilePanel temp = (TilePanel)support.getComponent().getParent();
		System.out.println("call to import data for JPanel");
	
		//System.out.println("panel tranfer data to :" + temp.getTileId());
		if(canImport(support)) {
			try {
				Transferable t = support.getTransferable();
				//System.out.println("transferable: " + t.toString());
				Object value = t.getTransferData(SUPPORTED_FLAVOR);
			//	System.out.println("returned value:" + value.toString());
				if(value instanceof ImageIcon) {
					System.out.println("value is an image");
					TilePanel temp = (TilePanel)support.getComponent();
					System.out.println("Tile importing: " + BoardUtils.getPos(temp.getTileId()));
				//	System.out.println("Tile importing parent: " + BoardUtils.getPos(tempParent.getTileId()));
					Component component = support.getComponent();
					TileLabel label = new TileLabel((ImageIcon) value);
					((TilePanel)component).add(label);
					success = true;
				}

			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	


}
