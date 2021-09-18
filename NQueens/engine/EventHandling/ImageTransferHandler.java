package eventHandling;

import java.awt.Component;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.chess.engine.board.BoardUtils;
import com.chess.gui.Table.TilePanel;
import com.chess.gui.TileLabel;

public class ImageTransferHandler extends TransferHandler {
	public static final DataFlavor SUPPORTED_FLAVOR = DataFlavor.imageFlavor;
	private ImageIcon icon;
	
	public ImageTransferHandler(ImageIcon value) {
		this.icon = value;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}

	
	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		if(!info.isDrop()) {
			return false;
		}
		
		if(!info.isDataFlavorSupported(DataFlavor.imageFlavor)) {
			return false;
		}
		return true;	
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return DnDConstants.ACTION_MOVE;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		return new ImageSelection(getIcon());
	}
	
	@Override
	protected void exportDone(JComponent src, Transferable data, int action) {
		super.exportDone(src, data, action);
		((TileLabel)src).setVisible(false);
		TilePanel temp = (TilePanel)src.getParent();
		//System.out.println(" export tile Src tile: " + BoardUtils.getPos(temp.getTileId()));
		((TileLabel)src).getParent().remove((TileLabel)src);
		
	}
	
	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		boolean success = false;
		//System.out.println("Call to import data");
		TilePanel temp = (TilePanel)support.getComponent().getParent();
		//System.out.println("panel tranfer data to :" + temp.getTileId());
		if(canImport(support)) {
			try {
				Transferable t = support.getTransferable();
				Object value = t.getTransferData(SUPPORTED_FLAVOR);
				if(value instanceof Image) {
					Component component = support.getComponent();
//					TilePanel temp = (TilePanel)support.getComponent().getParent();
//					System.out.println("panel tranfer data to :" + temp.getTileId());
					TileLabel label = new TileLabel((ImageIcon) value);
					//((TilePanel)component).setLabel(label);
				}
				success = true;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}
}
