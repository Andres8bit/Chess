package com.chess.engine.player.AI;

import java.util.Collection;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.peices.Alliance;
import com.chess.engine.player.MoveTransition;

public class MiniMax {
	private int maxDepth;
	private int curDepth;
	private Board curBoard;
	private long alpha;
	private long beta;
	
	
	public MiniMax(final int depth, final Board board, final long alpha, final long beta) {
		this.maxDepth = depth;
		this.curBoard = board;
		this.alpha = alpha;
		this.beta = beta;
	}
	
	public Move search(final Alliance side) {
		
	final Collection<Move> moves = (Collection<Move>) this.curBoard.getAllLegalMoves();
	
     Move best = MoveFactory.getNullMove();
     long aplha = Long.MAX_VALUE; // min
     long beta = Long.MIN_VALUE; // max
     long cur = 0;
     for(final Move move: moves) {
    	 final MoveTransition transition = curBoard.curPlayer().makeMove(move);
    	 if(transition.getMoveStatus().isDone()) {
    		 if(curBoard.curPlayer().getAlliance().isWhite()) {
    			 cur = min(this.maxDepth - 1,transition.getToBoard());
    			 aplha = cur <= aplha ? cur : alpha;
    			 best = move;
    		 }else {
    			 cur = max(this.maxDepth - 1, transition.getToBoard());
    			 beta = cur >= beta ? cur : beta;
    			 best = move;
    		 }
    	 }
     }
		
		return best;
	}
	
	
	private long min(final int depth, final Board board) {
		if(depth == 0) {
			return Evaluator.evaluate(board, Alliance.WHITE);
		}
		
		long curMin = Long.MAX_VALUE;
		final Collection<Move> moves = board.curPlayer().getLegalMoves();
		
		for(final Move move: moves) {
			final MoveTransition transition = board.curPlayer().makeMove(move);
			
			if(transition.getMoveStatus().isDone()) {
				final long curVal = max(depth-1,board);
				
				if(curVal <= curMin) {
					curMin = curVal;
				}
			}
			
		}
		 return curMin;
	}
	
	private long max(final int depth,final Board board) {
		if(depth == 0) {
			return Evaluator.evaluate(board, Alliance.BLACK);	
		}
		
		long curMax = Long.MIN_VALUE;
		final Collection<Move> moves = board.curPlayer().getLegalMoves();
		
		for(final Move move: moves) {
			final MoveTransition transition = board.curPlayer().makeMove(move);
			
			if(transition.getMoveStatus().isDone()) {
				final long curVal = min(depth-1,board);
				
				if(curVal >= curMax) {
					curMax = curVal;
				}
			}
			
		}
		 return curMax;
	}
}
