package com.chess.engine.peices;

import java.util.Collection;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected final int position;
	protected final Alliance owner;
	protected final boolean firstMove;
	protected final PieceType type;
	
	Piece(final int pos, final Alliance owner,final PieceType type){
		this.position = pos;
		this.owner = owner;
		this.firstMove = true;
		this.type = type;
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
