package com.chess.engine.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtils {


	public final static List<String> ALGEBRAIC_NOTATION = initAlgNot();
	public final static Map<String,Integer> POSITION_TO_COORDINATE = initPosToCoor();
    public final static List<Boolean> FIRST_COLUMN = initColumn(0);
    public final static List<Boolean> SECOND_COLUMN = initColumn(1);
    public final static List<Boolean> THIRD_COLUMN = initColumn(2);
    public final static List<Boolean> FOURTH_COLUMN = initColumn(3);
    public final static List<Boolean> FIFTH_COLUMN = initColumn(4);
    public final static List<Boolean> SIXTH_COLUMN = initColumn(5);
    public final static List<Boolean> SEVENTH_COLUMN = initColumn(6);
    public final static List<Boolean> EIGHTH_COLUMN = initColumn(7);
    
    public final static List<Boolean> EIGHTH_ROW = initRow(0);
    public final static List<Boolean> SEVENTH_ROW = initRow(8);
    public final static List<Boolean> SIXTH_ROW = initRow(16);
    public final static List<Boolean> FIFTH_ROW = initRow(24);
    public final static List<Boolean> FOURTH_ROW = initRow(32);
    public final static List<Boolean> THIRD_ROW = initRow(40);
    public final static List<Boolean> SECOND_ROW = initRow(48);
    public final static List<Boolean> FIRST_ROW = initRow(56);
	public static final int NUM_TILES = 64;
	public static final int TILES_PER_ROW = 8;
	
	private BoardUtils() {
		throw new RuntimeException("Cannot Instantiate");
	}
	
	private static Map<String, Integer> initPosToCoor() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
	}

	private static List<String>initAlgNot() {
	    return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
	}

	public static boolean isValidTile(int coord) {
		return (coord >= 0 && coord < 64);
	}
	
	public static int getCoords(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}
	
	public static String getPos(final int coord) {
		return ALGEBRAIC_NOTATION.get(coord);
	} 

	private static List<Boolean> initColumn(int columnNumber) {
	        final Boolean[] column = new Boolean[NUM_TILES];
	        for(int i = 0; i < column.length; i++) {
	            column[i] = false;
	        }
	        do {
	            column[columnNumber] = true;
	            columnNumber += TILES_PER_ROW;
	        } while(columnNumber < NUM_TILES);
	        return Collections.unmodifiableList(Arrays.asList((column)));
	    }

	    private static List<Boolean> initRow(int rowNumber) {
	        final Boolean[] row = new Boolean[NUM_TILES];
	        for(int i = 0; i < row.length; i++) {
	            row[i] = false;
	        }
	        do {
	            row[rowNumber] = true;
	            rowNumber++;
	        } while(rowNumber % TILES_PER_ROW != 0);
	        return Collections.unmodifiableList(Arrays.asList(row));
	    }
}
