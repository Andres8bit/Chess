package com.chess.engine.player.AI;

import java.util.Collection;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.peices.Alliance;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements Strategy{
 private int depth;
	
	
	public MiniMax(final int depth, final Board board, final long alpha, final long beta) {
		this.depth = depth;
	}
	
	@Override
	public Move excecute(Board board)  {
	
     Move best = MoveFactory.getNullMove();
     long alpha = Long.MAX_VALUE; // min
     long beta = Long.MIN_VALUE; // max
     long cur = 0;
     for(final Move move: board.getAllLegalMoves()) {
    	 final MoveTransition transition = board.curPlayer().makeMove(move);
    	 if(transition.getMoveStatus().isDone()) {
    		 if(board.curPlayer().getAlliance().isWhite()) {
    			// System.out.println("call min with depth:" + (depth - 1));
				 cur = min(depth - 1,transition.getToBoard());
    			 if(cur <=alpha) {
    				 alpha = cur;
    				 best = move;
    			 }
    		 }else {
    			 //System.out.println("call max with depth:" + (depth - 1));
    			 cur = max(depth - 1, transition.getToBoard());
    			 if(cur >= beta) {
    				 beta = cur;
    				 best = move;
    			 }
    		 }
    	 }
     }
		
		return best;
	}
	
	
	private long min(final int depth, final Board board) {
		//System.out.println("min at depth:" + depth);
		if(depth == 0) {
			//System.out.println("returning from min");
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
		//System.out.println("max at depth:" + depth);
		if(depth == 0) {
			//System.out.println("returning from max");
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
