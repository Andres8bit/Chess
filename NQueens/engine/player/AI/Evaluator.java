package com.chess.engine.player.AI;

import java.util.ArrayList;
import java.util.Collection;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Piece;
import com.chess.engine.player.MoveTransition;

public final class Evaluator {
  
	public static long evaluate(final Board board,final Alliance side) {

	  return  side.direction() *  pieceScore(board,side) + legalMovesScore(board,side);
  }
	
	public static Move getBestMove(final Board board, final Alliance side) {
		final Collection<Move> moves = side.isBlack()?board.bPlayer().getLegalMoves(): board.wPlayer().getLegalMoves();
		long max = Long.MIN_VALUE;
		long cur = Long.MIN_VALUE;
		Move curBest = MoveFactory.getNullMove();
		
		for(final Move move: moves) {	
			final MoveTransition transition = board.curPlayer().makeMove(move);
			if(transition.getMoveStatus().isDone()) {
				cur = evaluate(board,side);
				if(cur > max) {
					max = cur;
					curBest = move;
				}
		   }
		}
		
		return curBest;
	}
	
	private static long pieceScore(final Board board, final Alliance side) {
		long sum = 0;
		
		final Collection<Piece> pieces = side.isBlack()? board.getBlackPieces():board.getWhitePieces();
		
		for(final Piece piece: pieces) {
			sum += piece.getVal();
		}
		
		return sum;
	}
	
	private static long legalMovesScore(final Board board, final Alliance side) {
		long sum = 0;
		final Collection<Move> moves = side.isBlack()?board.bPlayer().getLegalMoves():board.wPlayer().getLegalMoves();
		
		for(final Move move: moves) {
			if(move.isAttack()) {
				sum += move.getAttackPiece().getVal();
			}else {
				sum += 100;
			}
		}
		return sum;
	}
}
