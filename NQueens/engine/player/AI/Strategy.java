package com.chess.engine.player.AI;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public interface Strategy {
	public Move excecute(final Board board);

}
