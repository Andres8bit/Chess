package com.chess.engine.peices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public  class Queen extends Piece {
	private final static int[] POSSIBLE_MOVES_VECTOR = {-9,-7,7,9};
	Queen(int pos, Alliance owner) {
		super(pos, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> LegalMoves(Board board) {
		final List<Move> Moves = new ArrayList<>();
		
		
		for(final int offset: POSSIBLE_MOVES_VECTOR) {
			int init_pos = this.position;
			
			while(BoardUtils.ValidTileCoordinate(init_pos)) {
				
				
			}
		
		}
		
	}
}
