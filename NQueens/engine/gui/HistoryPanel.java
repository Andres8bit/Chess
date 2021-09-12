package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table.MoveLog;

public class HistoryPanel extends JPanel{
	
	private final DataModel model;
	private final JScrollPane scrollPane;
	private static final Dimension DIMS =  new Dimension(100,100);
	private static final Color PANEL_COLOR = Color.decode("#d9deda");
	
	HistoryPanel(){
		this.setLayout(new BorderLayout());
		this.model = new DataModel();
		final JTable table = new JTable(model);
		
		table.setRowHeight(15);
		this.scrollPane = new JScrollPane(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
	    scrollPane.setPreferredSize(DIMS);
	    this.setBackground(PANEL_COLOR);
		this.add(scrollPane,BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	void redo(final Board board, final MoveLog log) {
        int currentRow = 0;
        this.model.clear();
        for (final Move move : log.getMoves()) {
            final String moveText = move.toString();
            if (move.getPiece().piece_alliance().isWhite()) {
                this.model.setValueAt(moveText, currentRow, 0);
            }
            else if (move.getPiece().piece_alliance().isBlack()) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        if(log.getMoves().size() > 0) {
            final Move lastMove = log.getMoves().get(log.size() - 1);
            final String moveText = lastMove.toString();

            if (lastMove.getPiece().piece_alliance().isWhite()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
            }
            else if (lastMove.getPiece().piece_alliance().isBlack()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }
        }

        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
   }
	
	private String calculateCheckAndCheckMateHash(Board board) {
		if(board.curPlayer().isInCheckMate()) {
			return "#";
		}else if(board.curPlayer().isInCheck()) {
			return "+";
		}
		return "";
	}

	private static class DataModel extends DefaultTableModel{
		private final List<Row> values;
		private static final String[] NAMES = {"White", "Black"};
		
		DataModel(){
			this.values = new ArrayList<>();
		}
		
		public void clear() {
			this.values.clear();
			setRowCount(0);
		}
		
		@Override
		public int getRowCount() {
			if(this.values == null) {
				return 0;
			}
			
			return this.values.size();
		}
		
		@Override
		public int getColumnCount() {
			if(this.values == null) {
				return 0;
			}
			return NAMES.length;
		}
		
		@Override
		public Object getValueAt(final int row, final int column) {
			final Row curRow = this.values.get(row);
			if(column == 0) {
				return curRow.getWMove();
			}else if(column == 1) {
				return curRow.getBMove();
			}
			return null;
		}
		
		@Override
		public void setValueAt(final Object aValue,final int row, final int column) {
			final Row curRow;
			if(this.values.size() <= row) {
				curRow = new Row();
				this.values.add(curRow);
			}else {
				curRow = this.values.get(row);
			}
			
			if(column == 0) {
				curRow.setWMove((String) aValue);
				fireTableRowsInserted(row, row);
			}else if(column == 1) {
				curRow.setBMove((String)aValue);
			    fireTableCellUpdated(row, column);
			}
		}
		
		@Override
		public Class<?> getColumnClass(final int column){
			return Move.class;
		}
		
		@Override
		public String getColumnName(final int column) {
			return NAMES[column];
		}
	}
	
	
	private static class Row{
		private String wMove;
		private String bMove;
		
		Row(){
			
		}
		
		public String getWMove() {
			return this.wMove;
		}
		
		public String getBMove() {
			return this.bMove;
		}
		
		public void setWMove(final String move) {
			this.wMove = move;
		}
		
		public void setBMove(final String move) {
			this.bMove = move;
		}
	}
	
}
