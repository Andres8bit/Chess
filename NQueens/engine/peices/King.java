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

public class King extends Piece{
	private final static int[] POSSIBLE_MOVES = {-9,-8,-7,-1,1,7,8,9};
	
	
	public King(final int pos, final Alliance owner, final Boolean first) {
		super(pos,owner,PieceType.KING,first);
	}
	
	public King(final int pos, final Alliance owner) {
		super(pos, owner,PieceType.KING,true);
	}

	@Override
	public String toString() {
		return Piece.PieceType.KING.toString();
	}
	
	@Override
	public Collection<Move> legalMoves(final Board board) {
		final List<Move> legal_moves =new ArrayList<>();

		for (final int offset:POSSIBLE_MOVES) {
			if(firstColumn(this.position,offset)|| eighthColumn(this.position,offset)) {
					continue;
			}
			
			
			final int canidate = this.position + offset;
			if(BoardUtils.isValidTile(canidate)) {
				final Piece occupier = board.getPiece(canidate);

				if(occupier == null) {
				legal_moves.add(new MajorMove(board,this,canidate));
			}else {
				final Alliance owner = occupier.owner;
				if(owner != occupier.piece_alliance()) {
					legal_moves.add(new MajorAttackMove(board,this,canidate,occupier));
				}
			}
		}
	}
	return ImmutableList.copyOf(legal_moves);
  }

	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN.get(coord) && ( offset == 7 || offset == -9 || offset == -1);	
	}
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.EIGHTH_COLUMN.get(coord) && (offset == -7 || offset == 9 || offset == 1);
	}


	@Override
	public King movePiece(final Move move) {
		return new King(move.getDest(),move.getPiece().piece_alliance(),false);
	}
	
	
	@Override
	public ImageIcon getImg() {
		
		   BufferedImage image = null; 
			
		   try {
				if(this.owner.isBlack()) {
					image = ImageIO.read(new File("C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\BK.png"));
				}else {
					image = ImageIO.read(new File("C:\\\\Users\\\\andres\\\\eclipse-workspace\\\\NQueens\\\\art\\\\WK.png"));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ImageIcon(image);
	}
}
