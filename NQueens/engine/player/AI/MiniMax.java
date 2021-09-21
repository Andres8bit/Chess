package com.chess.engine.player.AI;

import java.util.Collection;

import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.peices.Alliance;
import com.chess.engine.player.MoveTransition;

public class MiniMax extends Thread implements Strategy{
 private int depth;
 long alpha = Long.MAX_VALUE; // min
 long beta = Long.MIN_VALUE; // max
	
	
	public MiniMax(final int depth, final Board board, final long alpha, final long beta) {
		this.depth = depth;
		//this.alpha = alpha;
		//this.beta = beta;

	}
	
	@Override
	public Move excecute(Board board)  {
	
     Move best = MoveFactory.getNullMove();
     Board curBoard  = Board.createStandardBoard();
     long cur = 0;
     for(final Move move: board.curPlayer().getLegalMoves()) {
    	 
    	 final MoveTransition transition = board.curPlayer().makeMove(move);
    	 
    	 if(transition.getMoveStatus().isDone()) {
    		 curBoard = transition.getToBoard();
    		 
    		 System.out.println("top level minimax");
    		 final long score = board.curPlayer().getAlliance().isBlack()?max(this.depth-1,curBoard):min(this.depth-1,curBoard);
    		 
    		 if(board.curPlayer().getAlliance().isBlack()) {
    			 if(score > beta) {
    				 System.out.println("set beta to :" + score);
    				 beta = score;
    				 best = move;
    			 }
    		 }else {
    			 if(score < alpha) {
    				 System.out.println("set alpha to :" + score);
    				 alpha = score;
    				 best = move;
    			 }
    			 
    		 }
    	 }
     }
		
		return best;
	}
	
	
	private long min(final int depth, final Board board) {
		// System.out.println("min call");
		final long score = Evaluator.evaluate(board, Alliance.WHITE);
//		System.out.println(" ================== start =====================");
//		System.out.println("score from min: " + score);
//		System.out.println("current alpha: " + alpha);
//		System.out.println(" ================== End =====================");
//		if(score > alpha) {
//			return alpha;
//		}else if(depth == 0) {
//
//			return score;
//		}
//		
		
		if(depth == 0) {
			return score;
		}
		
		long curMin = Long.MAX_VALUE;
		final Collection<Move> moves = board.curPlayer().getLegalMoves();
		
		for(final Move move: moves) {
			final MoveTransition transition = board.curPlayer().makeMove(move);
			
			if(transition.getMoveStatus().isDone()) {
			//	System.out.println();
			//	System.out.println("call to max from min");
			//	System.out.println();
				final long curVal = max(depth-1,board);
				
				if(curVal <= curMin) {
					curMin = curVal;
				}
			}else {
				System.out.println("move was not valid");
			}
			
		}
		 return curMin;
	}
	
	private long max(final int depth,final Board board) {
		// System.out.println("max call ");
		final long score = Evaluator.evaluate(board, Alliance.BLACK);
//		System.out.println(" ================== start =====================");
//		System.out.println("score from max: " + score);
//		System.out.println("current beta: " + beta);
//		System.out.println(" ================== End =====================");
//		if(score < beta) {
//			return beta;
//		}else if(depth == 0) {
//			//System.out.println("returning from max");
//			return score;
//		}
		
		if(depth == 0) {
			return score;
		}
		
		long curMax = Long.MIN_VALUE;
		final Collection<Move> moves = board.curPlayer().getLegalMoves();
		
		for(final Move move: moves) {
			final MoveTransition transition = board.curPlayer().makeMove(move);
			
			if(transition.getMoveStatus().isDone()) {
			//	System.out.println();
				//System.out.println("call to min from max");
			//	System.out.println();
				final long curVal = min(depth-1,board);
				
				if(curVal >= curMax) {
					curMax = curVal;
				}
			}
		}
		 return curMax;
	}



}
