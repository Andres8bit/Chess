package com.chess.engine.peices;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

public  class Queen extends Piece {
	private final static int[] POSSIBLE_MOVES = {-9,-8,-7,-1,1,7,8,9};

	public Queen(final int pos, final Alliance owner, final Boolean first) {
		super(pos,owner,PieceType.QUEEN,first);
	}
	
	public Queen(final int pos, final Alliance owner) {
		super(pos, owner,PieceType.QUEEN,true);
	}

	@Override
	public String toString() {
		return owner.isWhite()? Piece.PieceType.QUEEN.toString().toUpperCase() : Piece.PieceType.QUEEN.toString();
	}
	
	@Override
	public Collection<Move> legalMoves(final Board board) {
		final List<Move> legal_moves = new ArrayList<>();
		for(final int offset: POSSIBLE_MOVES) {
			int candidate = this.position;
			while(BoardUtils.isValidTile(candidate)) {
			
				if(firstColumn(candidate,offset) ||
						eighthColumn(candidate,offset)) {
					break;
				}
			
				candidate += offset;
				if(!BoardUtils.isValidTile(candidate)) {
					break;
				}else {
					final Piece piece = board.getPiece(candidate);
					if(piece == null) {
						legal_moves.add(new MajorMove(board,this,candidate));
					}else {
						final Alliance owner = piece.piece_alliance();
						if(this.owner != owner) {
							legal_moves.add(new MajorAttackMove(board,this,candidate,piece));
						}
					break;
				}
			}
		  }
	   }
		return ImmutableList.copyOf(legal_moves);
  }
	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN.get(coord) && ( offset == -1 || offset == -9 || offset == 7);	
	}
	
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.EIGHTH_COLUMN.get(coord) && (offset == -7 || offset == 9 || offset == 1);
	}

	@Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getDest(),move.getPiece().piece_alliance(),false);
	}
	
	@Override
	public ImageIcon getImg() {
		   BufferedImage image = null; 
			
		   try {
				if(this.owner.isBlack()) {
					image = ImageIO.read(new File("C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\BQ.png"));
				}else {
					image = ImageIO.read(new File("C:\\\\Users\\\\andres\\\\eclipse-workspace\\\\NQueens\\\\art\\\\WQ.png"));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ImageIcon(image);
	}
}
