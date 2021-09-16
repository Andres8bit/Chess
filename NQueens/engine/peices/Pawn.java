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
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnMove;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece{
	private final static int[] POSSIBLE_MOVES = {8,16,7,9};
	
	public Pawn(final int pos, final Alliance owner, final Boolean first) {
		super(pos,owner,PieceType.PAWN,first);
	}
	
	public Pawn(final int pos, final Alliance owner) {
		super(pos, owner,PieceType.PAWN,true);
	}
	
	@Override
	public String toString() {
		return Piece.PieceType.PAWN.toString();// + owner.toString();
	}
	

	@Override
	public Collection<Move> legalMoves(final Board board) {
		List<Move> legal_moves = new ArrayList<>();
	
		for(final int offset: POSSIBLE_MOVES) {
			final int candidate = this.position + (this.owner.direction() * offset);
			
			if(!BoardUtils.isValidTile(candidate)) {
				continue;
			}
			//first move of pawn  && check for space to jump
			if(this.isFirstMove() && this.pawnJumpCheck(board,offset)) {
			   		legal_moves.add(new PawnMove(board,this,candidate));
			}
			//possible enPassant opening
			if(board.getEnPassant() != null && this.enPassantAttackCheck(board,offset)) {
					final Piece piece = board.getEnPassant();
					final Move move = new PawnEnPassantAttackMove(board, this, candidate, piece);
					legal_moves.add(new PawnEnPassantAttackMove(board, this, candidate, piece));
					//System.out.println("Added enPassant Attack from :" + BoardUtils.getPos(move.getCur()) + " to " + BoardUtils.getPos(move.getDest()));
			}
			//base attack check
			if( (offset == 7 || offset == 9) && this.pawnAttackCheck(board,offset)) {
					final Piece piece= board.getPiece(candidate);
					legal_moves.add(new PawnAttackMove(board,this,candidate,piece));
					//System.out.println("Added pawn Attack :" + new PawnAttackMove(board, this, candidate, piece).toString());
			}
			// regular move forward
			if(offset == 8 && board.getPiece(candidate) == null) {
				legal_moves.add(new PawnMove(board,this,candidate));		
				//System.out.println("Added pawn move :" + new PawnMove(board, this, candidate).toString());
			}
		}
		return ImmutableList.copyOf(legal_moves);
	}

	private boolean enPassantAttackCheck(Board board, int offset) {
		final int candidate = this.position + (this.owner.direction() * offset);
		
		if(offset != 7 && offset != 9 || board.getPiece(candidate)!= null) {
				return false;
		}
	
		
		if(offset == 7 && !((BoardUtils.EIGHTH_COLUMN.get(this.position) && this.owner.isWhite()
				           || (BoardUtils.FIRST_COLUMN.get(this.position)&& this.owner.isBlack())))) {
			
			if(board.getEnPassant().pos() == (this.position + (this.owner.getOppositeDirection()))){
				final Piece piece = board.getEnPassant();
           
				return this.owner != piece.piece_alliance();
			}
			
		}else if(!((BoardUtils.FIRST_COLUMN.get(this.position) && this.owner.isWhite()
				|| (BoardUtils.EIGHTH_COLUMN.get(this.position)&& this.owner.isBlack())))){
			
			if(board.getEnPassant().pos() == (this.position - (this.owner.getOppositeDirection()))){
				final Piece piece = board.getEnPassant();
				return this.owner != piece.piece_alliance(); 
			}
		}
		
		return false;
	}

	private boolean pawnAttackCheck(final Board board,final int offset) {
		final int candidate = this.position + (this.owner.direction() * offset);
		
		 if(offset == 7 &&
					!((BoardUtils.EIGHTH_COLUMN.get(this.position) && this.owner.isWhite()
					|| (BoardUtils.FIRST_COLUMN.get(this.position)&& this.owner.isBlack())))) {
				
				if(board.getPiece(candidate) != null) {
						final Piece piece = board.getPiece(candidate);
							return this.owner != piece.piece_alliance();
					}				
			}else if(
					!((BoardUtils.FIRST_COLUMN.get(this.position) && this.owner.isWhite()
					|| (BoardUtils.EIGHTH_COLUMN.get(this.position)&& this.owner.isBlack())))) {
	
				if(board.getPiece(candidate) != null) {					
					final Piece piece = board.getPiece(candidate);
					   		return this.owner != piece.piece_alliance();					
				}
			}
		return false;
	}

	private boolean pawnJumpCheck(final Board board,final int offset ) {
		if( offset == 16 && (
			(BoardUtils.SEVENTH_ROW.get(this.position) && this.piece_alliance().isBlack()) 
								            || 
			(BoardUtils.SECOND_ROW.get(this.position) && this.piece_alliance().isWhite()))) {
				final int candidate = this.position + (this.owner.direction() * offset);
				final int behind = this.position + (this.owner.direction() * 8);
			
				 if(board.getPiece(behind) == null && board.getPiece(candidate) == null) { 
					 	return true;
				 }
		}
		return false;
	}

	@Override
	public Pawn movePiece(final Move move) {
		//System.out.println("move Pawn");
		return new Pawn(move.getDest(),move.getPiece().piece_alliance(),false);
	}

	public Piece promote() {
		return  new Queen(this.position, this.owner, false);
	}
	
	@Override
	public ImageIcon getImg() {
		   BufferedImage image = null; 
			
		   try {
				if(this.owner.isBlack()) {
					image = ImageIO.read(new File("C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\BP.png"));
				}else {
					image = ImageIO.read(new File("C:\\\\Users\\\\andres\\\\eclipse-workspace\\\\NQueens\\\\art\\\\WP.png"));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ImageIcon(image);
	}
}