package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Rook;
import com.chess.engine.player.AI.Evaluator;
import com.chess.engine.peices.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Ai extends Player{
	private Alliance side;
	private Evaluator eval;
	
	public Ai(Alliance side, Board board){
		// board          -> board 
		// legalMoves     -> if side is black return blegalMoves else return wLegalMoves
		// OpponentsMoves -> if side isBlack return white legal moves else return bLegalMoves
		super(board,side.isBlack()?board.bPlayer().getLegalMoves():board.wPlayer().getLegalMoves(),side.isBlack()?board.wPlayer().getLegalMoves():board.bPlayer().getLegalMoves());
		this.side = side;
		this.eval = null;
	}
	
	Ai(Alliance side,Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
		super(board, legalMoves, opponentMoves);
		this.side = side;
		this.eval = null;
	}

	Ai(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
		super(board, legalMoves, opponentMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.side.isBlack()?this.board.wPlayer().getActivePieces():this.board.bPlayer().getActivePieces();
	}

	@Override
	public Alliance getAlliance() {
		return this.side;
	}

	@Override
	public Player opponent() {
		return this.side.isBlack()?this.board.wPlayer():this.board.bPlayer();

	}

	@Override
	public Collection<Move> calculateKingCastle(Collection<Move> legalMoves, Collection<Move> opponentLegalMoves) {
		
		return this.side.isBlack()? bSideCalculateCastle(legalMoves,opponentLegalMoves) : wSideCalculateCastle(legalMoves,opponentLegalMoves);
	}
	
	private Collection<Move> bSideCalculateCastle(Collection<Move> legalMoves, Collection<Move> opponentLegalMoves){
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
	
	private Collection<Move> wSideCalculateCastle(Collection<Move> legalMoves, Collection<Move> opponentLegalMoves){
final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			if(!this.board.getTile(61).isOccupied() && !this.board.getTile(62).isOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
					if( Player.AttacksOnTile(61, opponentLegalMoves).isEmpty()
					  && Player.AttacksOnTile(62,opponentLegalMoves).isEmpty()
					  && rookTile.getPiece().getType() == PieceType.ROOK) {
			          kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62, (Rook)rookTile.getPiece(), rookTile.getCoord(), 61));
					}
				}
			}
			
			if(   !this.board.getTile(59).isOccupied() 
			   && !this.board.getTile(58).isOccupied()
			   && !this.board.getTile(57).isOccupied()) {
				final Tile rookTile = this.board.getTile(56);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()
						&& Player.AttacksOnTile(58,opponentLegalMoves).isEmpty()
						&& Player.AttacksOnTile(59,opponentLegalMoves).isEmpty()
						&& rookTile.getPiece().getType() == PieceType.ROOK) {
					kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getPiece(), rookTile.getCoord(), 69));
				}
			}
		}
		
		
		return ImmutableList.copyOf(kingCastles);
	}

	public Move calculateBestMove() {
		return null;
	}
	
}
