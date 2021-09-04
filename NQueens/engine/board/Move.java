package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.peices.Piece;


public abstract class Move {
 final Board board;
 final Piece piece;
 final int dest;
 
 public static final Move NULL_MOVE = new NullMove();
  private Move(final Board board, final Piece to_move, final int destination ){
	this.board = board;
	this.piece = to_move;
	this.dest = destination;
  }
 
  public int getCur() {
	 return piece.pos();
  }
 
  public int getDest() {
		return dest;
  }
 
  public Piece getPiece() {
	  return piece;
  }
  
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

 
public static class AttackMove extends Move{
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
	
 public static final class PawnMove extends Move{
	 public PawnMove(final Board board, final Piece to_move, final int destination ) {
		 super(board,to_move,destination);
	 }
 }
 
 public static  class PawnAttackMove extends AttackMove{
	 public PawnAttackMove(final Board board, final Piece to_move, final int destination, final Piece attack ) {
		 super(board,to_move,destination,attack);
	 }
 }
 
 public static final class PawnEnPassanMove extends AttackMove{
	 public PawnEnPassanMove(final Board board, final Piece to_move, final int destination, final Piece attack ) {
		 super(board,to_move,destination,attack);
	 }
 }
 
 public static final class PawnJumpMove extends Move{
	 public PawnJumpMove(final Board board, final Piece to_move, final int destination ) {
		 super(board,to_move,destination);
	 }
 }
 
 static abstract class CastleMove extends Move{

	public CastleMove(final Board board,final  Piece to_move,final int destination) {
		super(board, to_move, destination);
	}
 
 }
public static final class KingSideCastleMove extends CastleMove{

			public KingSideCastleMove(final Board board,final  Piece to_move,final int destination) {
				super(board, to_move, destination);
			}
 }

public static final class QueenSideCastleMove extends CastleMove{

	public QueenSideCastleMove(final Board board,final  Piece to_move,final int destination) {
		super(board, to_move, destination);
	}

}

public static final class NullMove extends Move{

	public NullMove() {
		super(null,null, -1);
	}
	
	@Override
	public Board execute() {
		throw new RuntimeException("Cannot execute null move.");
	}

}

public static class MoveFactory {
 private MoveFactory() {
	throw new RuntimeException("Cannot Instantiate");
 }
 
 public static Move createMove(final Board board, final int curPos, final int destPos) {
	 
	 for(final Move move: board.getAllLegalMoves()) {
		 if(move.getCur() == curPos && move.getDest() == destPos) {
			 return move;
		 }
	 }
	 return NULL_MOVE;
 }
}
}
