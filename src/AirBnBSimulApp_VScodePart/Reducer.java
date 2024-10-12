package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Reducer {
	private static final long serialVersionUID = 8626459955423694374L;
	ServerSocket providerReducerSocket;
	Socket connectionWR = null;

	private List<ManagerPack> packetsM = new ArrayList<>();
	private List<UserPack> packetsU = new ArrayList<>();

	public static final int PORT = 9876; // Reducer's port

	public static void main(String args[]) {
		new Reducer().openReducerServer();
	}

	private void openReducerServer() {
		try {
			providerReducerSocket = new ServerSocket(9876, 50);
			while (true) {
				connectionWR = providerReducerSocket.accept();
				System.out.println("Reducer is running..");
				Thread t = new ActionsForWorkers(connectionWR, packetsM, packetsU);
				t.start();
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				providerReducerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}