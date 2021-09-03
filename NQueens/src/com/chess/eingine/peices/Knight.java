package com.chess.engine.peices;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Knight extends Piece{
	private final static int[] POSSIBLE_MOVES = {-17,-15,-10,-6,6,10,15,17};
	public Knight(final int pos,final Alliance owner) {
		super(pos, owner,PieceType.KNIGHT);
	}

	
	@Override
	public String toString() {
		return Piece.PieceType.KNIGHT.toString();
	}
	
	@Override
	public Collection<Move> legalMoves(final Board board) {
	int canidate;
	final List<Move>legal_move = new ArrayList<>();
		for(final int cur: POSSIBLE_MOVES) {
			canidate = this.position + cur;
			if(BoardUtils.isValidTile(canidate)) {
				
				if(firstColumn(this.position,cur) 
				   || secondColumn(this.position,cur)
				   || seventhColumn(this.position,cur)
				   || eighthColumn(this.position,cur))
					continue;
				
				final Tile curTile = board.getTile(canidate);
				if(!curTile.isOccupied()) {
					legal_move.add(new MajorMove(board,this,canidate));
				}else {
					final Piece occupier = curTile.getPiece();
					final Alliance piece_owner = occupier.piece_alliance();
					if(this.owner != piece_owner) {
						legal_move.add(new AttackMove(board, this, canidate, occupier));
					}
				}
			}
		}
		return ImmutableList.copyOf(legal_move);
	}
	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN [coord] && (offset == -17 || offset == -10 
				||  offset == 6 || offset == 15);	
	}
	
	private static boolean secondColumn(final int coord, final int offset) {
		return BoardUtils.SECOND_COLUMN[coord] && (offset == -10 || offset == 6);
	}
	
	private static boolean seventhColumn(final int coord, final int offset) {
		return BoardUtils.SEVENTH_COLUMN [coord] && (offset == -6 || offset == 10);
	}
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.EIGHTH_COLUMN[coord] && (offset == -15 || offset == -6 
				|| offset == 10 || offset == 17);
	}
}
