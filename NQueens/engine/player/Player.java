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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean checked;
	
	Player(final Board board, final Collection<Move> legalMoves,
			final Collection<Move> opponentMoves){
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastle(legalMoves,opponentMoves)));
		this.checked = !Player.AttacksOnTile(this.playerKing.pos(),opponentMoves).isEmpty();
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
		if(!isLegalMove(move)) {
			return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board transition = move.execute(); 
		final Collection<Move> kingAttacks = Player.AttacksOnTile(transition.curPlayer().opponent().getKing().pos(), 
				transition.curPlayer().getLegalMoves());
		
		if(!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		
		return new MoveTransition(this.board,move,MoveStatus.DONE);
	}
	
	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player opponent();
	public abstract Collection<Move> calculateKingCastle(final Collection<Move> legalMoves,final Collection<Move> opponentLegalMoves);
	
}
