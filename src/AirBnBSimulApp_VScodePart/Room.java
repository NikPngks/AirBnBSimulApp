package AirBnBSimulApp_VScodePart;

import java.io.*;
import java.util.Date;

public class Room implements Serializable {
	private static final long serialVersionUID = 8626459955423694374L;
	private String roomName;
	private String area;
	private double price;
	private int numberOfReviews;
	private int noOfPerson; // Maximum number of persons allowed to stay in the room
	public int[] availability_365 = new int[365];// Array to track availability for each day of the year (0=available,
													// 1=available, 2=booked)
	private double stars;
	private String[] roomImage;// Array to hold image file paths or names of the room images
	private File imageFile;// Image file associated with the room
	byte[] byteArrayPNG;// Byte array to hold image data in PNG format
	private int idManager;
	private int reservCounter;// Counter for reservations made

	public Room() {
		this.roomName = roomName;
		this.area = area;
		this.price = price;
		this.numberOfReviews = numberOfReviews;
		this.noOfPerson = noOfPerson;
		this.availability_365 = availability_365;
		this.stars = stars;
		this.roomImage = roomImage;
		this.idManager = idManager;
		this.reservCounter = reservCounter;
	}

	public byte[] getByteArrayPNG() {
		return byteArrayPNG;
	}

	public void setByteArrayPNG(byte[] byteArrayPNG) {
		this.byteArrayPNG = byteArrayPNG;
	}

	public int getreservCounter() {
		return reservCounter;
	}

	public void setreservCounter(int reservCounter) {
		this.reservCounter = reservCounter;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getNumberOfReviews() {
		return numberOfReviews;
	}

	public void setNumberOfReviews(int numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}

	public int getNoOfPerson() {
		return noOfPerson;
	}

	public void setNoOfPerson(int noOfPerson) {
		this.noOfPerson = noOfPerson;
	}

	public int[] getAvailability_365() {
		return availability_365;
	}

	public void setAvailability_365(int[] availability_365) {
		this.availability_365 = availability_365;
	}

	public void setavailability_365(int noOfAvailableDays) {
		for (int i = 0; i < noOfAvailableDays; i++) {
			this.availability_365[0] = 1;
		}
	}

	public void setavailability_365(int firstDayAvailable, int noOfAvailableDays) {
		for (int i = 0; i < noOfAvailableDays; i++) {
			this.availability_365[firstDayAvailable + i] = 1;// Mark days as available
		}
	}

	public void setUnAvailability_365(int firstBookedDay, int noOfBookedDays) {
		for (int i = 0; i < noOfBookedDays; i++) {
			this.availability_365[firstBookedDay + i] = 0;// Mark days as unavailable
		}
	}

	public void setBookDay_365(int firstBookedDay, int noOfBookedDays) {
		for (int i = 0; i < noOfBookedDays; i++) {
			this.availability_365[firstBookedDay + i] = 2;// Mark days as booked
		}
	}

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	public String[] getRoomImage() {
		return roomImage;
	}

	public void setRoomImage(String[] roomImage) {
		this.roomImage = roomImage;
	}

	public int getIdManager() {
		return idManager;
	}

	public void setIdManager(int idManager) {
		this.idManager = idManager;
	}

	int tempInt = 0;// Temporary variable for hash function

	// Hash function to distribute rooms across workers
	public int hashFunction(int integer, int noOfWorkers) {
		if (integer == 0) {
			char IthChar = roomName.charAt(integer);
			int IthInt = (int) IthChar;// Get ASCII value of the first character
			return IthInt;
		} else {
			// Adjust integer if it's greater than the room name length
			if (roomName.length() < integer) {
				while (!(roomName.length() < integer)) {
					integer--; // Reduce integer until it fits the room name length
				}
			}
			char IthChar = roomName.charAt(integer);// Get character at the specified index
			int IthInt = (int) IthChar;// Get ASCII value
			tempInt = hashFunction(integer - 1, noOfWorkers) * 11 + IthInt;
			return tempInt % 7 % noOfWorkers;// Distribute room across workers
		}
	}

	@Override
	public String toString() {
		return "Room= " + roomName + '\n' +
				"area= " + area + '\n' +
				"price= " + price + '\n' +
				"numberOfReviews= " + numberOfReviews + '\n' +
				"noOfPerson = " + noOfPerson + '\n' +
				"availability_365= " + availability_365 + '\n' +
				"stars= " + stars + '\n' +
				"roomImage= " + roomImage + '\n' +
				"IdManager= " + idManager; // Returns a formatted string of room attributes
	}

	// Method to update the room's average star rating based on a new review
	public void roomReview(int reviewStars) {
		// Calculates the new average rating
		this.setStars((this.getNumberOfReviews() * this.getStars() + reviewStars) / (this.getNumberOfReviews() + 1));
		// Increments the number of reviews
		this.setNumberOfReviews(this.getNumberOfReviews() + 1);
	}

}