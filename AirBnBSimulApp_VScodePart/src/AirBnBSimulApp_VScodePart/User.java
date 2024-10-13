package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class User extends Thread {
	private static final long serialVersionUID = 8626459955423694374L;
	int idUser; // Unique identifier for the user
	UserPack t; // UserPack object containing user information and filters

	User(UserPack t) {
		this.t = t;
	}

	String roomNameToBeBooked = null;// Name of the room to be booked

	@Override
	public void run() {
		// let's make the User as a client on the master
		Socket requestSocketToMaster = null;// Socket for connecting to the master server
		ObjectOutputStream out = null; // Output stream for sending data to the master
		ObjectInputStream in = null; // Input stream for receiving data from the master

		System.out.println("Insert your Id:\n");
		Scanner keyboardInputInt = new Scanner(System.in);
		t.idUser = keyboardInputInt.nextInt(); // Set user ID (no validation for non-integer input)
		t.chosenFilters = new Filters();// Initialize chosen filters

		// Display user options
		System.out.println("choose one of the following options:\n" +
				"* 1. Add filters to search for a room\n" +
				"* 2. Book a room\n" +
				"* 3. Rate a room\n" +
				"* 4. no choice");
		keyboardInputInt = new Scanner(System.in);
		int choice = keyboardInputInt.nextInt();
		if (choice == 1) {
			t.setUserSelectIndicator(1);// Set user choice indicator for filters
			while (t.chosenFilters.isTemp()) { // While filters can still be added
				System.out.println("Choose a filter:\n" +
						"* 1. choose area (up to 3 areas)\n" +
						"* 2. min price\n" +
						"* 3. max price\n" +
						"* 4. min rating Star\n" +
						"* 5. number of persons\n" +
						"* 6. first day\n" +
						"* 7. sum of the days you want to book\n" +
						"* 8. no other choice");
				keyboardInputInt = new Scanner(System.in);
				int filterOption = keyboardInputInt.nextInt();
				try {
					t.chooseFilters(filterOption);// Apply the selected filter
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (choice == 2) {
			t.setUserSelectIndicator(2);
			System.out.println("Enter the name of the room you want to book:\n");
			Scanner keyboardInputRoomName = new Scanner(System.in);
			String roomNameToBeBooked2 = keyboardInputRoomName.nextLine();// Get room name for booking
			t.setRoomNameToBeBooked(roomNameToBeBooked2);
			try {
				t.chooseFilters(6);
			} catch (Exception e) {
				e.printStackTrace();
			}
			t.setFirstBookedDay(t.chosenFilters.getReservationStart());
			try {
				t.chooseFilters(7);// Set the number of days to book
			} catch (Exception e) {
				e.printStackTrace();
			}
			t.setNoOfBookedDays(t.chosenFilters.getNoOfBookedDays());
		} else if (choice == 3) {
			t.setUserSelectIndicator(3);
			System.out.println("Enter the name of the room you want to rate\n");
			Scanner keyboardInputRoomName = new Scanner(System.in);
			String roomNameToReview = keyboardInputRoomName.nextLine();
			t.setRoomNameToBeBooked(roomNameToReview);
			t.roomReview();
		} else if (choice == 4) {
			t.setUserSelectIndicator(4);// No action taken
		} else {
			System.err.println("Invalid choice.");// Error message for invalid choice
		}
		try {
			requestSocketToMaster = new Socket("127.0.0.1", 1234);// Connect to the Master server on port 1234
			out = new ObjectOutputStream(requestSocketToMaster.getOutputStream());
			in = new ObjectInputStream(requestSocketToMaster.getInputStream());
			out.writeObject(t);// Send UserPack object to master
			out.flush();

			try {
				Object masterAnswer = (Object) in.readObject();// Get response from the master
				System.out.println("Response received from master");
				if (masterAnswer instanceof String) {
					String toBePrinted = (String) masterAnswer; // Response is a message
					System.out.println(toBePrinted);
				} else {
					UserPack masterPackNew = (UserPack) masterAnswer;// Response is a UserPack object
					RoomNode itemToShowFilteredRooms = new RoomNode();// Temporary RoomNode to hold response
					itemToShowFilteredRooms = masterPackNew.getTempItem().getHeadRoomNode(); // Get head node of rooms
					if (itemToShowFilteredRooms == null) {
						System.out.println("there are no rooms with this id");// No available rooms
					} else {
						System.out.println("The available rooms are: \n");// Display available rooms
						while (itemToShowFilteredRooms.getRoomItem() != null) {
							System.out.println("Room's name: " + itemToShowFilteredRooms.getRoomItem().getRoomName()
									+ "\n"
									+ "area: " + itemToShowFilteredRooms.getRoomItem().getArea() + "\n"
									+ "price: " + itemToShowFilteredRooms.getRoomItem().getPrice() + "\n"
									+ "stars: " + itemToShowFilteredRooms.getRoomItem().getStars() + "\n"
									+ "image: " + itemToShowFilteredRooms.getRoomItem().getRoomImage()[0] + "\n");
							if (itemToShowFilteredRooms.getPrevRoomNode() != null) {
								itemToShowFilteredRooms = itemToShowFilteredRooms.getPrevRoomNode();
							} else {
								itemToShowFilteredRooms.setRoomItem(null);// End of available rooms
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
				in.close();
				out.close();
				requestSocketToMaster.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		UserPack t = new UserPack();
		User US = new User(t);
		US.start(); // Start the user thread
	}
}