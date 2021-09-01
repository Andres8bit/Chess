package com.chess.engine.peices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class King extends Piece{
	private final static int[] POSSIBLE_MOVES = {-9,-8,-7,-1,1,7,8,9};
	King(final int pos, final Alliance owner) {
		super(pos, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> LegalMoves(Board board) {
	final List<Move> legal_moves =new ArrayList<>();

	for (final int offset:POSSIBLE_MOVES) {
		final int canidate = this.position + offset;
		
		if(firstColumn(this.position,offset)
		   ||eighthColumn(this.position,offset)) {
			continue;
		}
		if(BoardUtils.isValidTile(canidate)) {
			final Piece occupier = board.getTile(canidate).getPiece();
			final Alliance owner = occupier.owner;
			if(this.owner != owner) {
				legal_moves.add(new AttackMove(board,this,canidate,occupier));
			}
		}
		
		
	}
	return ImmutableList.copyOf(legal_moves);
	}

	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN [coord] && ( offset == 7 || offset == -9 || offset == -1);	
	}
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.SECOND_COLUMN[coord] && (offset ==-7 || offset == 9 || offset == 1);
	}
}
