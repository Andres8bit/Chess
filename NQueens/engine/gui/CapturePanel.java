package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.peices.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

public class CapturePanel extends JPanel{

	private static final Dimension DIMS = new Dimension(80,80);
	private static final Color PANEL_COLOR = Color.decode("#d9deda");
	private final JPanel northPanel;
	private final JPanel southPanel;
	private static String pieceIconPath = "C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\";
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	
	public CapturePanel() {
		super(new BorderLayout());
		
		setBackground(PANEL_COLOR);
		setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8,2));
		this.southPanel = new JPanel(new GridLayout(8,2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		
		add(this.northPanel,BorderLayout.NORTH);
		add(this.southPanel,BorderLayout.SOUTH);
		setPreferredSize(DIMS);
	}
	
	public void redo(final MoveLog log) {
		southPanel.removeAll();
		northPanel.removeAll();
		
		final List<Piece> wTaken = new ArrayList<>();
		final List<Piece> bTaken = new ArrayList<>();
		
		for(final Move move: log.getMoves()) {
			if(move.isAttack()) {
				System.out.println("is attack");
				final Piece taken = move.getAttackPiece();
				if(taken.piece_alliance().isWhite()) {
					wTaken.add(taken);
				}else if(taken.piece_alliance().isBlack()){
					bTaken.add(taken);
				}else {
					throw new RuntimeException("Unkown Onwer");
				}
			}
			
		}
		
		Collections.sort(wTaken,new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				return Ints.compare(o1.getVal(), o2.getVal());
			}
			
		});
		
		Collections.sort(bTaken,new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				return Ints.compare(o1.getVal(), o2.getVal());
			}
			
		});
		
		for(final Piece taken: wTaken) {
			try {
				final BufferedImage img = ImageIO.read(new File( pieceIconPath + taken.piece_alliance().toString().substring(0,1)
						+ taken.toString() + ".PNG"));
				final ImageIcon icon = new ImageIcon(img);
				final JLabel imgLabel = new JLabel(icon);
				this.southPanel.add(imgLabel);
				
				}
				catch(final IOException e){
				e.printStackTrace();
				}
			}
		
		for(final Piece taken: bTaken) {
			try {
				final BufferedImage img = ImageIO.read(new File( pieceIconPath + taken.piece_alliance().toString().substring(0,1)
						+ taken.toString() + ".PNG"));
				final ImageIcon icon = new ImageIcon(img);
				final JLabel imgLabel = new JLabel(icon);
				this.southPanel.add(imgLabel);
				
				}
				catch(final IOException e){
				e.printStackTrace();
				}
			}
		
		validate();
		
	}
	
}
