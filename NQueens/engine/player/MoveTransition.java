package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
	private final Board transitionBoard;
	private final Move move;
	private final MoveStatus moveStatus;
	
	public MoveTransition(final Board transition,final Move move,final MoveStatus moveStatus) {
		this.transitionBoard = transition;
		this.move = move;
		this.moveStatus = moveStatus;
	}
	
	public MoveStatus getMoveStatus() {
		return moveStatus;
	}
}
