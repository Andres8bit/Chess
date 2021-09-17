package eventHandling;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

public class ImageSelection implements Transferable{
	private ImageIcon image;

	public ImageSelection(ImageIcon imageIcon) {
		this.image = imageIcon;
	}
	
	public static void copyImageToClipboard(ImageIcon img) {
		new ImageIcon();
		
		
		BufferedImage newImage = new BufferedImage(img.getIconWidth(),img.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		newImage.createGraphics().drawImage(img.getImage(),0,0,null);
		img = new ImageIcon(newImage);
		ImageSelection imgSelection = new ImageSelection(img);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		toolkit.getSystemClipboard().setContents(imgSelection, null);
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{
			DataFlavor.imageFlavor
		};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.imageFlavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if(flavor.equals(DataFlavor.imageFlavor) == false) {
			throw new UnsupportedFlavorException(flavor);
		}
		return image;
	}
	

}
