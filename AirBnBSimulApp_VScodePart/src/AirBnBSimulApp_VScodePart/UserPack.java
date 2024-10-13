package AirBnBSimulApp_VScodePart;

import java.io.Serializable;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UserPack implements Serializable {
	private static final long serialVersionUID = 8626459955423694374L;

	int userSelectIndicator; // User selection indicator to track the user's menu choice
	int idUser;
	int noOfWorkers;
	public Filters chosenFilters;

	String roomNameToBeBooked;
	int firstBookedDay;
	int noOfBookedDays;

	int roomReview;
	RoomNode tempItem = null; // Temporary storage for room data
	String sender = "User";

	public UserPack() {
		super();
		this.chosenFilters = chosenFilters;
		this.userSelectIndicator = userSelectIndicator;
		this.roomNameToBeBooked = roomNameToBeBooked;
		this.firstBookedDay = firstBookedDay;
		this.noOfBookedDays = noOfBookedDays;
		this.roomReview = roomReview;
		this.tempItem = tempItem;
		this.idUser = idUser;
		this.noOfWorkers = noOfWorkers;
		this.sender = sender;
	}

	public UserPack(Filters chosenFilters, int userSelectIndicator, String roomNameToBeBooked,
			int firstBookedDay, int noOfBookedDays, int roomReview, int dUser) {
		super();
		this.chosenFilters = chosenFilters;
		this.userSelectIndicator = userSelectIndicator;
		this.roomNameToBeBooked = roomNameToBeBooked;
		this.firstBookedDay = firstBookedDay;
		this.noOfBookedDays = noOfBookedDays;
		this.roomReview = roomReview;
		this.sender = sender;
	}

	// Getter and setter methods for various properties
	public int getNoOfWorkers() {
		return noOfWorkers;
	}

	public void setNoOfWorkers(int noOfWorkers) {
		this.noOfWorkers = noOfWorkers;
	}

	public int getRoomReview() {
		return roomReview;
	}

	public void setRoomReview(int roomReview) {
		this.roomReview = roomReview;
	}

	public int getFirstBookedDay() {
		return firstBookedDay;
	}

	public void setFirstBookedDay(int firstBookedDay) {
		this.firstBookedDay = firstBookedDay;
	}

	public int getNoOfBookedDays() {
		return noOfBookedDays;
	}

	public void setNoOfBookedDays(int noOfBookedDays) {
		this.noOfBookedDays = noOfBookedDays;
	}

	public Filters getChosenFilters() {
		return chosenFilters;
	}

	public void setChosenFilters(Filters chosenFilters) {
		this.chosenFilters = chosenFilters;
	}

	public int getUserSelectIndicator() {
		return userSelectIndicator;
	}

	public void setUserSelectIndicator(int userSelectIndicator) {
		this.userSelectIndicator = userSelectIndicator;
	}

	public RoomNode getTempItem() {
		return tempItem;
	}

	public void setTempItem(RoomNode tempItem) {
		this.tempItem = tempItem;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	void book(Room roomToBeBooked, Filters chosenFilters) {
		roomToBeBooked.setUnAvailability_365(chosenFilters.getReservationStart(), chosenFilters.getNoOfBookedDays());
	}

	public String getRoomNameToBeBooked() {
		return roomNameToBeBooked;
	}

	public void setRoomNameToBeBooked(String roomNameToBeBooked) {
		this.roomNameToBeBooked = roomNameToBeBooked;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	void roomReview() {
		System.out.println("Choose your rating from 1 to 5 stars");
		Scanner keyboardInputInt = new Scanner(System.in);
		int reviewStars = keyboardInputInt.nextInt();
		// Validate input for review stars
		if (reviewStars >= 0 && reviewStars <= 5) {
			this.setRoomReview(reviewStars);
		} else {
			System.out.println("Invalid input. Please try again:");
			roomReview();// Prompt user to review again
		}
	}

	// Method for selecting filters based on user choice
	void chooseFilters(int choice) throws Exception {
		if (choice == 1) {
			if (this.chosenFilters.intForChosenAreas <= 1) {// Check if user can choose more areas
				String selectedArea = chooseArea();
				this.chosenFilters.setArea(selectedArea);
			} else {
				System.err.println("You cannot choose another area.");
			}
		} else if (choice == 2) {
			System.out.println("Enter the min price:");
			Scanner keyboardInputDouble = new Scanner(System.in);
			double minPrice = keyboardInputDouble.nextDouble();
			this.chosenFilters.setMinPrice(minPrice);
		} else if (choice == 3) {
			System.out.println("Enter the max price:");
			Scanner keyboardInputDouble = new Scanner(System.in);
			double maxPrice = keyboardInputDouble.nextDouble();
			this.chosenFilters.setMaxPrice(maxPrice);
		} else if (choice == 4) {
			System.out.println("Enter the min number of stars:");
			Scanner keyboardInputInt = new Scanner(System.in);
			int minStars = keyboardInputInt.nextInt();
			this.chosenFilters.setStars(minStars);
		} else if (choice == 5) {
			System.out.println("Enter the number of visitors:");
			Scanner keyboardInputInt = new Scanner(System.in);
			int noOfPersons = keyboardInputInt.nextInt();
			this.chosenFilters.setNoOfPerson(noOfPersons);
		} else if (choice == 6) {
			System.out.println("Enter the first book day:"
					+ "for example 27-09-2024");
			Scanner keyboardInputDate = new Scanner(System.in); // accept the date as 27-09-2024
			String startReserv = keyboardInputDate.nextLine();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date firstDay = dateFormat.parse(startReserv);
				Date referenceDate = dateFormat.parse("01-01-2024");
				long differenceInMillis = firstDay.getTime() - referenceDate.getTime();// Calculate difference in
																						// milliseconds
				long differenceInDays1 = differenceInMillis / (1000 * 60 * 60 * 24);// Convert milliseconds to days
				int differenceInDays = (int) differenceInDays1; // Cast to int
				this.chosenFilters.setReservationStart(differenceInDays); // Set the reservation start in filters
			} catch (ParseException e) {
				System.err.println("Wrong date entry format");
			}
		} else if (choice == 7) {
			System.out.println("type the number of days of the reservation: ");
			Scanner keyboardInputInt = new Scanner(System.in);
			int noOfBookedDays = keyboardInputInt.nextInt();
			this.chosenFilters.setNoOfBookedDays(noOfBookedDays);
			// the choice of sealing. will check if an entire region is selected
		} else if (choice == 8) {
			if (this.chosenFilters.intForChosenAreas == 0) {
				System.err.println("you must select at least one region");
			} else {
				if (this.chosenFilters.getReservationStart() == 0) {
					System.err.println("you must choose a reservation start date");
				} else {
					this.chosenFilters.setTemp(false);
				}
			}
			// if he has not pressed 1-8 returns to the initial options menu
		} else {
			System.err.println("you have chosen wrong, try again: ");
		}
		if (this.getUserSelectIndicator() == 1) {
			System.out.println("Do you want to choose another filter?\n" + "1. YES, 2. NO");
			Scanner keyboardInputInt = new Scanner(System.in);
			choice = keyboardInputInt.nextInt();
			if (choice == 1) { // if he types anything but 1 goes on.
			} else {
				if (this.chosenFilters.intForChosenAreas == 0) {
					System.err.println("you must select at least one area");
				} else {
					if (this.chosenFilters.getReservationStart() == 0) {
						System.err.println("you must choose a reservation start date");
					} else {
						this.chosenFilters.setTemp(false);
					}
				}
			}
		}
	}

	String chooseArea() {
		System.out.println("Enter the area:");
		Scanner keyboardInputString = new Scanner(System.in);
		String chosenArea = keyboardInputString.nextLine();
		return chosenArea;
	}
}