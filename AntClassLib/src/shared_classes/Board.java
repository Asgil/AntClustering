package shared_classes;

import java.io.Serializable;

public class Board implements Serializable {

	private static final long serialVersionUID = -8340202074843934794L;

	private Cell cells[][];
	private int rows;
	private int columns;

	/**
	 * Board constructor
	 * @param rows - number of rows
	 * @param columns - number of columns
	 * @param types - number of types of HeapObjects
	 * @param heapSize - maximum size of a Heap
	 */
	public Board(int rows, int columns, int types, int heapSize) {

		setRows(rows);
		setColumns(columns);
		
		cells = new Cell[rows][columns];
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				cells[i][j] = new Cell(i, j);
			}
		}
	}

	/**
	 * Get the number of rows
	 * @return rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Set the number of rows
	 * @param rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Get the number of columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Set the number of columns
	 * @param columns
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Get the Cells of the board
	 * @return
	 */
	public Cell[][] getBoardCells() {
		return cells;
	}

	/**
	 * Get the cellEntity based on this location
	 * @param location
	 * @return CellEntity
	 */
	public CellEntity getCellEntity(Location location) {
		int r = location.getRow();
		int c = location.getColumn();

		return cells[r][c].getEntityOnCell();
	}

	/**
	 *  Get the CellEntity Object type
	 * @param r - row
	 * @param c - column
	 * @return - ant | heap | empty
	 */
	public String getCellObjectType(int r, int c) {
		return cells[r][c].getEntityType();
	}

	/**
	 *  Get the CellEntity Object type
	 * @param location - tuple of row and column
	 * @return - ant | heap | empty
	 */
	public String getCellObjectType(Location location) {
		int r = location.getRow();
		int c = location.getColumn();

		return cells[r][c].getEntityType();
	}

	/**
	 * Destroy the deleted Heap from board by replacing it with EmptyCellEntity
	 * @param location
	 */
	public void destroyHeapOnCell(Location location) {
		if (!cells[location.getRow()][location.getColumn()].getEntityType().equalsIgnoreCase("heap")) {
			System.err.println("No heap found at " + location.toString());
			return;
		}
		cells[location.getRow()][location.getColumn()].setEntityOnCell(new EmptyCellEntity(location.getRow(), location
				.getColumn()));
	}
	
	/**
	 * Place a Heap Object Entity on Cell
	 * @param heap
	 */
	public void placeHeapOnCell(Heap heap) {
		int r = heap.getLocation().getRow();
		int c = heap.getLocation().getColumn();
		if ((r >= 0 && r < rows) && (c >= 0 && c < columns)) {
			cells[r][c].setEntityOnCell(heap);
		}
	}	

	/**
	 * Place Ant Object Entity on Cell
	 * @param ant
	 */
	public void placeAntOnCell(Ant ant) {
		int r = ant.getLocation().getRow();
		int c = ant.getLocation().getColumn();
		if ((r >= 0 && r < rows) && (c >= 0 && c < columns)) {
			cells[r][c].setEntityOnCell(ant);
		}
	}

	/**
	 * Delete the moved Ant from current cell by replacing it with EmptyCellEntity
	 * @param location
	 */
	public void destroyAntOnCell(Location location) {
		if (!cells[location.getRow()][location.getColumn()].getEntityType().equalsIgnoreCase("ant")) {
			System.err.println("No ant found at " + location.toString());
			return;
		}
		cells[location.getRow()][location.getColumn()].setEntityOnCell(new EmptyCellEntity(location.getRow(), location
				.getColumn()));

	}

}