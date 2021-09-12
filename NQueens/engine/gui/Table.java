package com.chess.gui;

//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.GridBagLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.*;
//import java.awt.event.*;
//import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JMenu;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.peices.Piece;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;



public class Table {
	
	private Tile srcTile;
	private Tile destTile;
	private Piece playerPiece;
	private final MoveLog log;
	private Board chessBoard;
	private BoardDirection boardDirection;
	private CapturePanel capturesPanel;
	private HistoryPanel logPanel;
	private boolean highLightLegalMoves;
	private final BoardPanel boardPanel;
	private final JFrame gameFrame;
	private static String pieceIconPath = "C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\";
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(800,800);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(600,600);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(60,60);
    private Color lightTileColor = Color.decode("#d9deda");
    		
    private Color darkTileColor = Color.decode("#63656a");
    
	public Table() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		this.chessBoard = Board.createStandardBoard();
		this.capturesPanel = new CapturePanel();
		this.logPanel = new HistoryPanel();
		this.boardPanel = new BoardPanel();
		this.log = new MoveLog();
		final JMenuBar tableMenu = createMenuBar();
		
		this.gameFrame.setJMenuBar(tableMenu);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.boardDirection = BoardDirection.NORMAL;

		this.gameFrame.add(this.capturesPanel,BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
		this.gameFrame.add(this.logPanel,BorderLayout.EAST);
		this.gameFrame.setVisible(true);
		this.highLightLegalMoves = false;

	}

	private JMenuBar createMenuBar() {
		final JMenuBar tableMenu = new JMenuBar();
		
		tableMenu.add(createFileMenu());
		tableMenu.add(createPreferencesMenu());
		return tableMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu preferences = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		
		flipBoardMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);	
			}
			
		});
		
		preferences.add(flipBoardMenuItem);
		preferences.addSeparator();
		final JCheckBoxMenuItem legalMoveHighLightCheckbox = new JCheckBoxMenuItem("HighLight Legal Moves",false);
		
		legalMoveHighLightCheckbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				highLightLegalMoves = legalMoveHighLightCheckbox.isSelected();
				
			}
		});
		preferences.add(legalMoveHighLightCheckbox);
		
		return preferences;
	}

	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open that pgn file.");
			}
		});
		
		
		final JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		fileMenu.add(openPGN);
		fileMenu.add(exitItem);
		return fileMenu;
	}
	
	private class BoardPanel extends JPanel{
		final List<TilePanel> boardTiles;
		
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			
			for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(final Board board) {
			removeAll();
			
			for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)){
				tilePanel.drawTile(board);
				add(tilePanel);
			}			
			validate();
			repaint();
		}
	}
	
	
    enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();

    }
    
    public static class MoveLog{
    	private final List<Move> moves;
    	
    	MoveLog(){
    		this.moves = new ArrayList<>();
    	}
    	
    	public List<Move> getMoves(){
    		return this.moves;
    	}
    	
    	public void addMove(final Move move) {
    		this.moves.add(move);
    	}
    	
    	public int size() {
    		return this.moves.size();
    	}
    	
    	public void clear() {
    		this.moves.clear();
    	}
    	
    	public  Move removeMove(int index) {
    		return this.moves.remove(index);
    	}
    	
    	public boolean removeMove(final Move move) {
    		return this.moves.remove(move);
    	}
    	
    }
    
    
	private class TilePanel extends JPanel{
		private final int tileId;
		
		

		TilePanel(final BoardPanel board, final int tileId){
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignPieceIcon(chessBoard);
		
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					
					if(SwingUtilities.isRightMouseButton(e)) {
						srcTile = null;
						destTile = null;
						//playerPiece = null;
					}else if(SwingUtilities.isLeftMouseButton(e)) {
						if(srcTile == null) {
							srcTile = chessBoard.getTile(tileId);
							playerPiece = srcTile.getPiece();
							if(playerPiece == null) {
								srcTile = null;
							}else {
								System.out.println("srcTile -> " + BoardUtils.getPos(tileId));
							}
						}else {
							destTile = chessBoard.getTile(tileId);
							System.out.println("Selected tile ->" + BoardUtils.getPos(destTile.getCoord()));
							final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), tileId);
							final MoveTransition transition = chessBoard.curPlayer().makeMove(move);
							
							if(transition.getMoveStatus().isDone()) {
								chessBoard = transition.getToBoard();
								log.addMove(move);
							}
							
							srcTile = null;
							destTile = null;
							playerPiece = null;				
						}
						
						SwingUtilities.invokeLater(() -> {
								logPanel.redo(chessBoard,log);
								capturesPanel.redo(log);
								boardPanel.drawBoard(chessBoard);
								playSound();
							
						});
					}
					
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
			validate();
		}

		@Override
		public String toString() {
			if(playerPiece != null) {
				return playerPiece.toString() + " tileId: "+ tileId;
			}
			return "tileId : " + tileId;
		}
		
		public void drawTile(final Board board) {
			assignTileColor();
			assignPieceIcon(board);
			validate();
			highLightLegalMoves(board);
			repaint();
		}
		
		private void highLightLegalMoves(final Board board) {
			if(highLightLegalMoves) {
				for(final Move move: pieceLegalMoves(board)) {
					if(move.getDest() == this.tileId) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\green_dot.png")))));
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		
		private Collection<Move> pieceLegalMoves(final Board board) {
			if(playerPiece != null && playerPiece.piece_alliance() == board.curPlayer().getAlliance()) {
				return playerPiece.legalMoves(board);
			}
			
			return Collections.emptyList();
		}

		private void assignTileColor() {
			
			if(    BoardUtils.FIRST_ROW.get(this.tileId)
				|| BoardUtils.THIRD_ROW.get(this.tileId)
				|| BoardUtils.FIFTH_ROW.get(this.tileId)
				|| BoardUtils.SEVENTH_ROW.get(this.tileId)) {
				
				setBackground(this.tileId % 2 == 0 ? lightTileColor:darkTileColor);
			
			}else if(    BoardUtils.SECOND_ROW.get(this.tileId)
					  || BoardUtils.FOURTH_ROW.get(this.tileId)
					  || BoardUtils.SIXTH_ROW.get(this.tileId)
					  || BoardUtils.EIGHTH_ROW.get(this.tileId)) {
				
				setBackground(this.tileId % 2 != 0 ? lightTileColor: darkTileColor);
			}
		}
		
		private void assignPieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(this.tileId).isOccupied()) {
				try {
					final BufferedImage image = ImageIO.read(new File(pieceIconPath + board.getTile(this.tileId).getPiece().piece_alliance().toString().substring(0,1)
							+ board.getTile(this.tileId).getPiece().toString() + ".PNG"));
					this.add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		private void playSound() {
		    
			


		}
	}
}
