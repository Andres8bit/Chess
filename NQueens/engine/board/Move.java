package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.peices.Piece;

public abstract class Move {
 final Board board;
 final Piece piece;
 final int dest;
 
 private Move(final Board board, final Piece to_move, final int destination ){
	this.board = board;
	this.piece = to_move;
	this.dest = destination;
 }
 
 public int getDest() {
		return dest;
  }
 
  public Piece getPiece() {
	  return piece;
  }
 public static final class MajorMove extends Move
 
 {

	public MajorMove(final Board board,final  Piece to_move,final int destination) {
		super(board, to_move, destination);
	}

	@Override
	public Board execute() {
	   final Builder builder = new Builder();
		// TODO hash code and equals for pieces
	   for(final Piece piece: this.board.curPlayer().getActivePieces()) {
			if(!this.piece.equals(piece)) {
				builder.setPiece(piece);
			}		
		}
		
	   for(final Piece piece: this.board.curPlayer().opponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		
	   // move the piece
	    builder.setPiece(this.piece.movePiece(this));
	    builder.setMoveMaker(this.board.curPlayer().opponent().getAlliance());
		
		return builder.build();
	}

 }
 
	public static final class AttackMove extends Move{
		final Piece attackPiece;

	public	AttackMove(final Board board,final  Piece to_move, final int destination, final Piece attack) {
			super(board, to_move, destination);
			this.attackPiece = attack;
		
		}

	@Override
	public Board execute() {
		// TODO Auto-generated method stub
		return null;
	}
	}

	public abstract Board execute();


 
}
