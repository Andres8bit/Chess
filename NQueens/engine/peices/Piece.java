package com.chess.engine.peices;

import java.util.Collection;

import javax.swing.ImageIcon;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected final int position;
	protected final Alliance owner;
	protected final boolean firstMove;
	protected final PieceType type;
	private final int cachedHashCode;
	
	Piece(final int pos, final Alliance owner,final PieceType type,final boolean firstMove){
		this.position = pos;
		this.owner = owner;
		this.firstMove = firstMove;
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
	
	public int getVal() {
		return this.type.val();
	}
	
	public abstract ImageIcon getImg();
	public abstract Collection<Move> legalMoves(final Board board);
	public abstract Piece movePiece(final Move move);

	public enum PieceType{
		PAWN("p", 100),
		KNIGHT("n",300),
		BISHOP("b",300),
		ROOK("r",500),
		QUEEN("q",900),
		KING("k",1000);
		
		private String pieceName;
		private int val;
		PieceType(final String name,final int value){
			this.pieceName = name;
			this.val = value;
		}
		
		public int val() {
			return this.val;
		}

		@Override
		public String toString() {
			return  this.pieceName;
		}
	}
}
