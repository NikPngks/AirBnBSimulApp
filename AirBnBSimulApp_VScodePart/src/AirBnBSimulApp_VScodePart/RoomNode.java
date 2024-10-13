package AirBnBSimulApp_VScodePart;

import java.io.Serializable;
import java.util.NoSuchElementException;

public class RoomNode implements Serializable {
	private static final long serialVersionUID = 8626459955423694374L;
	private RoomNode headRoomNode = null; // the root of the linked list
	Room roomItem = null;
	private RoomNode nextRoomNode = null;
	private RoomNode prevRoomNode = null;
	int N; // counter to track the number of nodes in the list
	private RoomNode emptyForToSearchName = null;
	private RoomNode emptyForSearchIdFilters = null;
	private AreaBookings[] prwthAreaBookings = null;// array to hold area bookings

	// Check if the list is empty
	public boolean isEmpty() {
		if (this.headRoomNode == null) {
			return true;
		} else {
			return false;
		}
	}

	// Add the rooms at the beginning of the list
	public void addRoom(Room room) {
		RoomNode node = new RoomNode(room); // create a new RoomNode
		if (this.headRoomNode == null || isEmpty()) { // if the list is empty
			this.headRoomNode = node; // set the new node as the head
			this.N = 1;// increment node count
		} else {
			node.setPrevRoomNode(this.headRoomNode);// link the new node to the current head
			this.headRoomNode.setNextRoomNode(node);// set the new node as the next of the current head
			this.headRoomNode = node; // update head to the new node
			this.N++;
		}
	}

	public RoomNode getFirst() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("The room list is empty.");
		} else {
			return getHeadRoomNode();
		}
	}

	public RoomNode getHeadRoomNode() {
		return headRoomNode;
	}

	public void setHeadRoomNode(RoomNode headRoomNode) {
		this.headRoomNode = headRoomNode;
	}

	public RoomNode(Room Item) {
		this.roomItem = Item;
		this.nextRoomNode = null;
		this.prevRoomNode = null;
	}

	public RoomNode() {
		this.roomItem = null;
		this.nextRoomNode = null;
		this.prevRoomNode = null;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public Room getRoomItem() {
		return roomItem;
	}

	public void setRoomItem(Room roomItem) {
		this.roomItem = roomItem;
	}

	public RoomNode getNextRoomNode() {
		return this.nextRoomNode;
	}

	public void setNextRoomNode(RoomNode nextRoomNode) {
		this.nextRoomNode = nextRoomNode;
	}

	public RoomNode getPrevRoomNode() {
		return this.prevRoomNode;
	}

	public void setPrevRoomNode(RoomNode prevRoomNode) {
		this.prevRoomNode = prevRoomNode;
	}

	public AreaBookings[] getPrwthAreaBookings() {
		return prwthAreaBookings;
	}

	public void setPrwthAreaBookings(AreaBookings[] prwthAreaBookings) {
		this.prwthAreaBookings = prwthAreaBookings;
	}

	// Method that finds a room based on its name
	public Room searchByRoomName(String roomName) {
		RoomNode tempItem = getFirst();
		for (int i = 0; i < this.N; i++) {
			if (tempItem.getRoomItem().getRoomName().equals(roomName)) {
				return tempItem.getRoomItem();
			}
			tempItem = tempItem.getPrevRoomNode();
		}
		return emptyForToSearchName.getRoomItem();
	}

	public RoomNode searchByManagerId(int idManager) {
		emptyForSearchIdFilters = new RoomNode();// create an empty node to store results
		RoomNode tempItem = getFirst(); // start the search from the first node
		for (int i = 0; i < this.N; i++) {
			if (tempItem.getRoomItem().getIdManager() == idManager) {
				emptyForSearchIdFilters.addRoom(tempItem.getRoomItem());
			}
			tempItem = tempItem.getPrevRoomNode();
		}
		return emptyForSearchIdFilters;
	}

	// This method will apply the user's filters to search for rooms
	public RoomNode searchBasedOnFilters(Filters filter) {
		emptyForSearchIdFilters = new RoomNode();// create a node to store results
		RoomNode tempItem = getFirst(); // start from the first node
		for (int i = 0; i < this.N; i++) {
			int j = -1;
			if (tempItem.getRoomItem().getArea().equals(filter.getArea()[0])
					|| tempItem.getRoomItem().getArea().equals(filter.getArea()[1])
					|| tempItem.getRoomItem().getArea().equals(filter.getArea()[2])) {
				if (tempItem.getRoomItem().getPrice() <= filter.getMaxPrice()) {
					if (tempItem.getRoomItem().getPrice() >= filter.getMinPrice()) {
						if (tempItem.getRoomItem().getStars() >= filter.getStars()) {
							if (tempItem.getRoomItem().getNoOfPerson() >= filter.getNoOfPerson()) {
								if (tempItem.getRoomItem().getAvailability_365()[filter.getReservationStart()] == 1) {
									for (j = 0; j < filter.getNoOfBookedDays(); j++) {
										if (tempItem.getRoomItem().getAvailability_365()[j] != 1) {
											/*
											 * here I am essentially giving a negative value to j at the beginning
											 * to make it look like it didn't run the whole loop and I start doing the
											 * checks.
											 */
											j = filter.getNoOfBookedDays() + 5;
											/* if I find a room that meets all the criteria */
										}
										/*
										 * at the end I tell him to check the availability, if it is available for
										 * the days I want then the j will stay negative
										 * as I set it, otherwise it will be positive
										 */
									}
								}
							}
						}
					}
				}
			}
			if (j > -1 && j == filter.getNoOfBookedDays()) { // this means that it reached the last if and that it also
																// looped finding only aces that show availability
				emptyForSearchIdFilters.addRoom(tempItem.getRoomItem());
			}
			tempItem = tempItem.getPrevRoomNode();
		}
		return emptyForSearchIdFilters;
	}

	// Generates a report of reservations based on the specified period
	AreaBookings[] reservationsReport(int firstPeriodDay, int lastPeriodDay) {
		AreaBookings[] finalReportInArray = new AreaBookings[this.N];
		RoomNode tempItem = this.getHeadRoomNode(); // start from the head node
		for (int i = 0; i < this.N; i++) {// iterate through each room node
			for (int j = 0; j < this.N; j++) {// iterate through the report array
				// Check if the current report entry is null
				if (finalReportInArray[j] == null) {
					finalReportInArray[j] = new AreaBookings();// create a new report entry
					finalReportInArray[j].setArea(tempItem.getRoomItem().getArea());// set the area
					int sumRoom = 0;// counter for reservations
					// Check availability for the specified period
					for (int y = firstPeriodDay; y < lastPeriodDay; y++) {
						if (tempItem.getRoomItem().getAvailability_365()[y] == 2) {
							sumRoom++;// increment count if a reservation exists
						}
					}
					finalReportInArray[j].setSumReservations(sumRoom);
					j = this.N;
				} else if (finalReportInArray[j].area.equals(tempItem.getRoomItem().getArea())) {
					int sumRoom = 0;
					for (int y = firstPeriodDay; i < lastPeriodDay; i++) {
						if (tempItem.getRoomItem().getAvailability_365()[y] == 2) {
							sumRoom = sumRoom + 1;
						}
					}
					finalReportInArray[j].setSumReservations(finalReportInArray[j].getSumReservations() + sumRoom);
					j = this.N;
				}
			}
			tempItem = tempItem.getPrevRoomNode();
		}
		return finalReportInArray;
	}
}