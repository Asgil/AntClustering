package shared_classes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteAnt extends Remote {
	
	/**
	 * Returns the Ant object basing on the antIndex
	 * @param Index of currently processing Ant
	 */
	public Ant getAnt(int antIndex) throws RemoteException;

	/**
	 * Sets the new location in rows and columns of a board for currently processed Ant
	 * @param Current location to be deleted from the board
	 * @param Next location to be assigned
	 * @param Index of a currently processed Ant
	 */
	public void changeAntLocation(Location current, Location next, int antIndex) throws RemoteException;

	/**
	 * Changing the status of an object carrying by an Ant. If Ant was not carrying the HeapObject - pick up an object, 
	 * Else - drop an object
	 * @param carryingObjectType = -1 if an Ant is not carrying an object, otherwise - value of a HeapObject type
	 * @param antIndex - Index of currently processed Ant
	 */
	public void updateAntHeapObject(int antIndex, int carryingObjectType) throws RemoteException;

	/**
	 * Updating the Heap object on board after it was processed by an Ant
	 * @param heapLocation - coordinates of a Heap to be changed in rows and columns of a board
	 * @param newHeap - the List type object that represents the new structure of a Heap
	 */
	public void updateHeapOnBoard(Location heapLocation, Heap newHeap) throws RemoteException;

	/**
	 * Deleting the Heap from the board
	 * @param - coordinates of a Heap to be deleted in rows and columns of a board
	 */
	public void destroyHeapOnBoard(Location heapLocation) throws RemoteException;

	/**
	 * Placing a new heap on the board
	 * @param heap - the structure of a heap to be placed on a board
	 */
	public void placeHeapOnBoard(Heap heap) throws RemoteException;

	/**
	 * Get the width of the board in pixels
	 */
	public int getBoardWidth() throws RemoteException;

	/**
	 * Get the height of the board in pixels
	 */
	public int getBoardHeight() throws RemoteException;

	/**
	 * Get the total number of ants
	 */
	public int getTotalNumOfAnts() throws RemoteException;

	/**
	 * Get the total number of clients
	 */
	public int getTotalNumOfClients() throws RemoteException;

	/**
	 * Get the Index of the first Ant to be processed by the current client
	 * @param clientID - ID of the currently running client, specified in MainClient manually 
	 */
	public int getStartIndexOfAnts(int clientID) throws RemoteException;

	/**
	 * Get the Index of the last Ant to be processed by the current client
	 * @param clientID - ID of the currently running client, specified in MainClient manually 
	 */
	public int getEndIndexOfAnts(int clientID) throws RemoteException;

	/**
	 * Get the whole instance of current status of the board
	 */
	public Board getBoard() throws RemoteException;

	/**
	 * Send a signal to the GUI to redraw the board with changes applied 
	 */
	public void requestRedraw() throws RemoteException;

	/**
	 * Assigning color to Ant dynamically, basing on the clientID and total number of clients
	 * @param antIndex - Index of currently processed Ant
	 * @param clientID - ID of the currently running client, specified in MainClient manually 
	 */
	public void assignColorToAnt(int antIndex, int clientID) throws RemoteException;

	/**
	 * Assigning clientID to the currently processed by this client Ant
	 * @param antIndex - Index of currently processed Ant
	 * @param clientID - ID of the currently running client, specified in MainClient manually 
	 */
	public void assignAntsClientID(int antIndex, int clientID) throws RemoteException;

	/**
	 * Replacing the Ant object on board with EmptyCellEntity after each step of an Ant
	 * @param oldLocation - location of an Ant in rows and columns to be replaced by an empty cell
	 */
	public void destroyAntOnBoard(Location oldLocation) throws RemoteException;

	/**
	 * Placing the Ant Object on board
	 * @param ant - currently processed Ant
	 */
	public void placeAntOnBoard(Ant ant) throws RemoteException;

	/**
	 * After new client has connected to the server - increasing the number of active clients
	 * @param clientID - ID of the currently running client, specified in MainClient manually 
	 */
	public void clientConnected(int clientID) throws RemoteException;

	/**
	 * Return Number of types of heap objects
	 */
	public int getTypesOfHeapObjects() throws RemoteException;
}