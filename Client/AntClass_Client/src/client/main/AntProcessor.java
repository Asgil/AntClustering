package client.main;

import java.rmi.RemoteException;
import java.util.LinkedList;

import shared_classes.Ant;
import shared_classes.Board;
import shared_classes.Cell;
import shared_classes.Heap;
import shared_classes.IRemoteAnt;
import shared_classes.Location;

public class AntProcessor {

	/**
	 * Move an Ant on the board with randomly generated probability on hDirection and/or vDirection
	 * 
	 * @param antLocation
	 *            - current Ant location
	 * @param boardRows
	 *            - number of rows
	 * @param boardColumns
	 *            - number of columns
	 * @param board
	 *            - Current Instance of a board
	 * @return - New location of an Ant
	 * @throws RemoteException
	 */
	public Location move(Location antLocation, int boardRows, int boardColumns, Board board) throws RemoteException {
		for (int counter = 0; counter < 8; ++counter) { // to make sure the blocked ant (by other ants and heaps) will not loop
														// forever
			float horizontalDirection = (float) Math.random();
			float verticalDirection = (float) Math.random();
			int row;
			int column;
			if (horizontalDirection >= 0.5) {
				row = (antLocation.getRow() + 1) % boardRows;
			} else {
				row = (antLocation.getRow() - 1 + boardRows) % boardRows;
			}
			if (verticalDirection >= 0.5) {
				column = (antLocation.getColumn() + 1) % boardColumns;
			} else {
				column = (antLocation.getColumn() - 1 + boardColumns) % boardColumns;
			}

			// check if new cell contains a heap
			if (board.getCellObjectType(row, column).equalsIgnoreCase("empty")) {
				return new Location(row, column);
			} else { // move in a new direction if we saw a heap it should not move on top of a heap
				continue;
			}
		}
		return antLocation;
	}

	/**
	 * Returns a nearby heap location if found any, otherwise returns null Index parameter is the index of the ant in the ants[]
	 * array which needs to look around itself
	 * 
	 * @param ant
	 *            - Currently processed Ant
	 * @param board
	 *            - Current instance of a board
	 * @return Heap location on the board if found around the Ant, null otherwise
	 */
	public Location lookAround(Ant ant, Board board) {
		Location resultLocation = new Location();

		// direction to look at
		int hDirection[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
		int vDirection[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

		Cell[][] cells = board.getBoardCells();
		int rows = board.getRows();
		int columns = board.getColumns();

		for (int i = 0; i < hDirection.length; i++) {
			int lookatH = ant.getLocation().getRow() + hDirection[i];
			int lookatV = ant.getLocation().getColumn() + vDirection[i];
			if (lookatH >= 0) {
				lookatH %= rows;
			} else if (lookatH < 0) {
				lookatH = rows - 1;
			}

			if (lookatV >= 0) {
				lookatV %= columns;
			} else if (lookatV < 0) {
				lookatV = columns - 1;
			}
			if (cells[lookatH][lookatV].getEntityType().equals("heap")) {
				resultLocation.setRow(lookatH);
				resultLocation.setColumn(lookatV);
				return resultLocation;
			} else {
				continue;
			}
		}
		return null;
	}

	/**
	 * Looking for an Empty Cell to place the HeapObject
	 * 
	 * @param ant
	 * @param board
	 * @return Location of an EmptyCell
	 */
	public Location lookAroundForEmpty(Ant ant, Board board) {
		Location resultLocation = new Location();

		// direction to look at
		int hDirection[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
		int vDirection[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

		Cell[][] cells = board.getBoardCells();
		int rows = board.getRows();
		int columns = board.getColumns();

		for (int i = 0; i < hDirection.length; i++) {
			int lookatH = ant.getLocation().getRow() + hDirection[i];
			int lookatV = ant.getLocation().getColumn() + vDirection[i];
			if (lookatH >= 0) {
				lookatH %= rows;
			} else if (lookatH < 0) {
				lookatH = rows - 1;
			}

			if (lookatV >= 0) {
				lookatV %= columns;
			} else if (lookatV < 0) {
				lookatV = columns - 1;
			}
			if (cells[lookatH][lookatV].getEntityType().equals("empty")) {
				resultLocation.setRow(lookatH);
				resultLocation.setColumn(lookatV);
				return resultLocation;
			} else {
				continue;
			}
		}
		return null;
	}

	/**
	 * Algorithm to drop an Object on the empty cell or a heap
	 * 
	 * @param board
	 *            - current instance of a board
	 * @param heapLocation
	 *            - location of found heap
	 * @param ant
	 * @param antStub
	 * @return element that the ant has dropped
	 * @throws RemoteException
	 */
	public int processDropAlgorithm(Board board, Location heapLocation, Ant ant, IRemoteAnt antStub) throws RemoteException {
		synchronized (board) {
			// first, check if the heap is still there (because it may have disappeared because of other clients' ants)
			if (heapLocation == null
					|| board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].getEntityType() == null) {
				return ant.getHeapElementType();
			}
			// if
			// (board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].getEntityType().equalsIgnoreCase("empty"))
			// {
			//
			// // drop on EmptyCell
			// Heap heap = new Heap(heapLocation.getRow(), heapLocation.getColumn(), antStub.getTypesOfHeapObjects());
			// LinkedList<Integer> heapObjects = new LinkedList<Integer>();
			// heapObjects.add(ant.getHeapElementType());
			// heap.updateHeapObjects(heapObjects);
			// antStub.placeHeapOnBoard(heap);
			// return -1;
			// }

			Heap heap = null;
			LinkedList<Integer> heapObjects = null;
			try {
				heap = (Heap) board.getCellEntity(heapLocation);
				heapObjects = heap.getHeapObjects();
			} catch (Exception e) {
				// e.printStackTrace();
				return ant.getHeapElementType();
			}


			switch (heapObjects.size()) {
			case 0: // according to the reference .pdf file, there should be such a case
				return -1;
			case 1:
				// apply the drop algorithm according to the reference .pdf file
				if (heapObjects.get(0) == ant.getHeapElementType()) {
					heapObjects.add(ant.getHeapElementType());
					antStub.updateHeapOnBoard(heapLocation, heap); // update the heap object on the server side
					return -1;
				} else {
					return ant.getHeapElementType();
				}
			default:
				// apply the drop algorithm according to the reference .pdf file
				int firstHeapObject = heapObjects.getFirst();
				int lastHeapObject = heapObjects.getLast();
				if (firstHeapObject != lastHeapObject
						|| (firstHeapObject == lastHeapObject && firstHeapObject == ant.getHeapElementType())) {
					heapObjects.add(ant.getHeapElementType());
					heap.updateHeapObjects(heapObjects);
					antStub.updateHeapOnBoard(heapLocation, heap); // update the heap object on the server side
					return -1;
				}
				return ant.getHeapElementType();
			}
		}
	}


	/**
	 * Algorithm to pick up an Object from the Heap
	 * 
	 * @param board
	 * @param heapLocation
	 * @param ant
	 * @param antStub
	 * @return type of the picked-up object
	 * @throws RemoteException
	 */
	public int processPickUpAlgorithm(Board board, Location heapLocation, Ant ant, IRemoteAnt antStub) throws RemoteException {
		synchronized (board) {
			// first, check if the heap is still there (because it may have disappeared because of other clients' ants)
			if (!board.getBoardCells()[heapLocation.getRow()][heapLocation.getColumn()].getEntityType().equalsIgnoreCase("heap")) {
				return -1; // return an empty object (carrying nothing)
			}

			Heap heap = (Heap) board.getCellEntity(heapLocation);
			LinkedList<Integer> heapObjects = heap.getHeapObjects();
			int heapObjectElement = -1; // return -1 if it is not carrying anything
			switch (heapObjects.size()) {
			case 0: // nothing to do [but remove heap from board]
				antStub.destroyHeapOnBoard(heapLocation);
				break;
			case 1:// pickup object, destroy heap
				heapObjectElement = heapObjects.remove(0); // set type of object to be returned
				antStub.destroyHeapOnBoard(heapLocation);
				break;
			case 2:
				// pickup if the two heapObjects are not same
				if (heapObjects.get(0) != heapObjects.get(1)) {
					heapObjectElement = heapObjects.remove(0);
					// set type of object to be returned
					heap.updateHeapObjects(heapObjects);
					antStub.updateHeapOnBoard(heapLocation, heap); // update the heap object on the server side
				} else {
					heapObjectElement = -1;
				}
				break; // return the type of the picked-up object
			default:
				int firstHeapObject = heapObjects.getFirst();
				int lastHeapObject = heapObjects.getLast();
				if (firstHeapObject != lastHeapObject) {
					heapObjectElement = heapObjects.remove(0);
					heap.updateHeapObjects(heapObjects);
					antStub.updateHeapOnBoard(heapLocation, heap); // update the heap object on the server side
				}
			}
			return heapObjectElement; // return the type of the picked-up object
		}
	}

}
