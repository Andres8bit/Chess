package com.chess.engine.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.peices.Alliance;
import com.chess.engine.peices.Bishop;
import com.chess.engine.peices.King;
import com.chess.engine.peices.Knight;
import com.chess.engine.peices.Pawn;
import com.chess.engine.peices.Piece;
import com.chess.engine.peices.Queen;
import com.chess.engine.peices.Rook;

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
	    
	    
	   public static Board FENtoBoard(final String fen) {
		   String[][] rows = new String[8][1];
		   Builder builder = new Builder();
		   int start_index = 0;
		   int end_index = fen.indexOf('/');
		   int boardPos = 0;
		   Alliance turnMaker = null;
		   
		   for(int i = 0; i < TILES_PER_ROW-1; i++) {
			   rows[i][0] = fen.substring(start_index,end_index);
			  start_index = end_index+1;
			  end_index = fen.indexOf('/', start_index+1);
		   }
		   
		   
		   end_index = fen.indexOf(' ',start_index);
		   rows[7][0] = fen.substring(start_index,end_index);
		   
		   turnMaker = fen.charAt(end_index+1) == 'b' ? Alliance.BLACK : Alliance.WHITE;
		   builder.setMoveMaker(turnMaker);
		   
		   for(int i = 0; i < 8; i++) {
			for(int j = 0; j < rows[i][0].length();j++) {		
				if(Character.isDigit(rows[i][0].charAt(j))) {
					boardPos += Character.getNumericValue(rows[i][0].charAt(j));
				}else {
					
					final Piece piece = BoardUtils.buildPiece(rows[i][0].charAt(j),boardPos);	
					if(piece != null) 
						{ builder.setPiece(piece); }
					
					boardPos++;
				}
			}   
		   }
	
		   return builder.build();
		   
		   
	   }
	   
	   
	private static Piece buildPiece(char type, int boardPos) {
		Alliance owner = Character.isUpperCase(type) ? Alliance.WHITE : Alliance.BLACK;
		boolean isFirstMove = true;
		char piece_type = Character.toUpperCase(type);
		
		switch(piece_type) {
		 	case 'B':
		 		return new Bishop(boardPos,owner);
		 	case 'K':
		 		return new King(boardPos,owner);
		 	case 'N':
		 		return new Knight(boardPos,owner);

		 	case 'P':
		 		if(owner.isBlack()) {
		 			if(!( boardPos >= 8 && boardPos <= 15)) {
		 				isFirstMove = false;
		 			}
		 		}else if(!( boardPos >= 48 && boardPos <= 63)){
	 				isFirstMove = false;
	 			}
		 		
		 		return new Pawn(boardPos,owner,isFirstMove);

		 	case 'Q':
		 		return new Queen(boardPos,owner);

		 	case 'R':
		 		return new Rook(boardPos,owner);
		}
		return null;
	}

	public static String BoardToFEN(final Board board) {
	    	StringBuilder fen = new StringBuilder();
	    	int emptyTileCounter = 0;
	
	    	
	    	// for loop handles chess layout:
	    	for(int i = 0; i < NUM_TILES; i++) {
	    		final Tile cur = board.getTile(i);
	    		
	    		if((i%8 == 0) && i !=0) {
	    			if(emptyTileCounter !=0) {
	    				fen.append(emptyTileCounter);
	    				emptyTileCounter = 0;
	    			}
	    			fen.append("/");
	    		}
	    		if(cur.isOccupied()) {
	    			if(emptyTileCounter != 0) {
	    				fen.append(emptyTileCounter);
	    				fen.append(cur.getPiece().toString());
	    				emptyTileCounter = 0;
	    			}else {
	    				fen.append(cur.getPiece().toString());
	    			}
	    		}else {
	    			emptyTileCounter++;
	    		}

	    	}
	    	
	    	// add the current player turn
	    	fen.append(" " + board.curPlayer().getAlliance().toString() + " ");
	    	
	    	//add castle options
	    	if(!board.wPlayer().isCastled()) {
	    		fen.append("KQ");
	    	}else {
	    		fen.append("-");
	    	}
	    	
	    	if(!board.bPlayer().isCastled()) {
	    		fen.append("kq");
	    	}else {
	    		fen.append("-");
	    	}
	    	
	    	//handle enPassant:
	    	final Piece pawn = board.getEnPassant();
	    	if(pawn != null) {
	    		fen.append(" " + BoardUtils.getPos(pawn.pos()) + " ");
	    	}else {
	    		fen.append(" - ");
	    	}
	    	
	    	// checks for draw state
            if(!board.isFiftyMoveRule()) {
            	fen.append(" 0 ");
            }else {
            	fen.append(" 1 ");
            }
            
	    	fen.append(board.getTurnCount());
	    	return fen.toString();
	    }
}
