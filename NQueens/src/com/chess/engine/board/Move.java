package com.chess.engine.board;

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
 public static final class MajorMove extends Move
 
 {

	public MajorMove(final Board board,final  Piece to_move,final int destination) {
		super(board, to_move, destination);
	}

	@Override
	public Board execute() {
		// TODO Auto-generated method stub
		return null;
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
