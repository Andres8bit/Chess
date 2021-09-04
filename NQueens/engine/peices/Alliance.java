package com.chess.engine.peices;

import com.chess.engine.player.BPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WPlayer;

public enum Alliance {
	
BLACK{
	@Override
	public int direction() {
		return -1;
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
},
WHITE{

	@Override
	public int direction() {
		return 1;
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
	
};

public abstract int direction();
public abstract boolean isWhite();
public abstract boolean isBlack();

public abstract Player choosePlayer(WPlayer wplayer,BPlayer bplayer);
}
