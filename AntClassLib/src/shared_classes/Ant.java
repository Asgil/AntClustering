package shared_classes;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Ant implements Serializable, CellEntity {

	private static final long serialVersionUID = -6819460915834657895L;

	private Location location;
	private boolean isCarrying;
	private int carryingHeapObject; // -1 when not isCarrying any object
	private float color;
	private int totalNumberOfTypesOfHeapObjects;
	private int clientID;
	
	//Declare an Array of Colors
	private static Color[] ColorArray = {Color.black, Color.blue, Color.cyan, 		
		Color.green, Color.lightGray, Color.magenta, Color.darkGray, 
		Color.orange, Color.pink, Color.red, Color.gray, Color.yellow};

	/**
	 * Constructor for an Ant
	 * @param r - row
	 * @param c - column
	 * @param _color
	 * @param _typesOfObjects
	 */
	public Ant(int r, int c, float _color, int _typesOfObjects) {
		location = new Location(r, c);
		this.isCarrying = false;
		this.carryingHeapObject = -1;
		this.color = _color;
		this.totalNumberOfTypesOfHeapObjects = _typesOfObjects;

	}
	public Ant(int r, int c){
		location = new Location(r, c);
		this.isCarrying = false;
		this.carryingHeapObject = -1;
	};

	/**
	 * Get the status of an Ant
	 * @return true if Carrying in Object, false otherwise
	 */
	public boolean isCarrying() {
		return isCarrying;
	}

	/**
	 * Get the type of an Element that Ant is Carrying
	 * @return
	 */
	public int getHeapElementType() {
		// -1 if ant is not isCarrying anything
		return carryingHeapObject;
	}

	/**
	 * Set the status isCarrying to false
	 * Set the type of carried object to -1
	 */
	public void dropCarryingHeapObject() {
		isCarrying = false;
		carryingHeapObject = -1;
	}

	/**
	 * Ant picks up an Object - status isCarryng is true
	 * Type of carried HeapObject is set the the type of HeapObject
	 * @param heapObject
	 */
	public void pickUpHeapObject(int heapObject) {
		isCarrying = true;
		this.carryingHeapObject = heapObject;
	}


	@Override
	public String getEntityType() {
		return "ant";
	}

	@Override
	public int getDrawingSize() {
		return 35;
	}

	@Override
	public void drawOnBoard(Graphics g, int sizecol, int sizerow, Board board) {
		//For first 12 clients the color of their Ants is chosen from the predefined array of colors, basing on the client ID
		//This makes colors more distinct from each other
		if (clientID < 12) {
			g.setColor(ColorArray[clientID]);
		} else {
			//If there are more than 12 clients - colors are assigned dynamically
			//Assignment is based on the clientID and total possible number of clients
			Color colorAnt = new Color((int) ((0.7) * 255), (int) ((color) * 255), (int) ((color/2.0) * 255));
			g.setColor(colorAnt);
		}
		int y = getLocation().getRow() * sizerow;
		int x = getLocation().getColumn() * sizecol;

		g.fillOval((int) (x + (sizecol - getDrawingSize() * 0.02 * Math.min(sizecol, sizerow)) / 2.0),
				(int) (y + (sizerow - getDrawingSize() * 0.02 * Math.min(sizecol, sizerow)) / 2.0),
				(int) (getDrawingSize() * (0.02 * Math.min(sizecol, sizerow))),
				(int) (getDrawingSize() * (0.02 * Math.min(sizecol, sizerow))));
		
		//Drawing heap object on Ant if isCarrying
		if (isCarrying) {
			//Calculating the color of HeapObject to be drawn
			float colorheapvalue = (float) carryingHeapObject / (float) totalNumberOfTypesOfHeapObjects;
			Color colorHeap = new Color((int) ((colorheapvalue) * 255), (int) ((colorheapvalue) * 255), (int) ((0.7) * 255));
			g.setColor(colorHeap);
			g.fillRect((int) (x + (sizecol - getDrawingSize() / 2 * 0.02 * Math.min(sizecol, sizerow)) / 2.0),
					(int) (y + (sizerow - getDrawingSize() / 2 * 0.02 * Math.min(sizecol, sizerow)) / 2.0),
					(int) (getDrawingSize() / 2 * (0.02 * Math.min(sizecol, sizerow))),
					(int) (getDrawingSize() / 2 * (0.02 * Math.min(sizecol, sizerow))));
			g.setColor(Color.white);
			g.drawRect((int) (x + (sizecol - getDrawingSize() / 2 * 0.02 * Math.min(sizecol, sizerow)) / 2.0),
					(int) (y + (sizerow - getDrawingSize() / 2 * 0.02 * Math.min(sizecol, sizerow)) / 2.0),
					(int) (getDrawingSize() / 2 * (0.02 * Math.min(sizecol, sizerow))),
					(int) (getDrawingSize() / 2 * (0.02 * Math.min(sizecol, sizerow))));
		}
	}

	/**
	 * Assign a color for an Ant
	 * @param _color
	 */
	public void assignColor(float _color) {
		color = _color;
	}
	
	/**
	 * Assign a clientID for an Ant
	 * @param _clientID
	 */
	public void assignAntsClientID(int _clientID){
		clientID = _clientID;
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

	/*public void printStatus() {
		System.out.println(isCarrying + ":" + carryingHeapObject);
	}*/

}
