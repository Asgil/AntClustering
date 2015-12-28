package shared_classes;

import java.io.Serializable;


public class Cell implements Serializable {

	private static final long serialVersionUID = 6305343068366336423L;

	private CellEntity cellEntity;

	public Cell(int r, int c) {
		cellEntity = new EmptyCellEntity(r, c);
	}

	/**
	 * Get the type of CellEntity as a String value
	 * @return type
	 */
	public String getEntityType() {
		return cellEntity.getEntityType();
	}

	/**
	 * Get a CellEntity object on Cell
	 * @return CellEntity
	 */
	public CellEntity getEntityOnCell() {
		return cellEntity;
	}

	/**
	 * Place a passed CellEntity on current cell
	 * @param cellEntity
	 */
	public void setEntityOnCell(CellEntity cellEntity) {
		this.cellEntity = null;
		this.cellEntity = cellEntity;
	}

}
