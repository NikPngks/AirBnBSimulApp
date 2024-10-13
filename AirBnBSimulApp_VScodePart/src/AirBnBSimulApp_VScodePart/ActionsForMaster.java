package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ActionsForMaster extends Thread {
	private static final long serialVersionUID = 8626459955423694374L;
	ObjectInputStream in;// Input stream to receive data from the manager or user
	ObjectOutputStream out;// Output stream to send data back to the manager or user
	RoomNode headRoomNode;// Head node of the room linked list for managing room data
	int noOfWorkers;

	public ActionsForMaster(Socket connection, RoomNode headRoomNode) {
		try {
			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());
			this.headRoomNode = headRoomNode; // Set the headRoomNode
			this.noOfWorkers = noOfWorkers;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// Initialize variables for communication with the reducer
			Socket requestToReducerSocket = null; // Socket for sending requests to the reducer
			ObjectOutputStream out3 = null; // Output stream for the reducer
			ObjectInputStream in3 = null; // Input stream for the reducer
			RoomNode forSearchIdFilters = null;// Node to store results of room searches
			System.out.println("Master's request is caught, please wait");
			// Receive the package from the master
			Object masterPack = (Object) in.readObject();
			// Check if the received package is an instance of ManagerPack or UserPack
			if (masterPack instanceof ManagerPack) { // if it is managerPack then..
				ManagerPack masterPackNew = (ManagerPack) masterPack;
				System.out.println("Master's request is from manager " + masterPackNew.getIdManager());
				// Perform actions based on the manager's selected action indicator
				if (masterPackNew.getManagerSelectIndicator() == 1) {// add rooms
					/* THIS DOES NOT NEED TO GO TO THE REDUCER */
					RoomNode tempRmNd = new RoomNode();
					tempRmNd = masterPackNew.getTempItem().getHeadRoomNode();// Get the list of rooms to add
					while (tempRmNd != null) {
						if (tempRmNd.getRoomItem() != null) {
							// Synchronize access to headRoomNode while adding rooms
							synchronized (headRoomNode) {
								headRoomNode.addRoom(tempRmNd.getRoomItem());
							}
							tempRmNd = tempRmNd.getPrevRoomNode();// Move to the previous room node
						}
					}
					// Send success message back to the manager
					String workerAnswer = "Room registration successful";
					out.writeObject(workerAnswer); // Send this message to the master
					out.flush();

				} else if (masterPackNew.getManagerSelectIndicator() == 2) { // Add available dates to a room
					/* THIS DOES NOT NEED TO GO TO THE REDUCER */
					String tempRoomNamePaketou = masterPackNew.getRoomNameForChangeAvailability();
					Room wantedRoom = headRoomNode.searchByRoomName(tempRoomNamePaketou);
					if (wantedRoom.getRoomName() != null) {// Check if the room exists
						int firstDayAvailable = masterPackNew.getFirstDayAvailable();
						int noOfAvailableDays = masterPackNew.getNoOfAvailableDays();
						synchronized (headRoomNode) {
							wantedRoom.setavailability_365(firstDayAvailable, noOfAvailableDays);// Set availability
						}
						// Send success message back to the manager
						String workerAnswer = "Room update successful";
						out.writeObject(workerAnswer);
						out.flush();
					} else {// Room does not exist
						String workerAnswer = "The room does not exist";
						out.writeObject(workerAnswer);
						out.flush();
						System.out.println("The room does not exist");
					}
				} else if (masterPackNew.getManagerSelectIndicator() == 3) { // Display reservations for the manager's
																				// property
					int idManager = masterPackNew.getIdManager();
					forSearchIdFilters = headRoomNode.searchByManagerId(idManager); // Search for rooms managed by the
																					// manager
					// Prepare to send the package to the reducer
					if (forSearchIdFilters.getHeadRoomNode() != null) {
						if (forSearchIdFilters.getHeadRoomNode().getRoomItem() != null) {
							masterPackNew.setTempItem(forSearchIdFilters);
							/* ADD REDUCER'S PORT */
							requestToReducerSocket = new Socket("127.0.0.1", 9876);
							out3 = new ObjectOutputStream(requestToReducerSocket.getOutputStream());
							out3.writeObject(masterPackNew);// Send data to reducer
							out3.flush();
							in3 = new ObjectInputStream(requestToReducerSocket.getInputStream());
							String rdcrAnswer = (String) in3.readObject();
							System.out.println(rdcrAnswer);// Print response
							out.writeObject(rdcrAnswer);// Send response back to the manager
							out3.flush();
							out.flush();
							out3.close();
							in3.close();
						} else {// No rooms found for the manager
							masterPackNew.setTempItem(forSearchIdFilters);// Set empty room node
							/* ADD REDUCER'S PORT */
							requestToReducerSocket = new Socket("127.0.0.1", 9876);
							out3 = new ObjectOutputStream(requestToReducerSocket.getOutputStream());
							out3.writeObject(masterPackNew); // Send to reducer
							out3.flush();
							in3 = new ObjectInputStream(requestToReducerSocket.getInputStream());
							String rdcrAnswer = (String) in.readObject();
							System.out.println(rdcrAnswer);// Print response
							out.writeObject(rdcrAnswer);// Send back to the manager
							out.flush();
						}
					} else {// No rooms found for the manager
						masterPackNew.setTempItem(forSearchIdFilters);// Set empty room node
						/* ADD REDUCER'S PORT */
						requestToReducerSocket = new Socket("127.0.0.1", 9876);
						out3 = new ObjectOutputStream(requestToReducerSocket.getOutputStream());
						out3.writeObject(masterPackNew);
						in3 = new ObjectInputStream(requestToReducerSocket.getInputStream());
						String rdcrAnswer = (String) in3.readObject();
						System.out.println(rdcrAnswer);// Print response
						out.writeObject(rdcrAnswer);// Send back to the manager
						out.flush();
						out3.flush();// Ensure output stream is flushed
					}
				} else {// Manager wants to display accommodation bookings over a certain period
					if (masterPackNew.getTempItem() == null)
						masterPackNew.setTempItem(new RoomNode());
					// Set to a new RoomNode if null
					/* toReducer */
					if (masterPackNew.getTempItem().getPrwthAreaBookings() == null)
						masterPackNew.getTempItem().setPrwthAreaBookings(new AreaBookings[this.headRoomNode.getN()]);
					masterPackNew.getTempItem().setPrwthAreaBookings(headRoomNode
							.reservationsReport(masterPackNew.getFirstPeriodDay(), masterPackNew.getLastPeriodDay()));
					/* ADD REDUCER'S PORT */
					requestToReducerSocket = new Socket("127.0.0.1", 9876);
					out3 = new ObjectOutputStream(requestToReducerSocket.getOutputStream());
					out3.writeObject(masterPackNew);// Send to reducer
					out3.flush();
					in3 = new ObjectInputStream(requestToReducerSocket.getInputStream());
					String rdcrAnswer = (String) in3.readObject();// Get response
					System.out.println(rdcrAnswer + " ( printing in worker)");// Print response
					out.writeObject(rdcrAnswer);// Send back to the manager
					out.flush();
				}

			} else { // If the package is from a user
				UserPack masterPackNew = (UserPack) masterPack;
				System.out.println("Master's request is from user " + masterPackNew.getIdUser());
				/*---->*/ if (masterPackNew.getUserSelectIndicator() == 1) {
					// User is adding filters
					/* toReducer */
					Filters chosenFilters = masterPackNew.getChosenFilters();// Get selected filters
					forSearchIdFilters = headRoomNode.searchBasedOnFilters(chosenFilters);// Search for rooms based on
																							// filters
					masterPackNew.setTempItem(forSearchIdFilters);// Set search results in temp item

					/* ADD REDUCER'S PORT */
					requestToReducerSocket = new Socket("127.0.0.1", 9876);
					out3 = new ObjectOutputStream(requestToReducerSocket.getOutputStream());
					out3.writeObject(masterPackNew);
					out3.flush();
					in3 = new ObjectInputStream(requestToReducerSocket.getInputStream());
					String rdcrAnswer = (String) in3.readObject();// Get response from reducer
					System.out.println(rdcrAnswer + " (printing in worker)");// Print response
					out.writeObject(rdcrAnswer);// Send back to the manager
					out.flush();
					out3.close();
					in3.close();
				} else if (masterPackNew.getUserSelectIndicator() == 2) { // Room booking action
					/* THIS DOES NOT NEED TO GO TO THE REDUCER */
					String tempRoomNamePaketou = masterPackNew.getRoomNameToBeBooked();
					Room wantedRoom = headRoomNode.searchByRoomName(tempRoomNamePaketou);// Search for the room
					if (wantedRoom.getRoomName() != null) {// Check if the room exists
						int firstBookedDay = masterPackNew.getFirstBookedDay();
						int noOfBookedDays = masterPackNew.getNoOfBookedDays();
						// Check room availability for the booking period
						int z = 0;
						for (int j = firstBookedDay; j < noOfBookedDays; j++) {
							if (wantedRoom.getAvailability_365()[j] != 1) {
								z = 5;// Room is not available
							}
						}
						if (z == 0) {// Room is available
							headRoomNode.searchByRoomName(tempRoomNamePaketou).setBookDay_365(firstBookedDay,
									noOfBookedDays);// Set booking
							System.out.println("Successfully booked the " + tempRoomNamePaketou + " room for the user"
									+ masterPackNew.getIdUser() + " (printing in worker)");// Print success message
							String rdcrAnswer = "Room update successful";
							out.writeObject(rdcrAnswer);// Send success message back to the manager
						} else {
							System.out.println("Unsuccessfully booked " + tempRoomNamePaketou + " room for the user"
									+ masterPackNew.getIdUser() + " (printing in worker)");// Print failure message
							String rdcrAnswer = "Room update unsuccessful";
							out.writeObject(rdcrAnswer);// Send failure message back to the manager
						}
						out.flush();
					} else {// Room does not exist
						System.out.println("The room does not exist");
						String rdcrAnswer = "Room update unsuccessful";
						out.writeObject(rdcrAnswer);// Send failure message back to the manager
						out.flush();
					}
				} else /* means masterPackNew.getUserSelectIndicator() == 3 */ { // Room rating action
					/* THIS DOES NOT NEED TO GO TO THE REDUCER */
					String tempRoomNamePaketou = masterPackNew.getRoomNameToBeBooked();
					headRoomNode.searchByRoomName(tempRoomNamePaketou).roomReview(masterPackNew.getRoomReview()); // Add
																													// review
					System.out.println("Successful rating of " + tempRoomNamePaketou + " room for the user"
							+ masterPackNew.getIdUser() + " (printing in worker)"); // Print success message
					String rdcrAnswer = "Room update successful";
					out.writeObject(rdcrAnswer);// Send success message back to the manager
					out.flush();
				}
			}
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (requestToReducerSocket != null)
				requestToReducerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}