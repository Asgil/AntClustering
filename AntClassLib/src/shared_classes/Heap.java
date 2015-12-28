package shared_classes;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class Heap implements Serializable, CellEntity {

	private static final long serialVersionUID = 6933443421222451481L;

	private Location location;
	private LinkedList<Integer> heapObjects;
	private int heapObjectTypes; // the different types of heap elements

	/**
	 * Constructor for a HeapObject
	 * 
	 * @param r
	 *            - row number
	 * @param c
	 *            - column number
	 * @param heapObjectTypes
	 *            - number of types of HeapObjects
	 */
	public Heap(int r, int c, int heapObjectTypes) {
		location = new Location(r, c);
		this.heapObjectTypes = heapObjectTypes;
		heapObjects = new LinkedList<Integer>();
	}

	public Heap(int r, int c, int size, int types) {
		location = new Location(r, c);
		this.heapObjectTypes = types;
		heapObjects = new LinkedList<Integer>();

		// populating the heap
		for (int i = 0; i < size; i++) {
			heapObjects.add((int) (Math.random() * types));
		}

		Collections.sort(heapObjects);
	}

	/**
	 * Get the number of HeapObject in one heap (size of a Heap)
	 * 
	 * @return size
	 */
	public int getNumberOfHeapObjects() {
		return heapObjects.size();
	}

	/**
	 * Return the HeapObject
	 * 
	 * @return HeapObject
	 */
	public LinkedList<Integer> getHeapObjects() {
		return heapObjects;
	}

	/**
	 * Return an upper bound on the types of HeapObjects
	 * 
	 * @return number of predefined types of HeapObjects
	 */
	public int getHeapObjectTypes() {
		return heapObjectTypes;
	}

	@Override
	public String getEntityType() {
		return "heap";
	}

	@Override
	public int getDrawingSize() {
		return 30;
	}

	@Override
	public void drawOnBoard(Graphics g, int sizecol, int sizerow, Board board) {
		// Drawing heap objects of different types with different colors
		// Heap heap = (Heap) board.getBoardCells()[r][c].getEntityOnCell();
		if (getNumberOfHeapObjects() == 0) {
			int y = getLocation().getRow() * sizerow;
			int x = getLocation().getColumn() * sizecol;
			g.fillRect((int) (x * (sizecol / getNumberOfHeapObjects())), (int) (y + (sizerow * 0.1)), sizecol
					/ getNumberOfHeapObjects(), (int) (sizerow * 0.8));
			g.setColor(Color.red);
		} else {
			for (int k = 0; k < getNumberOfHeapObjects(); k++) {
				// System.err.println("elements.get(k) = " + elements.get(k) + " | getTypes() = " + getTypes());
				Color colorHeap = new Color((int) (((float) heapObjects.get(k) / (float) getHeapObjectTypes()) * 255),
						(int) (((float) heapObjects.get(k) / (float) getHeapObjectTypes()) * 255), (int) ((0.7) * 255));
				g.setColor(colorHeap);
				int y = getLocation().getRow() * sizerow;
				int x = getLocation().getColumn() * sizecol;
				g.fillRect((int) (x + (k) * (sizecol / getNumberOfHeapObjects())), (int) (y + (sizerow * 0.1)), sizecol
						/ getNumberOfHeapObjects(), (int) (sizerow * 0.8));
				// Drawing an edge for a heap element, to differentiate their quantity
				g.setColor(Color.white);
				g.drawRect((int) (x + (k) * (sizecol / getNumberOfHeapObjects())), (int) (y + (sizerow * 0.1)), sizecol
						/ getNumberOfHeapObjects(), (int) (sizerow * 0.8));
			}
		}
	}

	// modified after the pickup and dropping operations
	public void updateHeapObjects(LinkedList<Integer> heap) {
		this.heapObjects = new LinkedList<Integer>();
		for (int i = 0; i < heap.size(); i++) {
			this.heapObjects.add(heap.get(i));
		}
		Collections.sort(heapObjects);
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
}
