package com.chess.engine.peices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece{
	private final static int[] POSSIBLE_MOVES = {8,16,7,9};
	
	public Pawn(final int pos, final Alliance owner) {
		super(pos, owner,PieceType.PAWN);
	}
	
	@Override
	public String toString() {
		return Piece.PieceType.PAWN.toString();
	}
	

	@Override
	public Collection<Move> legalMoves(final Board board) {
		final List<Move> legal_moves = new ArrayList<>();
		
		for(final int offset:POSSIBLE_MOVES) {
			final int canidate = this.position + this.owner.direction() * offset;
		 
			if(!BoardUtils.isValidTile(canidate)) {
				continue;
			}
			if(offset == 8 && !board.getTile(canidate).isOccupied()) {
					//promotions
					legal_moves.add(new MajorMove(board,this,canidate));
			}else if(offset == 16 && this.firstMove && (BoardUtils.SECOND_ROW[this.position] && this.owner.isBlack())|| (BoardUtils.SEVENTH_ROW[this.position] && this.owner.isWhite()))
				{
					final int behind = this.position + this.owner.direction() * 8;
					if(!board.getTile(behind).isOccupied() &&  !board.getTile(canidate).isOccupied()) 
					 {
						legal_moves.add(new MajorMove(board,this,canidate));	
					 }
				}else if(offset == 7
						 && !((BoardUtils.EIGHTH_COLUMN[this.position] && this.owner.isWhite())
						 || (BoardUtils.FIRST_COLUMN[this.position] & this.owner.isBlack())))
				{
						if(board.getTile(canidate).isOccupied())
						{
							final Piece occupier = board.getTile(canidate).getPiece();
							if(this.owner != occupier.owner)
							{
								legal_moves.add(new MajorMove(board,this,canidate));
							}
						}
					
				}else if(offset == 9
						 && !((BoardUtils.FIRST_COLUMN[this.position] && this.owner.isWhite())
								 || (BoardUtils.EIGHTH_COLUMN[this.position] & this.owner.isBlack())))
				{ 
					final Piece occupier = board.getTile(canidate).getPiece();
					if(this.owner != occupier.owner) 
					{
						legal_moves.add(new MajorMove(board,this,canidate));
					}
			    }
		}
		return ImmutableList.copyOf(legal_moves);
	}

}
