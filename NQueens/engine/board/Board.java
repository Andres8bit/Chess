package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Bishop;
import com.chess.engine.peices.King;
import com.chess.engine.peices.Knight;
import com.chess.engine.peices.Pawn;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Queen;
import com.chess.engine.peices.Rook;
import com.chess.engine.player.BPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.player.WPlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public final class Board {
	private final List<Tile> board;
	private final Collection<Piece>wPieces;
	private final Collection<Piece>bPieces;
	private final WPlayer wPlayer;
	private final BPlayer bPlayer;
	private final Player curPlayer;
	private final Move transitionMove;
	private final Pawn enPassantPawn;
	private static int turnCount = 1;
	private static int lastCapture = 0;
	private static int lastPawnMove = 0;
	
	private Board(Builder builder) {
		this.board = createBoard(builder);
		this.wPieces = activePieces(this.board,Alliance.WHITE);
		this.bPieces = activePieces(this.board,Alliance.BLACK);
		
		final Collection<Move> wLegalMoves = legalMoves(this.wPieces);
		final Collection<Move> bLegalMoves = legalMoves(this.bPieces);
	
		this.enPassantPawn = builder.enPassantPawn;
		this.wPlayer = new WPlayer(this,wLegalMoves, bLegalMoves);
		this.bPlayer = new BPlayer(this, wLegalMoves,bLegalMoves);
		this.curPlayer = builder.nextMove.choosePlayer(this.wPlayer,this.bPlayer);
		this.transitionMove = builder.transitionMove != null ? builder.transitionMove : MoveFactory.getNullMove();

	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < BoardUtils.NUM_TILES;i++) {
			final String tileText = this.board.get(i).toString();//prettyPrint(this.board.get(i));
			builder.append(String.format("%3s", tileText));
			if(( i + 1) % BoardUtils.TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();	
	}
	
	public void resetLastPawnMoveCount() {
		lastPawnMove = 0;
		
	}
	
	public void resetLastCaptureCount() {
		lastCapture = 0;
	}
	
	public void resetTurnCount() {
		turnCount = 0;
		
	}

	public int getTurnCount() {
		return turnCount;
	}
	
	public Move getTransitionMove() {
	 return this.transitionMove;	
	}
	
	public Player curPlayer() {
		return this.curPlayer;
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.bPieces;
	}
	
	public Collection<Piece> getWhitePieces(){
		return this.wPieces;
	}
	
	public Player bPlayer() {
		return this.bPlayer;
	}
	
	public Player wPlayer() {
		return this.wPlayer;
	}
	
	public void incrementTurnCount() {
		turnCount++;
	}

	public void incrementLastCaptureCount() {
		lastCapture++;
	}
	
	public void incrementLastPawnMove() {
		lastPawnMove++;
	}
	
	public Iterable<Move> getAllLegalMoves() {
		return  Iterables.unmodifiableIterable(Iterables.concat(this.wPlayer.getLegalMoves(),this.bPlayer.getLegalMoves()));
	}

	public Piece getPiece(int coord) {
		return this.getTile(coord).getPiece();
	}

	public Iterable<Piece> getAllPieces() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.wPlayer.getActivePieces(),this.bPlayer.getActivePieces()));
	}

	public Pawn getEnPassant() {
		return this.enPassantPawn;
	}

	public boolean isFiftyMoveRule() {
		return lastCapture >= 50 && lastPawnMove >= 50;
	}

	private Collection<Move> legalMoves(Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		for(final Piece piece: pieces) {
			legalMoves.addAll(piece.legalMoves(this));
		} 
		
		return ImmutableList.copyOf(legalMoves);
	}

	private static Collection<Piece> activePieces(final List<Tile> board,final Alliance owner) {
		final List<Piece> active = new ArrayList<>();
		for(final Tile tile: board) {
			if(tile.isOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.piece_alliance() == owner) {
					active.add(piece);
				}
			}
		}
		return ImmutableList.copyOf(active);
	}

	public Tile getTile(final int coord) {
		return board.get(coord);
	}

	public Iterable<Tile> getAllTiles(){
		return Iterables.unmodifiableIterable(board);
	}
	
	private static List<Tile>createBoard(final Builder builder){
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		
		for(int i = 0; i < BoardUtils.NUM_TILES;i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		
		return ImmutableList.copyOf(tiles);
	}

	public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        return builder.build();
	}
	

	
	public Board createCustomBoard(final Collection<Piece> wActivePieces, final Collection<Piece> bActivePieces, final Alliance turnMaker ) {
		final Builder builder = new Builder();
		
		for(final Piece piece: wActivePieces) {
			builder.setPiece(piece);
		}
		
		for(final Piece piece: bActivePieces) {
			builder.setPiece(piece);
		}
		
		builder.setMoveMaker(turnMaker);
		
		return builder.build();
		
	}
	
	public static class Builder{
		Map<Integer,Piece>boardConfig;
		Alliance nextMove;
		Pawn enPassantPawn;
		Move transitionMove;
		
		public Builder() {
			this.boardConfig = new HashMap<>(32, 1.0f);
		}
		
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.pos(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance owner) {
			this.nextMove = owner;
			return this;
		}
		
		
		public Board createCustomBoard(final Collection<Piece> wActivePieces, final Collection<Piece> bActivePieces, final Alliance turnMaker ) {
			
			if(this.boardConfig != null) {
				this.boardConfig.clear();
			}
			
			for(final Piece piece: wActivePieces) {
				this.boardConfig.put(piece.pos(), piece);
			}
			
			for(final Piece piece: bActivePieces) {
				this.boardConfig.put(piece.pos(), piece);
			}
			
			this.setMoveMaker(turnMaker);
			
			return new Board(this);
			
		}
		
		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(final Pawn pawn) {
			this.enPassantPawn = pawn;
			
		}

        public Builder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }
		
	}
}
