package com.chess.engine.peices;

import java.util.Collection;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	protected final int position;
	protected final Alliance owner;
	
	Piece(final int pos, final Alliance owner){
		this.position = pos;
		this.owner = owner;
	}
	
	public Alliance piece_alliance() {
		return owner;
	}
	
	public abstract Collection<Move> LegalMoves(final Board board);

}
