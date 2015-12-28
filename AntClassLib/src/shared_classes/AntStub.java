package shared_classes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;

public class AntStub extends UnicastRemoteObject implements IRemoteAnt {

	private static final long serialVersionUID = 5500371245725480160L;

	private Ant[] ants;
	private int totalNumOfClients;
	private static int numOfActiveClients;
	private Board board;
	private GUI gui;
	private int clientID;

	private int typesOfHeapObjects;

	public AntStub(Ant[] ants, int totalNumOfClients, Board board, GUI gui, int typesOfHeapObjects) throws RemoteException {
		this.ants = ants;
		this.totalNumOfClients = totalNumOfClients;
		this.board = board;
		this.gui = gui;
		AntStub.numOfActiveClients = 0;
		this.typesOfHeapObjects = typesOfHeapObjects;
	}

	/**
	 * Returns the Ant object basing on the antIndex
	 * 
	 * @param Index
	 *            of currently processing Ant
	 */
	@Override
	public synchronized Ant getAnt(int antIndex) throws RemoteException {
		return ants[antIndex];
	}

	/**
	 * Sets the new location in rows and columns of a board for currently processed Ant
	 * 
	 * @param Current
	 *            location to be deleted from the board
	 * @param Next
	 *            location to be assigned
	 * @param Index
	 *            of a currently processed Ant
	 */
	@Override
	public synchronized void changeAntLocation(Location current, Location next, int antIndex) throws RemoteException {
		destroyAntOnBoard(current);
		ants[antIndex].getLocation().setColumn(next.getColumn());
		ants[antIndex].getLocation().setRow(next.getRow());
		placeAntOnBoard(ants[antIndex]);

	}

	/**
	 * Changing the status of an object carrying by an Ant. If Ant was not carrying the HeapObject - pick up an object, Else -
	 * drop an object
	 * 
	 * @param carryingObjectType
	 *            = -1 if an Ant is not carrying an object, otherwise - value of a HeapObject type
	 * @param antIndex
	 *            - Index of currently processed Ant
	 */
	@Override
	public synchronized void updateAntHeapObject(int antIndex, int carryingObjectType) throws RemoteException {
		if (carryingObjectType == -1) {
			ants[antIndex].dropCarryingHeapObject();
		} else {
			ants[antIndex].pickUpHeapObject(carryingObjectType);
		}
	}

	/**
	 * Updating the Heap object on board after it was processed by an Ant
	 * 
	 * @param heapLocation
	 *            - coordinates of a Heap to be changed in rows and columns of a board
	 * @param newHeap
	 *            - the List type object that represents the new structure of a Heap
	 */
	@Override
	public synchronized void updateHeapOnBoard(Location heapLocation, Heap newHeap) throws RemoteException {
		// check if the heap is there
		if (board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].getEntityOnCell().getEntityType()
				.equalsIgnoreCase("heap")) {
			Collections.sort(newHeap.getHeapObjects());
			board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].setEntityOnCell(newHeap);
		}
	}

	/**
	 * Deleting the Heap from the board
	 * 
	 * @param - coordinates of a Heap to be deleted in rows and columns of a board
	 */
	@Override
	public synchronized void destroyHeapOnBoard(Location heapLocation) throws RemoteException {
		// check if the heap is there
		if (board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].getEntityOnCell().getEntityType()
				.equalsIgnoreCase("heap")) {
			board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].setEntityOnCell(new EmptyCellEntity(
					heapLocation.getRow(), heapLocation.getColumn()));
		}
	}

	/**
	 * Placing a new heap on the board
	 * 
	 * @param heap
	 *            - the structure of a heap to be placed on a board
	 */
	public synchronized void placeHeapOnBoard(Heap heap) throws RemoteException {
		// check if the heap is there
		Location heapLocation = heap.getLocation();
		if (board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].getEntityOnCell().getEntityType()
				.equalsIgnoreCase("empty")) {
			board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].setEntityOnCell(heap);
		}
	}

	/**
	 * Get the width of the board in pixels
	 */
	@Override
	public synchronized int getBoardWidth() throws RemoteException {
		return board.getColumns();
	}

	/**
	 * Get the height of the board in pixels
	 */
	@Override
	public synchronized int getBoardHeight() throws RemoteException {
		return board.getRows();
	}

	/**
	 * Get the total number of ants
	 */
	@Override
	public synchronized int getTotalNumOfAnts() throws RemoteException {
		return ants.length;
	}

	/**
	 * Get the total number of clients
	 */
	@Override
	public synchronized int getTotalNumOfClients() throws RemoteException {
		return totalNumOfClients;
	}

	/**
	 * Get the Index of the first Ant to be processed by the current client
	 * 
	 * @param clientID
	 *            - ID of the currently running client, specified in MainClient manually
	 */
	@Override
	public synchronized int getStartIndexOfAnts(int clientID) throws RemoteException {
		// clientID should start from 0
		// return clientID * (getTotalNumOfAnts() / getTotalNumOfClients());
		return clientID * (getTotalNumOfAnts() / numOfActiveClients);
	}

	/**
	 * Get the Index of the last Ant to be processed by the current client
	 * 
	 * @param clientID
	 *            - ID of the currently running client, specified in MainClient manually
	 */
	@Override
	public synchronized int getEndIndexOfAnts(int clientID) throws RemoteException {
		// clientID should start from 0
		if (clientID + 1 == numOfActiveClients) { // check if this is the last client (i.e. with the highest ID)
			// assign until the last ant
			return getTotalNumOfAnts();
		} else {
			return (clientID + 1) * (getTotalNumOfAnts() / numOfActiveClients);
		}
	}

	/**
	 * Get the whole instance of current status of the board
	 */
	@Override
	public synchronized Board getBoard() throws RemoteException {
		return this.board;
	}

	/**
	 * Send a signal to the GUI to redraw the board with changes applied
	 */
	@Override
	public synchronized void requestRedraw() throws RemoteException {
		this.gui.repaint();
		try {
			wait(900); // force waiting period to synchronise after each repaint
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Assigning color to Ant dynamically, basing on the clientID and total number of clients
	 * 
	 * @param antIndex
	 *            - Index of currently processed Ant
	 * @param clientID
	 *            - ID of the currently running client, specified in MainClient manually
	 */
	@Override
	public synchronized void assignColorToAnt(int antIndex, int _clientID) throws RemoteException {
		this.clientID = _clientID;
		;
		ants[antIndex].assignColor((float) clientID / (float) numOfActiveClients);
	}

	/**
	 * Assigning clientID to the currently processed by this client Ant
	 * 
	 * @param antIndex
	 *            - Index of currently processed Ant
	 * @param clientID
	 *            - ID of the currently running client, specified in MainClient manually
	 */
	@Override
	public synchronized void assignAntsClientID(int antIndex, int clientID) throws RemoteException {
		ants[antIndex].assignAntsClientID(clientID);
	}

	/**
	 * Replacing the Ant object on board with EmptyCellEntity after each step of an Ant
	 * 
	 * @param oldLocation
	 *            - location of an Ant in rows and columns to be replaced by an empty cell
	 */
	@Override
	public synchronized void destroyAntOnBoard(Location oldLocation) throws RemoteException {
		if (board.getBoardCells()[oldLocation.getRow()][oldLocation.getColumn()].getEntityOnCell().getEntityType()
				.equalsIgnoreCase("ant")) {
			board.getBoardCells()[oldLocation.getRow()][oldLocation.getColumn()].setEntityOnCell(new EmptyCellEntity(oldLocation
					.getRow(), oldLocation.getColumn()));
		}

	}

	/**
	 * Placing the Ant Object on board
	 * 
	 * @param ant
	 *            - currently processed Ant
	 */
	@Override
	public synchronized void placeAntOnBoard(Ant ant) throws RemoteException {
		int r = ant.getLocation().getRow();
		int c = ant.getLocation().getColumn();
		if (board.getBoardCells()[r][c].getEntityOnCell().getEntityType().equalsIgnoreCase("empty")) {
			board.getBoardCells()[r][c].setEntityOnCell(ant);
		}

	}

	/**
	 * After new client has connected to the server - increasing the number of active clients
	 * 
	 * @param clientID
	 *            - ID of the currently running client, specified in MainClient manually
	 */
	@Override
	public synchronized void clientConnected(int clientID) throws RemoteException {
		++numOfActiveClients;
	}

	/**
	 * Return Number of types of heap objects
	 */
	@Override
	public synchronized int getTypesOfHeapObjects() throws RemoteException {
		return typesOfHeapObjects;
	}

}
