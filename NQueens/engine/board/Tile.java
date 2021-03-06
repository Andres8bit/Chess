package com.chess.engine.board;
import java.util.HashMap;
import java.util.Map;

import com.chess.engine.peices.Piece;
import com.google.common.collect.ImmutableMap;


public abstract class Tile {

	protected final int coord;
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	
	
	private static final Map<Integer,EmptyTile> createAllPossibleEmptyTiles(){
		
		final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();
		
		for(int i = 0; i < 64; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		return ImmutableMap.copyOf(emptyTileMap);
	}
	
	public static Tile createTile(final int coord, final Piece piece) {
		return piece != null ? new OccupiedTile(coord, piece) : EMPTY_TILES_CACHE.get(coord);
	}
	
	private Tile(int tileCoord){
		this.coord = tileCoord;
	}
	
	public abstract boolean isOccupied();
	public abstract Piece getPiece();
	
	public static final class EmptyTile extends Tile{
		EmptyTile(final int coord){
			super(coord);
		}
		
		@Override
		public String toString() {
			
			return "-";
		}
		
		@Override
		public boolean isOccupied() {
			return false;
		}
		@Override
		public Piece getPiece() {
			return null;
		}
	}
	
	public static final class OccupiedTile extends Tile{
		private final Piece piece;
		
		OccupiedTile(int coord, Piece item){
			super(coord);
			this.piece = item;
		}
		
		@Override
		public String toString() {
			return this.getPiece().piece_alliance().isBlack() ? this.getPiece().toString().toLowerCase():
				  this.getPiece().toString();
		}
		
		@Override
		public boolean isOccupied() {
			return true;
		}
		@Override
		public Piece getPiece() {
			return this.piece;
		}
	}

	public int getCoord() {
		return this.coord;
	}
}
