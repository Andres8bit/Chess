package com.chess.engine.peices;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WPlayer;

public enum Alliance {
	
BLACK{
	
	@Override
	public String toString() {
		return "black";
	}
	
	@Override
	public int direction() {
		return 1;
	}

	@Override
	public boolean isWhite() {
		return false;
	}

	@Override
	public boolean isBlack() {
		return true;
	}

	@Override
	public Player choosePlayer(final WPlayer wplayer,final BPlayer bplayer){
		return bplayer;
	}

	@Override
	protected int getOppositeDirection() {
		return -1;
	}

	@Override
	public boolean isPromotionSquare(int pos) {
		return BoardUtils.EIGHTH_ROW.get(pos);
	}
},
WHITE{

	@Override
	public String toString() {
		return "white";
	}
	
	@Override
	public int direction() {
		return -1;
	}

	@Override
	public boolean isWhite() {
		return true;
	}

	@Override
	public boolean isBlack() {
		return false;
	}

	@Override
	public Player choosePlayer(final WPlayer wplayer,final BPlayer bplayer){
		return  wplayer;
	}

	@Override
	protected int getOppositeDirection() {
		return 1;
	}

	@Override
	public boolean isPromotionSquare(int pos) {
		return BoardUtils.FIRST_ROW.get(pos);
	}
	
};

public abstract int direction();
public abstract boolean isWhite();
public abstract boolean isBlack();
public abstract boolean isPromotionSquare(int pos);
public abstract Player choosePlayer(WPlayer wplayer,BPlayer bplayer);
protected abstract int getOppositeDirection();
}
