package com.chess.engine.peices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

public class Rook extends Piece{
	private final static int[] POSSIBLE_MOVES = {-8,-1,1,8};
	
	public Rook(final int pos, final Alliance owner, final Boolean first) {
		super(pos,owner,PieceType.ROOK,first);
	}
	
	public Rook(final int pos,final  Alliance owner) {
		super(pos, owner,PieceType.ROOK,true);
	}
	

	@Override
	public String toString() {
		return Piece.PieceType.ROOK.toString();
	}
	
	@Override
	public Collection<Move> legalMoves(final Board board) {
		final List<Move> legal_moves = new ArrayList<>();
		for(final int offset: POSSIBLE_MOVES) {
			int canidate = this.position;
			while(BoardUtils.isValidTile(canidate)) {
				if(firstColumn(canidate,offset) || eighthColumn(canidate,offset)) {
					break;
				}
				
				canidate += offset;
				if(BoardUtils.isValidTile(canidate)) {
					final Piece piece = board.getPiece(canidate);
					if(piece == null) {
						legal_moves.add(new MajorMove(board, this, canidate));
					}else {
						final Alliance owner = piece.piece_alliance();
						if(this.owner != owner) {
							legal_moves.add(new AttackMove(board, this, canidate, piece));
						}
						break;
					}
				}
			}
			
		}
			return ImmutableList.copyOf(legal_moves);
	}
	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN.get(coord) && offset == -1;	
	}
	
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.EIGHTH_COLUMN.get(coord)&& offset == 1;
	}

	@Override
	public Rook movePiece(final Move move) {
		return new Rook(move.getDest(),move.getPiece().piece_alliance(),false);
	}

}
