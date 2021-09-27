package com.chess.engine.peices;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

public class Bishop extends Piece{
	private final static int[] POSSIBLE_MOVES = {-9,-7,7,9};

	
	public Bishop(final int pos, final Alliance owner, final Boolean first) {
		super(pos,owner,PieceType.BISHOP,first);
	}
	
	public Bishop(final int pos,final Alliance owner) {
		super(pos, owner,PieceType.BISHOP,true);
	}

	@Override
	public String toString() {
		return owner.isWhite()? 
			PieceType.BISHOP.toString().toUpperCase(): PieceType.BISHOP.toString();
	}
	
	@Override
	public Collection<Move> legalMoves(final Board board) {
	final List<Move> legal_moves = new ArrayList<>();
	for(final int offset: POSSIBLE_MOVES) {
		int canidateDest = this.position;
		while(BoardUtils.isValidTile(canidateDest)) {
			if(firstColumn(canidateDest,offset) ||
				eighthColumn(canidateDest,offset))
				break;
			canidateDest += offset;
			if(BoardUtils.isValidTile(canidateDest)) {
				final Piece piece = board.getPiece(canidateDest);
				if(piece == null) {
					legal_moves.add(new MajorMove(board, this, canidateDest));
				}else {
					final Alliance owner = piece.piece_alliance();
					if(this.owner != owner) {
						legal_moves.add(new MajorAttackMove(board, this, canidateDest, piece));
					}
					break;
				}
			}
		}
		
	}
		return ImmutableList.copyOf(legal_moves);
	}
	
	private static boolean firstColumn(final int coord,final int offset) {
		return BoardUtils.FIRST_COLUMN.get(coord) && (offset == -9 || offset == 7);	
	}
	
	
	private static boolean eighthColumn(final int coord, final int offset) {
		return BoardUtils.EIGHTH_COLUMN.get(coord) && (offset == -7 || offset == 9);
	}

	@Override
	public Bishop movePiece(Move move) {
		return new Bishop(move.getDest(),move.getPiece().piece_alliance(),false);
	}

	@Override
	public ImageIcon getImg() {
	   BufferedImage image = null; 
		
	   try {
			if(this.owner.isBlack()) {
				image = ImageIO.read(new File("C:\\Users\\andres\\eclipse-workspace\\NQueens\\art\\BB.png"));
			}else {
				image = ImageIO.read(new File("C:\\\\Users\\\\andres\\\\eclipse-workspace\\\\NQueens\\\\art\\\\WB.png"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ImageIcon(image);
	}

}
