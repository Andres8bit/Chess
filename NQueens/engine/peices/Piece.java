package com.chess.engine.peices;

import java.util.Collection;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected final int position;
	protected final Alliance owner;
	protected final boolean firstMove;
	protected final PieceType type;
	private final int cachedHashCode;
	
	Piece(final int pos, final Alliance owner,final PieceType type){
		this.position = pos;
		this.owner = owner;
		this.firstMove = true;
		this.type = type;
		this.cachedHashCode = computeHashCode();
		
	}
	
	private int computeHashCode() {
		int result = type.hashCode();
		result = 31 * result + owner.hashCode();
		result = 31 * result + position;
		result = 31 * result + (firstMove ?1:0);
		
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		
		if(!(other instanceof Piece)) {
			return false;
		}
		
		final Piece otherPiece = (Piece) other;
		return position == otherPiece.pos() && type == otherPiece.getType()
			   && owner == otherPiece.piece_alliance() && firstMove == otherPiece.isFirstMove();
		
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}
	
	public int pos() {
		return this.position;
	}
	
	public Alliance piece_alliance() {
		return owner;
	}
	
	public boolean isFirstMove() {
		return firstMove;
	} 
	
	public PieceType getType() {
		return this.type;
	}
	
	public abstract Collection<Move> legalMoves(final Board board);
	public abstract Piece movePiece(Move move);

	public enum PieceType{
		PAWN("P"),
		KNIGHT("N"),
		BISHOP("B"),
		ROOK("R"),
		QUEEN("Q"),
		KING("K");
		
		private String pieceName;
		PieceType(final String name){
			this.pieceName = name;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
	}
}
