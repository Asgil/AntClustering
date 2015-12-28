package shared_classes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.Serializable;

import javax.swing.JPanel;

public class GUI extends JPanel implements ActionListener, Serializable {

	private static final long serialVersionUID = -6465327145108762100L;

	private Board board;
	private int width, height;
	private int sizecol = 0;
	private int sizerow = 0;

	/**
	 * Constructor for GUI
	 * @param _board Initialised object
	 * @param _rows - Number of rows
	 * @param _cols - Number of columns
	 * @param _width - In pixels
	 * @param _height - In pixels
	 */
	public GUI(Board _board, int _rows, int _cols, int _width, int _height) {
		this.board = _board;
		this.width = _width;
		this.height = _height;
		sizecol = width / _cols;
		sizerow = height / _rows;

	}

	/**
	 * Updating the screen size if it is changed by a user
	 */
	private void updateScreenSize() {
		int widthnew = getWidth();
		int heightnew = getHeight();

		// recalculate the column/row sizes to have them match the resized window area
		sizecol = widthnew / board.getColumns();
		sizerow = heightnew / board.getRows();
		width = widthnew;
		height = heightnew;
	}

	/**
	 *  Recreating board with new width and height
	 */
	public void actionPerformed(ActionEvent e) {		
		repaint();
	}

	/**
	 * Get new width on Action event performed
	 * @param e
	 * @return new width
	 */
	public int getWidth(ActionEvent e) {
		// get screen size
		int widthnew = getWidth();
		return widthnew;
	}

	/**
	 * Get new height on Action event performed
	 * @param e
	 * @return new height
	 */
	public int getHeight(ActionEvent e) {
		// get screen size
		int heightnew = getHeight();
		return heightnew;
	}

	/**
	 * Drawing the board on JFrame
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		updateScreenSize();

		Graphics2D g2 = (Graphics2D) g;

		// Drawing lines
		for (int i = 0; i < board.getRows(); i++) {
			Line2D lin = new Line2D.Float(0, i * sizerow, width, i * sizerow);
			g2.draw(lin);
		}
		for (int j = 0; j < board.getColumns(); j++) {
			Line2D lin = new Line2D.Float(j * sizecol, 0, j * sizecol, height);
			g2.draw(lin);
		}

		// Traversing the board and drawing each entity on it (if any)
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				board.getBoardCells()[i][j].getEntityOnCell().drawOnBoard(g, sizecol, sizerow, board);
			}
		}
	}

}
