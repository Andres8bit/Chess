package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.peices.Pawn;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Rook;


public abstract class Move {
 protected final Board board;
 protected final Piece piece;
 protected final int dest;
 protected final boolean firstMove;
 public static final Move NULL_MOVE = new NullMove();
  
 
 private Move(final Board board, final Piece to_move, final int destination ){
	this.board = board;
	this.piece = to_move;
	this.dest = destination;
	this.firstMove = piece.isFirstMove();
  }
 
 private Move(final Board board, final int destination ){
	this.board = board;
	this.piece = null;
	this.dest = destination;
	this.firstMove = false;
  }
  
 @Override
 public String toString() {
	 if(this.piece != null) {
		 return  piece.toString() + BoardUtils.getPos(dest);
	 }
	 return "";
}
 
  @Override
  public int hashCode() {
	final int prime = 31;
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

  public Board getBoard() {
		return this.board;
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
	  
      final Board.Builder builder = new Builder();
      this.board.curPlayer().getActivePieces().stream().filter(piece -> !this.piece.equals(piece)).forEach(builder::setPiece);
      this.board.curPlayer().opponent().getActivePieces().forEach(builder::setPiece);
      
      builder.setPiece(this.piece.movePiece(this));
      builder.setMoveMaker(this.board.curPlayer().opponent().getAlliance());
      builder.setMoveTransition(this);
      return builder.build();
	}


  public static class MajorAttackMove extends AttackMove{
	public MajorAttackMove(final Board board, final Piece piece, final int dest, final Piece other) {
		super(board, piece,dest,other);
	}
	
	@Override
	public boolean equals(final Object other) {
		return this == other || other instanceof MajorAttackMove && super.equals(other);
	}
	  
	@Override
	public String toString() {
		return piece.toString() + "x" + 
	            BoardUtils.getPos(this.dest);
	}
  }
  
  public static class MajorMove extends Move {

      public MajorMove(final Board board,
                       final Piece pieceMoved,
                       final int destinationCoordinate) {
          super(board, pieceMoved, destinationCoordinate);
      }

      @Override
      public boolean equals(final Object other) {
          return this == other || other instanceof MajorMove && super.equals(other);
      }

  }
  
public static class AttackMove extends Move{
		final Piece attackPiece;

	public	AttackMove(final Board board,final  Piece to_move, final int destination, final Piece attack) {
			super(board, to_move, destination);
			this.attackPiece = attack;
	}
	
	 @Override
	 public String toString() {
		 return BoardUtils.getPos(this.piece.pos()).substring(0,1) + "x" + 
	            BoardUtils.getPos(this.dest);
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
	 
	 @Override
	 public boolean equals(final Object other) {
		 return this == other || other instanceof PawnMove && super.equals(other);
	 }
}

 public static  class PawnAttackMove extends AttackMove{
	 public PawnAttackMove(final Board board, final Piece to_move, final int destination, final Piece attack ) {
		 super(board,to_move,destination,attack);
	 }
	 
	 @Override
	 public boolean equals(final Object other) {
		 return this == other || other instanceof  PawnAttackMove && super.equals(other);
	 }
	 
	 @Override
	 public String toString() {
		 return BoardUtils.getPos(this.piece.pos()).substring(0,1) + "x" + 
	            BoardUtils.getPos(this.dest);
	 }
 }

 
 public static final class PawnEnPassantAttackMove extends AttackMove{
	 public PawnEnPassantAttackMove(final Board board, final Piece to_move, final int destination, final Piece attack ) {
		 super(board,to_move,destination,attack);
	 }
	 
	 @Override
	 public boolean isAttack() {
		 return true;
	 }
	 
	 @Override
	 public boolean equals(final Object other) {
		 return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other); 
	 }
	 
	 @Override
	 public String toString() {
		 return BoardUtils.getPos(this.piece.pos()).substring(0,1) + "x" +
	            BoardUtils.getPos(this.dest); 
	 }
	 
     @Override
     public Board execute() {
         final Board.Builder builder = new Builder();
         this.board.curPlayer().getActivePieces().stream().filter(piece -> !this.piece.equals(piece)).forEach(builder::setPiece);
         this.board.curPlayer().opponent().getActivePieces().stream().filter(piece -> !piece.equals(this.getAttackPiece())).forEach(builder::setPiece);
         
         builder.setPiece(this.piece.movePiece(this));
         builder.setMoveMaker(this.board.curPlayer().opponent().getAlliance());
         builder.setMoveTransition(this);
         
         return builder.build();
     }
	 
 }
 
 
 public static class PawnPromotion extends Move{
	 final Move decoratedMove;
	 final Pawn promoted;
	 
	 public PawnPromotion(final Move decorateMove){
		 super(decorateMove.getBoard(),decorateMove.getPiece(),decorateMove.getDest());
		 this.decoratedMove = decorateMove;
		 this.promoted = (Pawn)decorateMove.getPiece();
	 }
	 
	 @Override
	 public int hashCode() {
		 return this.decoratedMove.hashCode() +( 31 * promoted.hashCode());
	 }
	 
	 @Override
	 public boolean equals(final Object other) {
		 return this == other || other instanceof PawnPromotion && this.decoratedMove.equals(other);
	 }
	 
	 @Override
	 public Board execute() {
		 
		 final Board board = this.decoratedMove.execute();
		 final Builder builder = new Builder();
		 for(final Piece piece: board.curPlayer().getActivePieces()) {
			 if(!this.promoted.equals(piece)) {
				 builder.setPiece(piece);
			 }
		 }
		 
		 for(final Piece piece: board.curPlayer().opponent().getActivePieces()) {
				 builder.setPiece(piece);
		 }
		 
		 builder.setPiece((this.promoted.promote().movePiece(this)));
		 builder.setMoveMaker(board.curPlayer().getAlliance());
		 
		 return builder.build();
	 }
	 
	 @Override
	 public boolean isAttack() {
		 return this.decoratedMove.isAttack();
	 }
	 
	 @Override
	 public Piece getAttackPiece() {
		 return this.decoratedMove.getAttackPiece();
	 }
	 
	 @Override
	 public String toString() {
		 return "";
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
			 if(!this.piece.equals(piece)) {
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
	 
	 @Override
	 public String toString() {
		 return BoardUtils.getPos(this.piece.pos());
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.rook.hashCode();
		result = prime * this.rookDest;
		
		return result;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof CastleMove)) {
			return false;
		}
		
		final CastleMove otherMove = (CastleMove)other;
		return super.equals(otherMove) && this.rook.equals(otherMove.getCastleRook());
		
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
			
			@Override
			public boolean equals(final Object other) {
				return this == other || other instanceof KingSideCastleMove && super.equals(other);
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
	
	@Override
	public boolean equals(final Object other) {
		return this == other || other instanceof QueenSideCastleMove && super.equals(other);
	}

}

public static final class NullMove extends Move{

	public NullMove() {
		super(null,-1);
	}

	@Override
	public Board execute() {
		throw new RuntimeException("Cannot execute null move.");
	}
	
	@Override
	public int getCur() {
		return -1;
	}

}

public static class MoveFactory {
 private MoveFactory() {
	throw new RuntimeException("Cannot Instantiate");
 }

 public static Move getNullMove() {
     return NULL_MOVE;
 }
 
 public static Move createMove(final Board board, final int curPos, final int destPos) {

	 for(final Move move: board.getAllLegalMoves()) {
		 if(move.getCur() == curPos && move.getDest() == destPos) {
			 //.out.println("move created");
			 return move;
		 }
	 }
	// System.out.println("Move not found");
	 return NULL_MOVE;
 }
}


}