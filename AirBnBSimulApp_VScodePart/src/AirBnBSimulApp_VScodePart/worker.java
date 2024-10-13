package AirBnBSimulApp_VScodePart; //If you run this class, you should not run the worker1, worker2 and worker3!!!

//C:\Users\Nikos\Downloads\\rooms.json 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class worker extends Thread { // server on the master and a client on the reducerr
	private static final long serialVersionUID = 8626459955423694374L;
	private int workerId;
	private String workersHost;
	private RoomNode headRoomNode;
	private ServerSocket providerWorkerSocket;
	private Socket connectionMW = null;
	private static int PORT;

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			PORT = 2345 + i;
			RoomNode workerRoomDB = new RoomNode();
			int workerId = i;
			System.out.println("dwse ton host tou worker " + i);
			Scanner keyboardInputHost = new Scanner(System.in);
			String workersHost = keyboardInputHost.nextLine();// perna to host tou upologisth!! px 127.0.0.1
			worker worker = new worker(workerRoomDB, workerId);
			worker.start();
		}
	}

	@Override
	public void run() {
		try {
			providerWorkerSocket = new ServerSocket(this.workerId + 2345, 50);
			while (true) {
				connectionMW = providerWorkerSocket.accept(); // dexetai th sundesh me ton master
				System.out.println("Worker " + this.workerId + " is running..");

				Thread t = new ActionsForMaster(connectionMW, this.headRoomNode);
				t.start();
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				providerWorkerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public worker(String workersHost, RoomNode workerRoomDB, int workerId) {
		this.workersHost = workersHost;
		this.workerId = workerId;
		this.providerWorkerSocket = providerWorkerSocket;
		this.connectionMW = connectionMW;
		this.headRoomNode = workerRoomDB;
	}

	public worker(RoomNode workerRoomDB, int workerId) {
		this.workersHost = workersHost;
		this.workerId = workerId;
		this.providerWorkerSocket = providerWorkerSocket;
		this.connectionMW = connectionMW;
		this.headRoomNode = workerRoomDB;
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}

	public RoomNode getHeadRoomNode() {
		return headRoomNode;
	}

	public String getWorkersHost() {
		return workersHost;
	}

	public void setWorkersHost(String workersHost) {
		this.workersHost = workersHost;
	}

	public static int getPort() {
		return PORT;
	}

	public void setHeadRoomNode(RoomNode headRoomNode) {
		this.headRoomNode = headRoomNode;
	}

}