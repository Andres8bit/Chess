package com.chess.engine.peices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnJumpMove;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.peices.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece{
	private final static int[] POSSIBLE_MOVES = {8,16,7,9};
	
	public Pawn(final int pos, final Alliance owner, final Boolean first) {
		super(pos,owner,PieceType.PAWN,first);
	}
	
	public Pawn(final int pos, final Alliance owner) {
		super(pos, owner,PieceType.PAWN,true);
	}
	
	@Override
	public String toString() {
		return Piece.PieceType.PAWN.toString();// + owner.toString();
	}
	

	@Override
	public Collection<Move> legalMoves(final Board board) {
		final List<Move> legal_moves = new ArrayList<>();
	
		for(final int offset: POSSIBLE_MOVES) {
			final int candidate = this.position + (this.owner.direction() * offset);
			
			if(!BoardUtils.isValidTile(candidate)) {
				continue;
			}
			
			if(offset == 8 && board.getPiece(candidate) == null) {
				legal_moves.add(new MajorMove(board,this,candidate));
			
			}else if( offset == 16 && this.isFirstMove() && ((BoardUtils.SEVENTH_ROW.get(this.position) && this.piece_alliance().isBlack())
					|| (BoardUtils.SECOND_ROW.get(this.position) && this.piece_alliance().isWhite()))) {
				final int behind = this.position + (this.owner.direction() * 8);
				
				if(board.getPiece(behind) == null && board.getPiece(candidate) == null) {
					legal_moves.add(new PawnJumpMove(board,this,candidate));
				}
				
			}else if(offset == 7 &&
					!((BoardUtils.EIGHTH_COLUMN.get(this.position) && this.owner.isWhite()
					|| (BoardUtils.FIRST_COLUMN.get(this.position)&& this.owner.isBlack())))) {
					if(board.getPiece(candidate) != null) {
						final Piece piece = board.getPiece(candidate);
					
						if(this.owner != piece.piece_alliance()) {
			
							legal_moves.add(new MajorMove(board,this,candidate));
						}
					}
			}else if(offset == 9 &&
					!((BoardUtils.FIRST_COLUMN.get(this.position) && this.owner.isWhite()
					|| (BoardUtils.EIGHTH_COLUMN.get(this.position)&& this.owner.isBlack())))) {
				
				if(board.getPiece(candidate) != null) {
					final Piece piece = board.getPiece(candidate);
					if(this.owner != piece.piece_alliance()) {
						legal_moves.add(new MajorMove(board,this,candidate));
					}
				}
			}
		}
		return ImmutableList.copyOf(legal_moves);
	}

	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn(move.getDest(),move.getPiece().piece_alliance(),false);
	}
}
