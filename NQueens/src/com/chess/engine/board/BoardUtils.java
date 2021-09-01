package com.chess.engine.board;

public class BoardUtils {

	public static final boolean[] FIRST_COLUMN  = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);;
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] SECOND_ROW = null;
    public static final boolean[] SEVENTH_ROW = null;
	private BoardUtils() {
		throw new RuntimeException("Cannot Instantiate");
	}
	
	public static boolean isValidTile(int coord) {
		return coord >= 0 && coord < 64;
	}
	
	public static boolean[] initColumn(int col) {
		final boolean[] column = new boolean[64];
		do {
			column[col] = true;
			col += 8;
		}while(col < 64);
		return column;
	}


}
