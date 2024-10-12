package AirBnBSimulApp_VScodePart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManagerPack implements Serializable {
	private static final long serialVersionUID = 8626459955423694374L;
	Room[] rooms; // Array of rooms read from a JSON file
	String roomNameForChangeAvailability;
	int firstDayAvailable;
	int noOfAvailableDays;
	String areaForReservationsReport;
	int firstPeriodDay;
	String sender = "Manager";// Indicates the role of the user
	int lastPeriodDay;
	int idManager;
	int managerSelectIndicator;
	int noOfWorkers;
	RoomNode tempItem = null;// To show reservations for the manager

	// Getters and Setters
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public ManagerPack(Room[] rooms, String roomNameForChangeAvailability, int firstDayAvailable, int noOfAvailableDays,
			String areaForReservationsReport, int idManager, int managerSelectIndicator) {
		super();
		this.rooms = rooms;
		this.roomNameForChangeAvailability = roomNameForChangeAvailability;
		this.firstDayAvailable = firstDayAvailable;
		this.noOfAvailableDays = noOfAvailableDays;
		this.areaForReservationsReport = areaForReservationsReport;
		this.firstPeriodDay = firstPeriodDay;
		this.lastPeriodDay = lastPeriodDay;
		this.idManager = idManager;
		this.managerSelectIndicator = managerSelectIndicator;
		this.tempItem = tempItem;
		this.noOfWorkers = noOfWorkers;
		this.sender = sender;
	}

	public ManagerPack() {
		super();
		this.rooms = rooms;
		this.roomNameForChangeAvailability = roomNameForChangeAvailability;
		this.firstDayAvailable = firstDayAvailable;
		this.noOfAvailableDays = noOfAvailableDays;
		this.areaForReservationsReport = areaForReservationsReport;
		this.firstPeriodDay = firstPeriodDay;
		this.lastPeriodDay = lastPeriodDay;
		this.idManager = idManager;
		this.managerSelectIndicator = managerSelectIndicator;
		this.tempItem = tempItem;
		this.noOfWorkers = noOfWorkers;
		this.sender = sender;
	}

	// Getters and setters for all properties
	public Room[] getRooms() {
		return rooms;
	}

	public void setRooms(Room[] rooms) {
		this.rooms = rooms;
	}

	public String getRoomNameForChangeAvailability() {
		return roomNameForChangeAvailability;
	}

	public void setRoomNameForChangeAvailability(String roomNameForChangeAvailability) {
		this.roomNameForChangeAvailability = roomNameForChangeAvailability;
	}

	public String getareaForReservationsReport() {
		return areaForReservationsReport;
	}

	public void setareaForReservationsReport(String areaForReservationsReport) {
		this.areaForReservationsReport = areaForReservationsReport;
	}

	public int getIdManager() {
		return idManager;
	}

	public void setIdManager(int idManager) {
		this.idManager = idManager;
	}

	public int getManagerSelectIndicator() {
		return managerSelectIndicator;
	}

	public void setManagerSelectIndicator(int managerSelectIndicator) {
		this.managerSelectIndicator = managerSelectIndicator;
	}

	public int getFirstDayAvailable() {
		return firstDayAvailable;
	}

	public void setFirstDayAvailable(int firstDayAvailable) {
		this.firstDayAvailable = firstDayAvailable;
	}

	public int getNoOfAvailableDays() {
		return noOfAvailableDays;
	}

	public void setNoOfAvailableDays(int noOfAvailableDays) {
		this.noOfAvailableDays = noOfAvailableDays;
	}

	public int getFirstPeriodDay() {
		return firstPeriodDay;
	}

	public void setFirstPeriodDay(int firstPeriodDay) {
		this.firstPeriodDay = firstPeriodDay;
	}

	public int getLastPeriodDay() {
		return lastPeriodDay;
	}

	public void setLastPeriodDay(int lastPeriodDay) {
		this.lastPeriodDay = lastPeriodDay;
	}

	public RoomNode getTempItem() {
		return tempItem;
	}

	public int getNoOfWorkers() {
		return noOfWorkers;
	}

	public void setNoOfWorkers(int noOfWorkers) {
		this.noOfWorkers = noOfWorkers;
	}

	public void setTempItem(RoomNode tempItem) {
		this.tempItem = tempItem;
	}

	/*
	 * Prompts the user for the path to a JSON file containing room data
	 * and reads the data into the rooms array.
	 */
	void addRooms() {
		System.out.println("Give the address of the room file you want to add");
		Scanner keyboardInputDate = new Scanner(System.in);
		String pathJsonFile = keyboardInputDate.nextLine();
		this.rooms = jsonReader(pathJsonFile);
	}

	Room[] jsonReader(String pathJsonFile) {
		ObjectMapper objectMapper = new ObjectMapper();

		File roomsJsonFile = new File(pathJsonFile);
		Room[] roomTemp = null;
		try {
			roomTemp = objectMapper.readValue(roomsJsonFile, Room[].class);
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < roomTemp.length; i++) {
			File imageFile = new File(roomTemp[i].getRoomImage()[0]);
			roomTemp[i].byteArrayPNG = new byte[(int) imageFile.length()];

			try (FileInputStream fileInputStream = new FileInputStream(imageFile);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

				bufferedInputStream.read(roomTemp[i].byteArrayPNG, 0, roomTemp[i].byteArrayPNG.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return roomTemp;
	}

	/*
	 * Prompts the manager to enter the room name and available days.
	 * Parses the date input and calculates the availability of the specified room.
	 */
	void availableDays() {
		System.out.println("Enter the name of the room:\n");
		Scanner keyboardInputRoomName = new Scanner(System.in);
		String roomNameForChangeAvailability = keyboardInputRoomName.nextLine();
		setRoomNameForChangeAvailability(roomNameForChangeAvailability); // informs the name of the room in the package
																			// it will send
		System.out.println("type the date you want to make available "
				+ " for example 27-09-2024");
		Scanner keyboardInputDate = new Scanner(System.in); // I accept the date as such 27-09-2024
		String vailableDay = keyboardInputDate.nextLine();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date vailableDayOne = dateFormat.parse(vailableDay);
			Date referenceDate = dateFormat.parse("01-01-2024");
			long differenceInMillis = vailableDayOne.getTime() - referenceDate.getTime();
			long firstDayAvailable1 = (differenceInMillis) / (1000 * 60 * 60 * 24);
			int firstDayAvailable = (int) firstDayAvailable1;
			setFirstDayAvailable(firstDayAvailable);
			System.out.println("Enter how many days you want to set as available (e.g., 5):");
			Scanner keyboardInputInt = new Scanner(System.in);
			int noOfAvailableDays = keyboardInputInt.nextInt();
			setNoOfAvailableDays(noOfAvailableDays);
		} catch (ParseException e) {
			System.err.println("Error: Wrong date entry format. Please use DD-MM-YYYY format.");
		}
	}
}