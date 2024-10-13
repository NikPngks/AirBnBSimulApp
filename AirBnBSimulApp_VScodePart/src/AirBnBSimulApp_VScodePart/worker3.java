package AirBnBSimulApp_VScodePart; //worker with workerId=2!!

//If you run this class, you should not run the worker!!!

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class worker3 extends Thread { // server on the master and a client on the reducerr
	private static final long serialVersionUID = 8626459955423694374L;
	private int workerId;
	private String workersHost;
	// will make a base of nodes for rooms in the worker
	private RoomNode headRoomNode;// the root
	private ServerSocket providerWorkerSocket;
	private Socket connectionMW = null;
	public static final int PORT = 2347; // 2346 and 2347 the other two

	public static void main(String[] args) {
		RoomNode workerRoomDB = new RoomNode();
		int workerId = 2;
		worker3 worker = new worker3(workerRoomDB, workerId);
		worker.start();
	}

	@Override
	public void run() {
		try {
			providerWorkerSocket = new ServerSocket(this.workerId + 2345, 50);
			while (true) {
				connectionMW = providerWorkerSocket.accept(); // accepts the connection to the master
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

	public worker3(String workersHost, RoomNode workerRoomDB, int workerId) {
		this.workersHost = workersHost;
		this.workerId = workerId;
		this.providerWorkerSocket = providerWorkerSocket;
		this.connectionMW = connectionMW;
		this.headRoomNode = workerRoomDB;
	}

	public worker3(RoomNode workerRoomDB, int workerId) {
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