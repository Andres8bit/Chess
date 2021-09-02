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
import com.google.common.collect.ImmutableList;

public class Board {
	private final List<Tile> board;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < BoardUtils.NUM_TILES;i++) {
			final String tileText = this.board.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i+1)%BoardUtils.NUM_TILES == 0)
				builder.append("\n");
		}
		return builder.toString();
		
	}
	
	private Board(final Builder builder) {
		this.board = createGameBoard(builder);
		this.whitePieces = activePieces(this.board,Alliance.WHITE);
		this.blackPieces = activePieces(this.board,Alliance.BLACK);
		final Collection<Move> wInitLegalMoves = legalMoves(this.whitePieces);
		final Collection<Move> bInitLegalMoves = legalMoves(this.blackPieces);
	}
	
	private Collection<Move> legalMoves(Collection<Piece> pieces) {
		final Collection<Move> moves = new ArrayList();
		
		for(final Piece piece: pieces) {
			moves.addAll(piece.legalMoves(this));
		}
		
		return ImmutableList.copyOf(moves);
	}

	private Collection<Piece> activePieces(final List<Tile> board, final Alliance owner) {
		final  List<Piece> active = new ArrayList<>();
		for(final Tile tile : board) {
			if(tile.isOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.piece_alliance() == owner)
					active.add(piece);
			}
		}
		return ImmutableList.copyOf(active);
	}
	
	public static List<Tile> createGameBoard(final Builder builder){
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for(int i = 0; i < BoardUtils.NUM_TILES;i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return ImmutableList.copyOf(tiles);
	}
    

	public Tile getTile(final int coord) {
		return board.get(coord);
	}
	
	public static Board createStandardBoard() {
		final Builder builder = new Builder();
		
		// Black Set 
		builder.setPiece(new Rook(0,Alliance.BLACK));
		builder.setPiece(new Knight(1,Alliance.BLACK));
		builder.setPiece(new Bishop(2,Alliance.BLACK));
		builder.setPiece(new Queen(3,Alliance.BLACK));
		builder.setPiece(new King(4,Alliance.BLACK));
		builder.setPiece(new Bishop(5,Alliance.BLACK));
		builder.setPiece(new Knight(6,Alliance.BLACK));
		builder.setPiece(new Rook(7,Alliance.BLACK));
		builder.setPiece(new Pawn(8,Alliance.BLACK));
		builder.setPiece(new Pawn(9,Alliance.BLACK));
		builder.setPiece(new Pawn(10,Alliance.BLACK));
		builder.setPiece(new Pawn(11,Alliance.BLACK));
		builder.setPiece(new Pawn(12,Alliance.BLACK));
		builder.setPiece(new Pawn(13,Alliance.BLACK));
		builder.setPiece(new Pawn(14,Alliance.BLACK));
		builder.setPiece(new Pawn(15,Alliance.BLACK));
		
		// White Set
		builder.setPiece(new Pawn(48,Alliance.BLACK));
		builder.setPiece(new Pawn(49,Alliance.BLACK));
		builder.setPiece(new Pawn(50,Alliance.BLACK));
		builder.setPiece(new Pawn(51,Alliance.BLACK));
		builder.setPiece(new Pawn(52,Alliance.BLACK));
		builder.setPiece(new Pawn(53,Alliance.BLACK));
		builder.setPiece(new Pawn(54,Alliance.BLACK));
		builder.setPiece(new Pawn(55,Alliance.BLACK));
		builder.setPiece(new Rook(56,Alliance.BLACK));
		builder.setPiece(new Knight(57,Alliance.BLACK));
		builder.setPiece(new Bishop(58,Alliance.BLACK));
		builder.setPiece(new Queen(59,Alliance.BLACK));
		builder.setPiece(new King(60,Alliance.BLACK));
		builder.setPiece(new Bishop(61,Alliance.BLACK));
		builder.setPiece(new Knight(62,Alliance.BLACK));
		builder.setPiece(new Rook(63,Alliance.BLACK));
		
		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	
	}
	
	public static class Builder{
		Map<Integer,Piece> boardConfig;
		Alliance nextMove;
		
		public Builder() {
			this.boardConfig = new HashMap<>();
		}
		
		public Board build() {
			return new Board(this);
		}
		

		public Builder setPiece(final Piece piece) {
			this.boardConfig.put( piece.pos(),piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance alliance) {
			this.nextMove = alliance;
			return this;
		}
	}

}
