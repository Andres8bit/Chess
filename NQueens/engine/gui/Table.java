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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
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
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Piece;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;
import com.chess.gui.Table.TilePanel;
import com.google.common.collect.Lists;

import audio.AudioPlayer;
import eventHandling.ImageSelection;
import eventHandling.MouseInput;



public class Table {
	

	private MouseInput mouse;
	private Tile srcTile;
	private Tile destTile;
	private Tile testSrc;
	private Tile testDest;
	private Piece playerPiece;
	private final MoveLog log;
	private Board chessBoard;
	private BoardDirection boardDirection;
	private CapturePanel capturesPanel;
	private HistoryPanel logPanel;
	private boolean highLightLegalMoves;
	private boolean bAi;
	private boolean wAi;
	private final AudioPlayer soundEffects;
	private final BoardPanel boardPanel;
	private final JFrame gameFrame;
	private static String pieceIconPath = "C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\";
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(800,800);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(100,100);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private Color lightTileColor = Color.decode("#d9deda");
    		
    private Color darkTileColor = Color.decode("#63656a");
    
	public Table() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		this.chessBoard = Board.createStandardBoard();
		this.capturesPanel = new CapturePanel();
		this.logPanel = new HistoryPanel();
		this.boardPanel = new BoardPanel();
		this.soundEffects = new AudioPlayer();
		this.mouse = new MouseInput();
		this.soundEffects.initialize();
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
		this.bAi = false;
		this.wAi = false;

	}

	private JMenuBar createMenuBar() {
		final JMenuBar tableMenu = new JMenuBar();
		
		tableMenu.add(createFileMenu());
		tableMenu.add(createPreferencesMenu());
		return tableMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu preferences = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = flipView();
		final  JCheckBoxMenuItem wPlayerAi = setWhiteAi();
		final  JCheckBoxMenuItem bPlayerAi = setBlackAi();
		final JCheckBoxMenuItem legalMoveHighLightCheckbox = highLightMoves();
		
		preferences.add(flipBoardMenuItem);
		preferences.addSeparator();
		

		preferences.add(legalMoveHighLightCheckbox);
		preferences.addSeparator();		

	    preferences.add(bPlayerAi);
		preferences.addSeparator();
	    
		preferences.add(wPlayerAi);
		
		return preferences;
	}

	private JCheckBoxMenuItem setBlackAi() {
		final JCheckBoxMenuItem blackAi = new JCheckBoxMenuItem("Set Black to AI",false);
		
		blackAi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bAi = blackAi.isSelected();
			}
		});
		
		return blackAi;
	}
	

	private JCheckBoxMenuItem setWhiteAi() {
		final JCheckBoxMenuItem whiteAi = new JCheckBoxMenuItem("Set White to AI",false);
		
		whiteAi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				wAi = whiteAi.isSelected();
			}
		});
		
		return 	whiteAi;
	}

	private JCheckBoxMenuItem highLightMoves() {

		final JCheckBoxMenuItem legalMoveHighLight =  new JCheckBoxMenuItem("HighLight Legal Moves",false);
		
		legalMoveHighLight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				highLightLegalMoves = legalMoveHighLight.isSelected();
				
			}
		});
		
		return legalMoveHighLight;
	}

	private JMenuItem flipView() {
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		
		flipBoardMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);	
			}
			
		});
		
		return flipBoardMenuItem;
		
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
			//PanelTransferHandler drag = new PanelTransferHandler();
			
			for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
				TilePanel tilePanel = new TilePanel(this,i);
	

				add(tilePanel);
				this.boardTiles.add(tilePanel);
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
    
    
	public class TilePanel extends JPanel{
		 public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.imageFlavor;
		private final int tileId;
		private TileLabel icon;
		
		

		TilePanel(final BoardPanel board, final int tileId){
			super(new GridBagLayout());
			this.tileId = tileId;
			this.setTransferHandler(new eventHandling.PanelTransferHandler());
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignPieceIcon(chessBoard);
			assignTileLable();
			eventHandler();
			validate();
		}

		
		public JLabel getLabel() {
			return icon;
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
			assignTileLable();
			validate();
			highLightLegalMoves(board);
			repaint();
		}
		
		private void assignTileLable() {
			if(BoardUtils.FIRST_ROW.get(tileId)) {
			JLabel label = 	new JLabel(BoardUtils.getPos(tileId).substring(0,1));
			label.setAlignmentY(BOTTOM_ALIGNMENT);
			label.setAlignmentX(RIGHT_ALIGNMENT);
			label.setSize( new Dimension(2,2));
				this.add(label);
			}
			
        if(BoardUtils.FIRST_COLUMN.get(tileId)) {
			JLabel label = 	new JLabel(BoardUtils.getPos(tileId).substring(1));
			label.setAlignmentY(BOTTOM_ALIGNMENT);
			label.setAlignmentX(RIGHT_ALIGNMENT);
			label.setSize( new Dimension(2,2));
				this.add(label);
        }
			
			
		}

		public int getTileId() {
			return this.tileId;
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
				this.icon = new TileLabel(board.getTile(this.tileId).getPiece().getImg());
			}else {
				this.icon = new TileLabel(null);
			}
			this.add(icon);
		}
		
		
		private void eventHandler() {

			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					// if it is a humans turn allow events
					if((chessBoard.curPlayer().getAlliance().isWhite() && !wAi) || (chessBoard.curPlayer().getAlliance().isBlack() && !bAi) ) {
					if(SwingUtilities.isRightMouseButton(e)) {
						mouse.reset();
						srcTile = null;
						destTile = null;
					}else if(SwingUtilities.isLeftMouseButton(e)) {
						mouse.mouseLeftClicked(e, tileId);
						if(srcTile == null) {
							if(mouse.getSrc() != -1) {
								//System.out.println("src != -1");
							}
							srcTile = chessBoard.getTile(tileId);
							playerPiece = srcTile.getPiece();
							if(playerPiece == null) {
								srcTile = null;
								mouse.reset();
							}else {
								setBorder(BorderFactory.createBevelBorder(0, Color.green, Color.green));;
								}
							}else {
								destTile = chessBoard.getTile(tileId);
								//System.out.println("Selected tile ->" + BoardUtils.getPos(destTile.getCoord()));
								final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), tileId);
								//System.out.println("Move made by player: " + move.toString());
								final MoveTransition transition = chessBoard.curPlayer().makeMove(move);
							
								if(transition.getMoveStatus().isDone()) {
									chessBoard = transition.getToBoard();
									log.addMove(move);
									soundEffects.processInput("place");
								}
							
							srcTile = null;
							destTile = null;
							playerPiece = null;	
							mouse.reset();
						}
						
							SwingUtilities.invokeLater(() -> {
								logPanel.redo(chessBoard,log);
								capturesPanel.redo(log);
								boardPanel.drawBoard(chessBoard);
							});
						}
				 }else { // AI's Move
					 if(bAi && chessBoard.curPlayer().getAlliance().isBlack()) {
						 chessBoard.runAi(Alliance.BLACK);
					 }else if(wAi && chessBoard.curPlayer().getAlliance().isWhite()) {
						 final Move aiMove = chessBoard.runAi(Alliance.WHITE);
					 }

				 }
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					System.out.println("released mouse");
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
					
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					setBorder( BorderFactory.createEmptyBorder());
				}	
				
			});
		}


		public ImageIcon getPiece() {
			if(chessBoard.getTile(tileId).isOccupied()) {
				return chessBoard.getTile(tileId).getPiece().getImg();
			}
			return null;
		}


		public void setLabel(TileLabel label) {
			this.icon = label;
			this.drawTile(chessBoard);
			
		}
		
	}
}
