package com.chess.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import eventHandling.ImageTransferHandler;

public class TileLabel extends JLabel{
	private int tileId;
	
	

	public TileLabel(final ImageIcon icon) {
		if(icon != null) {
			this.setIcon(icon);
		}else {
			this.setIcon(null);
		}
		
		this.setTransferHandler(new ImageTransferHandler(icon));
		
	     this.addMouseMotionListener(new MouseAdapter() {
	            @Override
	            public void mouseDragged(MouseEvent e) {
	                JLabel lbl = (JLabel) e.getSource();
	                TransferHandler handle = lbl.getTransferHandler();
	                handle.exportAsDrag(lbl, e, TransferHandler.MOVE);
	            }
	        });
	}
	
	
	public ImageIcon getImage() {
		return (ImageIcon)this.getIcon();
	}

}
