package com.chess.engine.peices;

import java.util.Collection;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected final int position;
	protected final Alliance owner;
	protected final boolean firstMove;
	
	Piece(final int pos, final Alliance owner){
		this.position = pos;
		this.owner = owner;
		this.firstMove = true;
	}
	
	public Alliance piece_alliance() {
		return owner;
	}
	
	public boolean isFirstMove() {
		return firstMove;
	} 
	
	public abstract Collection<Move> LegalMoves(final Board board);

}
