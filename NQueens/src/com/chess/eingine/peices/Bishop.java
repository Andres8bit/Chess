package com.chess.engine.peices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Bishop extends Piece{
	private final static int[] POSSIBLE_MOVES = {-9,-7,7,9};

	public Bishop(final int pos,final Alliance owner) {
		super(pos, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return Piece.PieceType.BISHOP.toString();
	}
	
	@Override
	public Collection<Move> legalMoves(final Board board) {
	final List<Move> legal_moves = new ArrayList<>();
	for(final int offset: POSSIBLE_MOVES) {
		int canidate = this.position;
		while(BoardUtils.isValidTile(canidate)) {
			if(firstColumn(canidate,offset) ||
				eighthColumn(canidate,offset))
				break;
			canidate += offset;
			if(BoardUtils.isValidTile(canidate)) {
				final Tile dest = board.getTile(canidate);
				if(!dest.isOccupied()) {
					legal_moves.add(new MajorMove(board, this, canidate));
				}else {
					final Piece occupier = dest.getPiece();
					final Alliance owner = occupier.piece_alliance();
					if(this.owner != owner) {
						legal_moves.add(new AttackMove(board, this, canidate, occupier));
					}
					break;
				}
			}
		}
		
	}
		return ImmutableList.copyOf(legal_moves);
	}
	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN [coord] && (offset == -9 || offset == 7);	
	}
	
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.EIGHTH_COLUMN[coord] && (offset == -7 || offset == 9);
	}

}
