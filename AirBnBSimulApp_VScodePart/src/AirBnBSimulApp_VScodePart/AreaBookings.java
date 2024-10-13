package AirBnBSimulApp_VScodePart;

import java.io.Serializable;

public class AreaBookings implements Serializable {
	private static final long serialVersionUID = 8626459955423694374L;
	String area;
	int sumReservations;// here keeps a number with the reservations of the area

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getSumReservations() {
		return sumReservations;
	}

	public void setSumReservations(int sumReservations) {
		this.sumReservations = sumReservations;
	}

}