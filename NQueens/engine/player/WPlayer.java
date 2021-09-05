package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Piece;
import com.google.common.collect.ImmutableList;
import com.chess.engine.peices.Piece.PieceType;

public class WPlayer extends Player {

	public WPlayer(Board board, Collection<Move> wLegalMoves, Collection<Move> bLegalMoves) {
		super(board,wLegalMoves,bLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player opponent() {
		return this.board.bPlayer();
	}

	@Override
	public Collection<Move> calculateKingCastle(final Collection<Move> legalMoves,final Collection<Move> opponentLegalMoves) {
		final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			if(!(this.board.getTile(61).isOccupied()) && !(this.board.getTile(62).isOccupied())) {
				final Tile rookTile = this.board.getTile(63);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
					if( Player.AttacksOnTile(61, opponentLegalMoves).isEmpty()
					  && Player.AttacksOnTile(62,opponentLegalMoves).isEmpty()
					  && rookTile.getPiece().getType() == PieceType.ROOK) {
			          kingCastles.add(null);
					}
				}
			}
			
			if(   !this.board.getTile(59).isOccupied() 
			   && !this.board.getTile(58).isOccupied()
			   && !this.board.getTile(57).isOccupied()) {
				final Tile rookTile = this.board.getTile(56);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
					
				}
			}
		}
		
		
		return ImmutableList.copyOf(kingCastles);
	}
}
