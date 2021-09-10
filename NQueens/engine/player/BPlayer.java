package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Piece.PieceType;
import com.chess.engine.peices.Rook;
import com.google.common.collect.ImmutableList;

public class BPlayer extends Player {
	public BPlayer(final Board board, final Collection<Move> wLegalMoves, final Collection<Move> bLegalMoves) {
		super(board,bLegalMoves,wLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player opponent() {
		return this.board.wPlayer();
	}

	@Override
	public Collection<Move> calculateKingCastle(final Collection<Move> legalMoves,final Collection<Move> opponentLegalMoves) {
		final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			if(!(this.board.getTile(5).isOccupied()) && !(this.board.getTile(6).isOccupied())) {
				final Tile rookTile = this.board.getTile(7);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
					if( Player.AttacksOnTile(5, opponentLegalMoves).isEmpty()
					  && Player.AttacksOnTile(6,opponentLegalMoves).isEmpty()
					  && rookTile.getPiece().getType() == PieceType.ROOK) {
			        kingCastles.add(new KingSideCastleMove(this.board,this.playerKing,6,(Rook) rookTile.getPiece(),rookTile.getCoord(),5));
					}
				}
			}
			
			if(   !this.board.getTile(1).isOccupied() 
			   && !this.board.getTile(2).isOccupied()
			   && !this.board.getTile(3).isOccupied()) {
				final Tile rookTile = this.board.getTile(0);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()
						&& Player.AttacksOnTile(2,opponentLegalMoves).isEmpty()
						&& Player.AttacksOnTile(3,opponentLegalMoves).isEmpty()
						&& rookTile.getPiece().getType() == PieceType.ROOK) {
					kingCastles.add(new QueenSideCastleMove(this.board,this.playerKing,2,(Rook)rookTile.getPiece(),rookTile.getCoord(),3));
				}
			}
		}		
		return ImmutableList.copyOf(kingCastles);
	}
}
