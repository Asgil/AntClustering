package shared_classes;

import java.io.Serializable;

public class Location implements Serializable {

	private static final long serialVersionUID = -6171531016967658900L;

	private int column;
	private int row;

	// invalid location
	public Location() {
		setRow(-1);
		setColumn(-1);
	}

	/**
	 * Constructor for a location class object
	 * @param row
	 * @param column
	 */
	public Location(int row, int column) {
		setRow(row);
		setColumn(column);
	}

	/**
	 * Get the column number of an Object
	 * @return column number
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Set a column number for an Object
	 * @param column number
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * Get the row number of an Object
	 * @return row number
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Set a row number for an Object
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return "[" + row + ", " + column + "]";
	}
}
