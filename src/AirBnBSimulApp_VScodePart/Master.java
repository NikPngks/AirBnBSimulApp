package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Master /* extends Thread */ {
	private static final long serialVersionUID = 8626459955423694374L;
	public static final int PORT = 1234; // Master's port
	private static int noOfWorkers;

	static ArrayDeque<ClientRequest> requestsUsrMngr;
	public static List<worker> workersEx = new ArrayList<>();

	public static void main(String[] args) {
		System.out.println("How many workers do you want?");
		Scanner keyboardInputInt = new Scanner(System.in);
		noOfWorkers = keyboardInputInt.nextInt();
		for (int i = 0; i < noOfWorkers; i++) {

			RoomNode workerRoomDB = new RoomNode(); // FOR CHECKING AT HOME INSERT 127.0.0.1
			System.out.println("Enter the worker's host " + i);
			Scanner keyboardInputHost = new Scanner(System.in);
			String workersHost = keyboardInputHost.nextLine();
			// here I pass the host which delongs to i worker
			worker worker = new worker(workersHost, workerRoomDB, i); // pass the i using it as id for worker
			workersEx.add(worker);
		}
		requestsUsrMngr = new ArrayDeque<ClientRequest>();
		new Master().openServer();
	}

	String getWorkersHost(int i) {
		return this.workersEx.get(i).getWorkersHost();
	}

	ServerSocket masterProviderToSocket;
	Socket connection = null;

	public void openServer() {
		try {
			masterProviderToSocket = new ServerSocket(PORT, 40);// set up to 40 connections
			while (true) {
				connection = masterProviderToSocket.accept();
				if (connection.isConnected()) {
					System.out.println("Master is connected with Client"); // I will consider all the others as clients
																			// (User, Manager, Reducer and maybe
																			// Workers)
					Thread t = new ActionsForAllClients(connection, noOfWorkers);
					t.start();
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				if (masterProviderToSocket != null) {
					masterProviderToSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getNoOfWorkers() {
		return noOfWorkers;
	}

	public void setNoOfWorkers(int noOfWorkers) {
		this.noOfWorkers = noOfWorkers;
	}
}