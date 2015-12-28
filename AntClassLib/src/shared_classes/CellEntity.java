package shared_classes;

import java.awt.Graphics;

public interface CellEntity {

	/**
	 * Get location in rows and columns of a board object
	 * @return location
	 */
	public Location getLocation();

	/**
	 * Set current location of an Object to the provided tuple of row and column
	 * @param row
	 * @param col
	 */
	public void changeLocation(int row, int col);

	/**
	 * Get the type of the Cell Entity as a String value
	 * @return type
	 */
	public String getEntityType();

	/**
	 * Get a size of an Object to be drawn on board
	 */
	public int getDrawingSize();

	/**
	 * Draw the object on board
	 * @param g - Graphics
	 * @param sizecol - size of a column in pixels
	 * @param sizerow - size of a row in pixels
	 * @param board - board to be drawn
	 */
	public void drawOnBoard(Graphics g, int sizecol, int sizerow, Board board);
}
