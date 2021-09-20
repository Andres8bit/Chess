package com.chess.gui;

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
import java.util.concurrent.ExecutionException;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Piece;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.AI.MiniMax;
import com.chess.gui.Table.TilePanel;
import com.google.common.collect.Lists;

import audio.AudioPlayer;
import eventHandling.BoardListener;
import eventHandling.ImageSelection;
import eventHandling.MouseInput;




public class Table{
	

	private MouseInput mouse;
	private Tile srcTile;
	private Tile destTile;
	private List<BoardListener> listeners;
	private Piece playerPiece;
	private final MoveLog log;
	private Board chessBoard;
	private BoardDirection boardDirection;
	private CapturePanel capturesPanel;
	private HistoryPanel logPanel;
	private boolean highLightLegalMoves;
	private boolean bAi;
	private boolean wAi;
	private boolean moveMade = false;

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
		this.listeners = new ArrayList<BoardListener>();
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
		this.addBoardListener(new AiListener());

	}

	private void addBoardListener(BoardListener listener) {
	  this.listeners.add(listener);
		
	}
	
	private void notifyAllBoardListeners() {
		for(final BoardListener listener: listeners) {
			listener.moveMade();
		}
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

	private void MakeMove(final Move move) {
		final MoveTransition transition = chessBoard.curPlayer().makeMove(move);
		
		if(transition.getMoveStatus().isDone()) {
			chessBoard = transition.getToBoard();
			log.addMove(move);
			soundEffects.processInput("place");
			moveMade = true;
			notifyAllBoardListeners();
			boardPanel.drawBoard(chessBoard);
			return;
		}
		moveMade = false;
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
			this.setTransferHandler(new PanelTransferHandler());
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
		public void redraw() {
			SwingUtilities.invokeLater(() -> {
				logPanel.redo(chessBoard,log);
				capturesPanel.redo(log);
				boardPanel.drawBoard(chessBoard);
				soundEffects.processInput("stop");
				highLightLegalMoves(chessBoard);
			});
		}
		
		private void highLightLegalMoves(final Board board) {
			if(highLightLegalMoves) {
				for(final Move move: pieceLegalMoves(board)) {
					if(move.getDest() == this.tileId) {
						if(move.isAttack()) {
						setBorder(BorderFactory.createBevelBorder(0, Color.RED, Color.RED));
					}
					else {
						setBorder(BorderFactory.createBevelBorder(0,Color.GREEN,Color.GREEN));

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
				this.icon = new TileLabel(board.getTile(this.tileId).getPiece().getImg(),tileId);
			}else {
				this.icon = new TileLabel(null,tileId);
			}
			this.add(icon);
		}
		
		
		private void eventHandler() {

			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					// if it is a humans turn allow events
					if(SwingUtilities.isRightMouseButton(e)) {
						mouse.reset();
						srcTile = null;
						destTile = null;
					}else if(SwingUtilities.isLeftMouseButton(e)) {
						mouse.mouseLeftClicked(e, tileId);
						if(srcTile == null) {
							if(mouse.getSrc() != -1) {
		
							}
							srcTile = chessBoard.getTile(tileId);
							playerPiece = srcTile.getPiece();
							if(playerPiece == null) {
								srcTile = null;
								mouse.reset();
							}
							}else {
							destTile = chessBoard.getTile(tileId);
							final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), destTile.getCoord());
							MakeMove(move);
							srcTile = null;
							destTile = null;
							playerPiece = null;	
							mouse.reset();
						}
						redraw();
					}
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
			        if(!highLightLegalMoves) {
			        	setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
			        }
					
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					if(!highLightLegalMoves) {
						setBorder( BorderFactory.createEmptyBorder());
					}
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
		
		
		
		public class PanelTransferHandler extends TransferHandler {
			private static final DataFlavor SUPPORTED_FLAVOR = DataFlavor.imageFlavor;
			
			public PanelTransferHandler() {
				
			}
			
			@Override
			public boolean canImport(TransferHandler.TransferSupport info) {
				if(!info.isDrop() || !info.isDataFlavorSupported(SUPPORTED_FLAVOR)) {
					return false;
				}
				
				
				TilePanel temp = (TilePanel)info.getComponent();
				int tempId = temp.getTileId();
				
				if(chessBoard.getTile(tempId).isOccupied() && chessBoard.getTile(tempId).getPiece().piece_alliance() != chessBoard.curPlayer().getAlliance()) {
					return false;
				}
				if(srcTile == null && chessBoard.getTile(tempId).isOccupied()) {
					if(chessBoard.getTile(tempId).getPiece().piece_alliance() == chessBoard.curPlayer().getAlliance()) {
						srcTile = chessBoard.getTile(tempId);
						if(srcTile.getPiece().piece_alliance() != chessBoard.curPlayer().getAlliance()) {
							return false;
						}
					}
				}else {
					
					if(destTile!= null) {

						final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), destTile.getCoord());
						final MoveTransition transition = chessBoard.curPlayer().makeMove(move);
						if(transition.getMoveStatus().isDone()) {
							return true;
						}else {
							return false;
						}
					}
				

					
				}
				
				return true;	
			}
			
			@Override
			public boolean importData(TransferHandler.TransferSupport support) {
				boolean success = false;
				TilePanel temp = (TilePanel)support.getComponent();
				if(temp != null) {
					destTile = chessBoard.getTile(temp.getTileId());
				}
				
				if(canImport(support)) {
					try {
						Transferable t = support.getTransferable();
						Object value = t.getTransferData(SUPPORTED_FLAVOR);
						if(value instanceof ImageIcon  && destTile != null && srcTile != null) {
							final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), destTile.getCoord());
							MakeMove(move);
							srcTile = null;
							destTile = null;
							success = true;
							redraw();
						}

					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				return success;
			}
			
		}

		
		public class TileLabel extends JLabel{
			private int tileId;
			
			

			public TileLabel(final ImageIcon icon,final int pos) {
				if(icon != null) {
					this.setIcon(icon);
					this.tileId = pos;
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
			     
			     eventHandler();
			}
			
			
			public ImageIcon getImage() {
				return (ImageIcon)this.getIcon();
			}
			
			
			private void eventHandler() {

				addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(final MouseEvent e) {
						// if it is a humans turn allow events
						if(SwingUtilities.isRightMouseButton(e)) {
							mouse.reset();
							srcTile = null;
							destTile = null;
						}else if(SwingUtilities.isLeftMouseButton(e)) {
							mouse.mouseLeftClicked(e, tileId);
							if(srcTile == null) {
								if(mouse.getSrc() != -1) {
			
								}
								srcTile = chessBoard.getTile(tileId);
								playerPiece = srcTile.getPiece();
								if(playerPiece == null) {
									srcTile = null;
									mouse.reset();
								}
								}else {
									System.out.println("src is set to :" + srcTile.toString());
								destTile = chessBoard.getTile(tileId);
								final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), destTile.getCoord());
								MakeMove(move);
								srcTile = null;
								destTile = null;
								playerPiece = null;	
								mouse.reset();
							}
							redraw();
						}
					}

					@Override
					public void mousePressed(final MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(final MouseEvent e) {
						
					}

					@Override
					public void mouseEntered(final MouseEvent e) {
						
					}

					@Override
					public void mouseExited(final MouseEvent e) {
				
					}	
					
				});
			}
			
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
					
					
					TileLabel temp = (TileLabel)info.getComponent();
					
					int tempId = temp.getTileId();
					
					if(srcTile == null && chessBoard.getTile(tempId).isOccupied()) {
						if(chessBoard.getTile(tempId).getPiece().piece_alliance() == chessBoard.curPlayer().getAlliance()) {
							srcTile = chessBoard.getTile(tempId);
							if(srcTile.getPiece().piece_alliance() != chessBoard.curPlayer().getAlliance()) {
								return false;
							}
						}
					}else {
						if(destTile!= null) {
							final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), destTile.getCoord());
							final MoveTransition transition = chessBoard.curPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone()) {
								return true;
							}else {
								return false;
							}
						}
						
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
					
					TilePanel temp = (TilePanel)src.getParent();
                    if(temp != null && moveMade) {
					if(temp.getPiece() != null) {
						((TileLabel)src).setVisible(false);
						srcTile = chessBoard.getTile(temp.getTileId());
						((TileLabel)src).getParent().remove((TileLabel)src);
						srcTile = null;
						destTile = null;
					}
                    }
                    SwingUtilities.invokeLater(() -> {
						logPanel.redo(chessBoard,log);
						capturesPanel.redo(log);
						boardPanel.drawBoard(chessBoard);
					});
				}
				
				@Override
				public boolean importData(TransferHandler.TransferSupport support) {
					boolean success = false;
					TilePanel temp = (TilePanel)support.getComponent().getParent();
					if(temp != null) {
							destTile = chessBoard.getTile(temp.getTileId());
					}
					if(canImport(support)) {
						try {
							Transferable t = support.getTransferable();
							Object value = t.getTransferData(SUPPORTED_FLAVOR);
							if(value instanceof ImageIcon  && destTile != null && srcTile != null) {
								final Move move = MoveFactory.createMove(chessBoard, srcTile.getCoord(), destTile.getCoord());
								MakeMove(move);
									moveMade = false;
									srcTile = null;
									destTile = null;
								
								success = true;
								redraw();

							}else {
								destTile = null;
							}
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					return success;
				}
			}

			public int getTileId() {
				return this.tileId;
			}

		}
		
	}
	
	public class AiListener implements BoardListener{

		@Override
		public void moveMade() {
			if(wAi && chessBoard.curPlayer().getAlliance().isWhite()) {
				final Brain brain = new Brain();
				brain.execute();
			}else if(bAi && chessBoard.curPlayer().getAlliance().isBlack()) {
				final Brain brain = new Brain();
				brain.execute();
				
			}
		}
	}
	
	public class Brain extends SwingWorker<Move,String> {

		@Override
		protected Move doInBackground() throws Exception {
			System.out.println("Call to do in background");
			final MiniMax strategy = new MiniMax(4,chessBoard,0,0);
			
			return strategy.excecute(chessBoard);
		}
		
		@Override
		protected void done() {
			System.out.println("Call to do done");
			try {
				final Move best = get();
				System.out.println("making move");
				MakeMove(best);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
}
