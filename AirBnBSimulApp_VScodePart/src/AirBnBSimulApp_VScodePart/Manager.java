package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Manager extends Thread {
	private static final long serialVersionUID = 8626459955423694374L;
	ManagerPack t;
	Room[] rooms = new Room[50];// Any manager can add up to 50 rooms with per package

	Manager(ManagerPack t) {
		this.t = t;
	}

	@Override
	public void run() {
		// Establish the manager as a client to the master server
		Socket requestSocketToMaster = null; // Socket to contact master
		ObjectOutputStream out = null; // Output stream for sending data to master
		ObjectInputStream in = null; // Input stream for receiving data from master

		// Request manager ID input
		System.out.println("Insert your Id:\n");
		Scanner keyboardInputInt = new Scanner(System.in);
		t.setIdManager(keyboardInputInt.nextInt()); // Note: No input validation for non-integer values

		// Prompt the manager to choose an action
		System.out.println("choose one of the following options:\n" +
				"* 1. Add rooms\n" +
				"* 2. Add available dates to a room\n" +
				"* 3. Display reservations for their property\n" +
				"* 4. View all accommodation reservations for a specific period\n" +
				"* 5. no choice");

		keyboardInputInt = new Scanner(System.in);
		int choice = keyboardInputInt.nextInt();

		// Handle the manager's selection
		if (choice == 1) { // Add rooms
			t.setManagerSelectIndicator(1);
			t.addRooms(); // Calls addRooms to pass the rooms (up to 50) to the Manager array
		} else if (choice == 2) { // Add available dates to a room
			t.setManagerSelectIndicator(2);
			t.availableDays();
		} else if (choice == 3) { // Display reservations for the manager's property
			t.setManagerSelectIndicator(3);
		} else if (choice == 4) { // View reservations for a specific period
			t.setManagerSelectIndicator(4); // Set indicator to specify this action
			// Parse the start date and calculate the number of days since 01-01-2024
			System.out.println("Enter the first day of the time period you want to view, for example, 27-09-2024.");
			Scanner keyboardInputDate = new Scanner(System.in);
			String firstDay = keyboardInputDate.nextLine();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date firstPeriodDay = dateFormat.parse(firstDay);// Parse the first date
				Date referenceDate = dateFormat.parse("01-01-2024");// Reference start date
				long differenceInMillis = firstPeriodDay.getTime() - referenceDate.getTime(); // Calculate difference in
																								// milliseconds
				long firstDayPeriod1 = (differenceInMillis) / (1000 * 60 * 60 * 24);// Convert to days
				int firstDayPeriod = (int) firstDayPeriod1;
				t.setFirstPeriodDay(firstDayPeriod); // Set the start period day
			} catch (ParseException e) {
				System.err.println(
						"Invalid date format. Please enter the date in DD-MM-YYYY format, for example, 27-09-2024.");
			}

			// Repeat the process for the last date
			System.out.println("Enter the last day of the time period you want to view, for example, 27-09-2024.");
			keyboardInputDate = new Scanner(System.in);
			String lastDay = keyboardInputDate.nextLine();
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date lastPeriodDay = dateFormat.parse(lastDay);
				Date referenceDate = dateFormat.parse("01-01-2024");
				long differenceInMillis = lastPeriodDay.getTime() - referenceDate.getTime();
				long lastDayPeriod1 = (differenceInMillis) / (1000 * 60 * 60 * 24);
				int lastDayPeriod = ((int) lastDayPeriod1);
				t.setLastPeriodDay(lastDayPeriod); // Set the end period day
			} catch (ParseException e) {
				System.err.println("wrong date entry format");
			}
		} else if (choice == 5) {// on selection 5 it will terminate
			t.setManagerSelectIndicator(5);
		} else {
			System.err.println("Invalid selection");
		}
		try {
			/* ADD Master's PORT */
			requestSocketToMaster = new Socket("127.0.0.1", 1234); // // Connect to the Master on port 1234
			out = new ObjectOutputStream(requestSocketToMaster.getOutputStream()); // Output stream to master
			in = new ObjectInputStream(requestSocketToMaster.getInputStream());// Input stream from master

			out.writeObject(t);// Send the manager package to the master
			out.flush();

			try {// Receive the response packet from the master (could be a String or ManagerPack
					// object)
				Object masterAnswer = (Object) in.readObject();
				System.out.println("Received the response packet from the Master");
				if (masterAnswer instanceof String) {
					String toBePrinted = (String) masterAnswer;
					System.out.println(toBePrinted);// Print string response
				} else {// If it's a ManagerPack, process it
					ManagerPack masterPackNew = (ManagerPack) masterAnswer;

					if (masterPackNew.getManagerSelectIndicator() == 3) { // If viewing reservations for the property
						RoomNode itemToShowRoomBooking = new RoomNode();// I'm temporarily making a new RoomNode to
																		// which I'll pass the one I sent
						itemToShowRoomBooking = masterPackNew.getTempItem().getHeadRoomNode();// I am passing here what
																								// I sent
						if (itemToShowRoomBooking.getRoomItem() == null) {
							System.out.println("There are no rooms with this ID.");
						} else {
							// Loop through the availability of the room (365 days) and check if the room is
							// booked (value = 2)
							while (itemToShowRoomBooking.getRoomItem() != null) {
								for (int q = 0; q < 365; q++) {
									if (itemToShowRoomBooking.getRoomItem().getAvailability_365()[q] == 2) {
										LocalDate date = LocalDate.of(2024, 1, 1).plusDays(q);// Calculate date based on
																								// index
										String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
										System.out.println(
												"For the room " + itemToShowRoomBooking.getRoomItem().getRoomName()
														+ "the date is booked: " + formattedDate);
									}
								}
								if (itemToShowRoomBooking.getPrevRoomNode() != null) {
									itemToShowRoomBooking = itemToShowRoomBooking.getPrevRoomNode();// Go to the next
																									// room node
								} else {
									itemToShowRoomBooking.setRoomItem(null);// Exit loop when no more rooms
								}
							}
							System.out.println("These were all your room reservations");
							in.close();
							out.close();
						}
					} else if (masterPackNew.getManagerSelectIndicator() == 4) { // View reservations in a specific
																					// period
						RoomNode itemToShowAreaBooks = new RoomNode(); // Temporary RoomNode to process results
						itemToShowAreaBooks.setPrwthAreaBookings(masterPackNew.getTempItem().getPrwthAreaBookings());

						AreaBookings[] itemGiaAnafora = itemToShowAreaBooks.getPrwthAreaBookings();
						for (int z = 0; z < itemGiaAnafora.length; z++) {
							if (itemGiaAnafora[z] == null) {
								z = itemGiaAnafora.length + 1;
							} else {
								System.out.println(itemGiaAnafora[z].getArea() + ": "
										+ itemGiaAnafora[z].getSumReservations() + "reservations");
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				if (requestSocketToMaster != null)
					requestSocketToMaster.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ManagerPack t = new ManagerPack();
		Manager MN = new Manager(t);
		MN.start();
	}
}