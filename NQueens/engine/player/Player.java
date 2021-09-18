package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.King;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Piece.PieceType;
import com.chess.engine.player.AI.MiniMax;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import eventHandling.BoardListener;

public abstract class Player implements BoardListener {
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean checked;
	private MiniMax brain;
	
	Player(final Board board, final Collection<Move> legalMoves,
			final Collection<Move> opponentMoves){
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastle(legalMoves,opponentMoves)));
		this.checked = !Player.AttacksOnTile(this.playerKing.pos(),opponentMoves).isEmpty();
		this.brain = new MiniMax(2,board,0,0);
	}

	public static Collection<Move> AttacksOnTile(int pos, Collection<Move> moves) {
		final List<Move> attacks = new ArrayList<>();
		for(final Move move: moves) {
			if(pos == move.getDest()) {
			attacks.add(move);
			}
		}
		
		return ImmutableList.copyOf(attacks);
	}

	private King establishKing() {
		for(final Piece piece: getActivePieces()) {
			if(piece.getType() == PieceType.KING) {
				return (King) piece;
			}
		}
		throw new RuntimeException("No King Detected!");
	}
	
	protected boolean hasEscapeMoves() {
		for(final Move move: this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if(transition.getMoveStatus() == MoveStatus.DONE) {
				return true;
			}
		}
		return false;
	}
	
	public Piece getKing() {
		return this.playerKing;
	}
	
	public boolean isLegalMove(final Move move) {
		return this.legalMoves.contains(move);
	}
	
	public boolean isInCheck() {
		return this.checked;
	}
	
	public boolean isInCheckMate() {
		return this.checked && !hasEscapeMoves();
	}
	
	public boolean isInStaleMate() {
		return !this.checked && !hasEscapeMoves();
	}
	
	public boolean isCastled() {
		return false;	
	}
	
	public Collection<Move> getLegalMoves(){
		return this.legalMoves;
	}
	
	public MoveTransition makeMove(final Move move) {
		
		for(final Move curMove: this.legalMoves) {
		//	System.out.println("looking for: " + move.toString());
			//System.out.println("current move in list of legal moves: " + curMove.toString());
		}
        if (!this.legalMoves.contains(move)) {
        	//System.out.println("illegal move");
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        
        final Board transitionedBoard = move.execute();
        return transitionedBoard.curPlayer().opponent().isInCheck() ?
                new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK) :
                new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
	}
	
	public Move onOpponentMoveMade() {
		 return brain.search(this.getAlliance());
	}
	
	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player opponent();
	public abstract Collection<Move> calculateKingCastle(final Collection<Move> legalMoves,final Collection<Move> opponentLegalMoves);
	
}
