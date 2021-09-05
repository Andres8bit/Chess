package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.peices.Pawn;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Rook;


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

  @Override
  public int hashCode() {
	final int prime = 3;
	int result = 1;

	result = prime * result + this.getDest();
	result = prime * result + this.piece.hashCode();

	return result;
  }

  @Override
  public boolean equals(final Object other) {
	  if(this == other) {
		  return true;
	  }
	  if(!(other instanceof Move )){
		  return false;
	  }

	  final Move otherMove = (Move) other;
	  return this.dest == otherMove.getDest()    &&
			 this.piece.equals(otherMove.getPiece());
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

  public boolean isAttack() {
	  return false;
  }

  public boolean isCastlingMove() {
	  return false;
  }

  public Piece getAttackPiece() {
	  return null;
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
	public int hashCode() {
		return this.attackPiece.hashCode() + super.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}

		if(!(other instanceof AttackMove)) {
			return false;
		}

		final AttackMove otherMove = (AttackMove) other;

		return this.attackPiece.equals(otherMove.getAttackPiece()) && super.equals(otherMove);

	}

	@Override
	public Board execute() {
		// TODO Auto-generated method stub
		return null;
	 }

	@Override
	public boolean isAttack() {
		return true;
	}

	@Override
	public Piece getAttackPiece() {
		return this.attackPiece;
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

	 @Override
	 public Board execute() {
		 final Builder builder = new Builder();

		 for(final Piece piece: this.board.curPlayer().getActivePieces()) {
			 if(!this.getPiece().equals(piece)) {
				 builder.setPiece(piece);
			 }
		 }

		 for(final Piece piece: this.board.curPlayer().opponent().getActivePieces()) {
			 builder.setPiece(piece);
		 }

		 final Pawn movedPawn = (Pawn)this.piece.movePiece(this);
		 builder.setPiece(movedPawn);
		 builder.setEnPassantPawn(movedPawn);
		 builder.setMoveMaker(this.board.curPlayer().opponent().getAlliance());
		 return builder.build();
	 }
 }

 static abstract class CastleMove extends Move{

	protected  Rook rook;
	protected  int rookStart;
	protected  int rookDest;

	public CastleMove(final Board board,final  Piece to_move,final int destination, final Rook castleRook, final int rookStart, final int rookDest) {
		super(board, to_move, destination);
		this.rook = castleRook;
		this.rookStart = rookStart;
		this.rookDest = rookDest;
	}

	public Rook getCastleRook() {
		return this.rook;
	}

	@Override
	public boolean isCastlingMove() {
		return true;
	}

	@Override
	public Board execute() {
		final Builder builder = new Builder();

		 for(final Piece piece: this.board.curPlayer().getActivePieces()) {
			 if(!this.getPiece().equals(piece) && !this.rook.equals(piece)) {
				 builder.setPiece(piece);
			 }
		 }

		 for(final Piece piece: this.board.curPlayer().opponent().getActivePieces()) {
			 builder.setPiece(piece);
		 }

		 builder.setPiece(this.piece.movePiece(this));
		 builder.setPiece(new Rook(this.rookDest,this.rook.piece_alliance()));
		 builder.setMoveMaker(this.board.curPlayer().opponent().getAlliance());

		 return builder.build();
	}
 }
public static final class KingSideCastleMove extends CastleMove{

			public KingSideCastleMove(final Board board,final  Piece to_move,final int destination, final Rook castleRook, final int rookStart, final int rookDest) {
				super(board, to_move, destination, castleRook,rookStart , rookDest);


			}

			@Override
			public String toString() {
				return "0-0";
			}
 }

public static final class QueenSideCastleMove extends CastleMove{

	public QueenSideCastleMove(final Board board,final  Piece to_move,final int destination, final Rook castleRook, final int rookStart, final int rookDest) {
		super(board, to_move, destination, castleRook,rookStart , rookDest);
	}

	@Override
	public String toString() {
		return "0-0-0";
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
