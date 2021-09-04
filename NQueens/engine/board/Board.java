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
	
	private Board(Builder builder) {
		this.board = createBoard(builder);
		this.wPieces = activePieces(this.board,Alliance.WHITE);
		this.bPieces = activePieces(this.board,Alliance.BLACK);
		
		final Collection<Move> wLegalMoves = legalMoves(this.wPieces);
		final Collection<Move> bLegalMoves = legalMoves(this.bPieces);
		
		this.wPlayer = new WPlayer(this,wLegalMoves, bLegalMoves);
		this.bPlayer = new BPlayer(this, wLegalMoves,bLegalMoves);
		this.curPlayer = builder.nextMove.choosePlayer(this.wPlayer,this.bPlayer);
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
	
	private static String prettyPrint(Tile tile) {
		if(tile.isOccupied()) {
			return tile.getPiece().piece_alliance().isBlack() ? tile.toString().toLowerCase():
				   tile.toString();
		}
		return tile.toString();
	}

	private Collection<Move> legalMoves(Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		for(final Piece piece: pieces) {
			legalMoves.addAll(piece.legalMoves(this));
		} 
		return ImmutableList.copyOf(legalMoves);
	}

	private static Collection<Piece> activePieces(List<Tile> board, Alliance owner) {
		final List<Piece> active = new ArrayList<>();
		for(final Tile tile: board) {
			if(tile.isOccupied()) {
				final Piece piece= tile.getPiece();
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
	
	public static class Builder{
		Map<Integer,Piece>boardConfig;
		Alliance nextMove;
		
		public Builder() {
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.pos(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance owner) {
			this.nextMove = owner;
			return this;
		}
		
		
		public Board build() {
			return new Board(this);
		}
		
	}

	public Iterable<Move> getAllLegalMoves() {
		return  Iterables.unmodifiableIterable(Iterables.concat(this.wPlayer.getLegalMoves(),this.bPlayer.getLegalMoves()));
	}
}
