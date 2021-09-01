package com.chess.engine.peices;

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
	
};

public abstract int direction();
public abstract boolean isWhite();
public abstract boolean isBlack();
}
