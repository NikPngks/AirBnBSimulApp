package AirBnBSimulApp_VScodePart;

import java.io.Serializable;

public class Filters implements Serializable {
	private static final long serialVersionUID = 8626459955423694374L;
	private String[] area = { null, null, null }; // Default area selection (null signifies no selection)
	public int intForChosenAreas = 0;// Counter for selected areas
	private double minPrice = 0; // Minimum price default set to 0 (to be adjusted by user input)
	private double maxPrice = 1000000; // Maximum price default set to 1,000,000 (to be adjusted by user input)

	private double stars = 0; // Default star rating set to 0 (to be adjusted by user input)
	private int noOfPerson = 1; // Default number of persons set to 1
	private int reservationStart = 0; // Default reservation start date (01-01-2024, represented as 0)
	private int noOfBookedDays = 1; // Default number of booked days set to 1
	boolean temp = true; // Flag for filter selection, used internally

	public String[] getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area[intForChosenAreas] = area;
		this.intForChosenAreas++;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	public int getNoOfPerson() {
		return noOfPerson;
	}

	public void setNoOfPerson(int noOfPerson) {
		this.noOfPerson = noOfPerson;
	}

	public int getReservationStart() {
		return reservationStart;
	}

	public void setReservationStart(int differenceInDays) {
		this.reservationStart = (int) differenceInDays;
	}

	public int getNoOfBookedDays() {
		return noOfBookedDays;
	}

	public void setNoOfBookedDays(int noOfBookedDays) {
		this.noOfBookedDays = noOfBookedDays;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public Filters(String[] area, double minPrice, double maxPrice, double stars, int noOfPerson,
			int reservationStart, int noOfBookedDays) {

		this.area = area;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.stars = stars;
		this.noOfPerson = noOfPerson;
		this.reservationStart = reservationStart;
		this.noOfBookedDays = noOfBookedDays;
		this.intForChosenAreas = intForChosenAreas;
		this.temp = temp;
	}

	public Filters() {
		this.area = area;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.stars = stars;
		this.noOfPerson = noOfPerson;
		this.reservationStart = reservationStart;
		this.noOfBookedDays = noOfBookedDays;
		this.intForChosenAreas = intForChosenAreas;
		this.temp = temp;
	}
}