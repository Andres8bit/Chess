package com.chess.engine.board;

public class BoardUtils {

	public static final boolean[] FIRST_COLUMN  = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);;
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] SECOND_ROW = initRow(1);
    public static final boolean[] SEVENTH_ROW = initRow(6);
	public static final int NUM_TILES = 64;
	public static final int TILES_PER_ROW = 8;
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
	
	public static boolean[] initRow(int rowNum) {
		final boolean[] row = new boolean[NUM_TILES];
		do {
			row[rowNum] = true;
			rowNum++;
		}while(rowNum % TILES_PER_ROW != 0);
		
		return row;
	}
}
