package main.view;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

class RegionsTableModel extends AbstractTableModel {

	private int columnCount;
	private ArrayList<String[]> data;
	private String[] columnName;
	
	RegionsTableModel(String[] columnName) {
		this.columnCount = columnName.length;
		data = new ArrayList<String[]>();
		this.columnName = columnName;
	}
	
	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String[] row =data.get(rowIndex);
		return row[columnIndex];
	}
	
	public void addRow(int n, int a, int p, double c, String centerMass, double e) {
		data.add(new String[]{Integer.toString(n), Integer.toString(a), Integer.toString(p), String.format("%1.2f", c), centerMass, String.format("%1.2f", e)});
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnName[columnIndex];
	}

}
