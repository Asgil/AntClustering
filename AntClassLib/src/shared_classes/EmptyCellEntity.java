package shared_classes;

import java.awt.Graphics;
import java.io.Serializable;

public class EmptyCellEntity implements Serializable, CellEntity {

	private static final long serialVersionUID = 5108214842997643772L;

	private Location location;

	public EmptyCellEntity(int r, int c) {
		this.location = new Location(r, c);

	}

	@Override
	public int getDrawingSize() {
		return 0;
	}

	@Override
	public void drawOnBoard(Graphics g, int sizecol, int sizerow, Board board) {
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void changeLocation(int row, int col) {
		this.location.setColumn(col);
		this.location.setRow(row);
	}

	@Override
	public String getEntityType() {
		return "empty";
	}

}
