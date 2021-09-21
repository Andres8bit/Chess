package com.chess.engine.player.AI;

import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.google.common.collect.ImmutableList;

public class MiniMaxConcurrent extends Thread implements Strategy{
	private int depth;
	private final int threadLimit;

	public MiniMaxConcurrent(final int depth, final int threadCount) {
		this.depth = depth;
		this.threadLimit = threadCount;
	}


	@Override
	public Move excecute(Board board) {
		final List<Move> movePool = ImmutableList.copyOf(board.getAllLegalMoves());
		
		
		return null;
	}
}
