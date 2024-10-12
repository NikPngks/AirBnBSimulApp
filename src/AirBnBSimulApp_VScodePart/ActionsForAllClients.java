package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ActionsForAllClients extends Thread {
	private static final long serialVersionUID = 8626459955423694374L;
	ObjectInputStream in;
	ObjectOutputStream out;
	private int noOfWorkers;
	Socket connectionTemp;// Temporary socket connection for the client
	boolean succRegistrIndicator = true;// Indicator for successful registration

	public ActionsForAllClients(Socket connection, int noOfWorkers) {
		try {
			this.connectionTemp = connection;
			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());
			this.noOfWorkers = noOfWorkers;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Object receivedPack = (Object) in.readObject();// Read the incoming object from the client
			if (receivedPack instanceof ManagerPack) { // If the package is from a manager
				ManagerPack receivedManPack = (ManagerPack) receivedPack;
				if (receivedManPack.getSender().equals("Manager")) { // Check if it's from a manager
					ActionsForManagers(this.connectionTemp, noOfWorkers, receivedPack);
				} else {
					ActionsForReducers(this.connectionTemp, receivedPack); // If from a reducer
				}
			} else { // If the package is from a user
				UserPack receivedUserPack = (UserPack) receivedPack;
				if (receivedUserPack.getSender().equals("User")) { // Check if it's from a user
					this.ActionsForUsers(this.connectionTemp, noOfWorkers, receivedPack);
				} else {
					ActionsForReducers(this.connectionTemp, receivedPack); // If from a reducer
				}
			}
		} catch (IOException | ClassNotFoundException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				if (connectionTemp != null) {
					connectionTemp.close();
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private void ActionsForUsers(Socket connection, int noOfWorkers, Object receivedPack) {
		UserPack userPack = (UserPack) receivedPack;// Cast to UserPack
		userPack.setNoOfWorkers(this.noOfWorkers);// Set the number of workers in the user pack
		System.out.println("user" + userPack.getIdUser() + "'s request is caught, please wait");

		Socket requestToWorkersSocket = null; // Socket to communicate with worker nodes
		ObjectOutputStream out2 = null; // Output stream to send data to workers
		ObjectInputStream in2 = null; // Input stream to read data from workers

		try {
			if (userPack.getUserSelectIndicator() == 1) {// 1st filter addition
				for (int w = 0; w < noOfWorkers; w++) {
					// Create a socket to communicate with the worker
					requestToWorkersSocket = new Socket(Master.workersEx.get(w).getWorkersHost(), 2345 + w);
					out2 = new ObjectOutputStream(requestToWorkersSocket.getOutputStream());
					out2.writeObject(userPack);// Send user pack to worker
					out2.flush();
					// Read response from the worker
					in2 = new ObjectInputStream(requestToWorkersSocket.getInputStream());
					String ansWorker = (String) in2.readObject();
					System.out.print(ansWorker + " from worker " + w + " (printint in Master)");
					out2.close();
					in2.close();
					requestToWorkersSocket.close();
				}
				boolean loop2 = true;// Loop to check for user requests
				while (loop2) {
					String check = "User" + userPack.getIdUser();
					for (ClientRequest i : Master.requestsUsrMngr) {
						if (i.getRequestBody().equals(check)) {
							UserPack manPack = (UserPack) i.getPaketaReducer();// Get the manager pack
							out.writeObject(manPack);// Send response to the user
							out.flush();
							Master.requestsUsrMngr.remove(i);// Remove processed request
							loop2 = false;// Exit the loop
							out.close();
							in.close();
						}
					}
				}
			} else if (userPack.getUserSelectIndicator() == 2 || userPack.getUserSelectIndicator() == 3) {
				// Room reservation or evaluation
				Room tempRoom = new Room();// Create a Room to call the hash method
				tempRoom.setRoomName(userPack.getRoomNameToBeBooked());
				int worker = tempRoom.hashFunction(5, noOfWorkers);// Determine which worker handles the room

				// Create a socket to communicate with the determined worker
				requestToWorkersSocket = new Socket(Master.workersEx.get(worker).getWorkersHost(), 2345 + worker);
				out2 = new ObjectOutputStream(requestToWorkersSocket.getOutputStream());
				in2 = new ObjectInputStream(requestToWorkersSocket.getInputStream());
				out2.writeObject(userPack);// Send the user pack to the worker
				out2.flush();
				String workerAnswer = (String) in2.readObject();// Get the response from the worker
				System.out.println(workerAnswer + " (Printing in Master)\n");

				// Check the response and set the registration indicator
				if (!workerAnswer.equals("Room update successful")) {
					succRegistrIndicator = false;
				}
				String updateInfoUser = "successful selection execution " + userPack.getUserSelectIndicator() + " User "
						+ userPack.getIdUser() + "\n";
				if (!succRegistrIndicator) {
					updateInfoUser = "unsuccessful selection execution " + userPack.getUserSelectIndicator() + " User "
							+ userPack.getIdUser() + "\n";
				}
				succRegistrIndicator = true;// Reset the indicator for next use
				out.writeObject(updateInfoUser);// Send the response back to the user
				out.flush();
				requestToWorkersSocket.close();
			} else {
				System.out.print("No valid option, end of user program");
			}
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void ActionsForReducers(Socket connection, Object receivedPack) {
		System.out.println("\n Reducer's pack is caught, please wait");
		Object workerPack = (Object) receivedPack;
		if (workerPack instanceof ManagerPack) {// If it's a ManagerPack
			ManagerPack masterPackNew = (ManagerPack) workerPack;
			String check = "Manager" + masterPackNew.getIdManager();// Check manager ID
			ClientRequest itemList = new ClientRequest(workerPack, check);
			synchronized (Master.requestsUsrMngr) {
				Master.requestsUsrMngr.push(itemList); // Add request to the list
			}
		} else {
			UserPack masterPackNew = (UserPack) workerPack;// Otherwise, it's a UserPack
			String check = "User" + masterPackNew.getIdUser();// Check user ID
			ClientRequest itemList = new ClientRequest(workerPack, check);
			synchronized (Master.requestsUsrMngr) {
				Master.requestsUsrMngr.push(itemList);// Add request to the list
			}
		}
	}

	public void ActionsForManagers(Socket connection, int noOfWorkers, Object receivedPack) {
		try {
			System.out.println("Manager's request is caught, please wait");
			ManagerPack managerPack = (ManagerPack) receivedPack;// Cast to ManagerPack
			managerPack.setNoOfWorkers(this.noOfWorkers);
			Socket requestToWorkersSocket = null;// Socket to communicate with worker nodes
			ObjectOutputStream out2 = null;// Output stream to send data to workers
			ObjectInputStream in2 = null;// Input stream to read data from workers
			try {
				if (managerPack.getManagerSelectIndicator() == 1) {// 1st choice of room entry
					Room[] tempRooms = managerPack.getRooms();
					ManagerPack[] managPackArray = new ManagerPack[managerPack.getNoOfWorkers()];
					for (int s = 0; s < managerPack.getNoOfWorkers(); s++) {
						managPackArray[s] = new ManagerPack();
						managPackArray[s].setTempItem(new RoomNode());
						managPackArray[s].setIdManager(managerPack.getIdManager());
						managPackArray[s].setManagerSelectIndicator(1);
					}
					for (Room i : tempRooms) {
						int pntrWorker = i.hashFunction(5, noOfWorkers);
						managPackArray[pntrWorker].getTempItem().addRoom(i);
					}
					for (int a = 0; a < managPackArray.length; a++) {
						// now it sends the packets to the workers
						requestToWorkersSocket = new Socket(Master.workersEx.get(a).getWorkersHost(), 2345 + a);
						// add the tempInt to address to worker which hash gave me to add the room
						out2 = new ObjectOutputStream(requestToWorkersSocket.getOutputStream());
						in2 = new ObjectInputStream(requestToWorkersSocket.getInputStream());
						ManagerPack managerPackToSend = managPackArray[a];
						out2.writeObject(managerPackToSend); // sends the i which is one of the imported rooms to the
																// worker 2345+ hash result
						out2.flush();

						String workerAnswer = (String) in2.readObject();

						if (!workerAnswer.equals("Room registration successful")) {
							succRegistrIndicator = false;
						}
					}
					String infoManager = "successful execution of option 1 Manager " + managerPack.getIdManager();
					if (!succRegistrIndicator) {
						infoManager = "unsuccessful execution of option 1 Manager" + managerPack.getIdManager();
					}
					out.writeObject(infoManager);
					// sends this response back to the manager
					/* 1st choise */ out.flush();
					requestToWorkersSocket.close();
					/* 2nd choise */ } else if (managerPack.getManagerSelectIndicator() == 2) {// 2nd option available
																								// days
					Room tempRoom = new Room();// I'm making a Room to call the hash method
					tempRoom.setRoomName(managerPack.getRoomNameForChangeAvailability());
					int worker = tempRoom.hashFunction(5, noOfWorkers);// this is the worker in which the room that the
																		// manager wants to change availability is
																		// located
					requestToWorkersSocket = new Socket(Master.workersEx.get(worker).getWorkersHost(), 2345 + worker);
					out2 = new ObjectOutputStream(requestToWorkersSocket.getOutputStream());
					in2 = new ObjectInputStream(requestToWorkersSocket.getInputStream());
					out2.writeObject(managerPack);
					out2.flush();
					String workerAnswer = (String) in2.readObject();
					if (!workerAnswer.equals("Room update successful")) {
						succRegistrIndicator = false;
					}
					String infoManager = "Successful execution of option 2 Manager " + managerPack.getIdManager();
					if (!succRegistrIndicator) {
						infoManager = "Unsuccessful execution of option 2 Manager" + managerPack.getIdManager();
					}
					succRegistrIndicator = true;
					out.writeObject(infoManager);// sends this response back to the manager
					out.flush();
					/* 2nd choise */ requestToWorkersSocket.close();

				} else if (managerPack.getManagerSelectIndicator() == 3
						|| managerPack.getManagerSelectIndicator() == 4) {
					/* 3rd 4th choise */
					for (int w = 0; w < noOfWorkers; w++) {
						requestToWorkersSocket = new Socket(Master.workersEx.get(w).getWorkersHost(), 2345 + w);
						out2 = new ObjectOutputStream(requestToWorkersSocket.getOutputStream());
						out2.writeObject(managerPack);
						out2.flush();
						in2 = new ObjectInputStream(requestToWorkersSocket.getInputStream());
						String ansWorker = (String) in2.readObject();
						System.out.print(ansWorker + " from worker " + w + " (printing in Master)");
						out2.close();
						in2.close();
						requestToWorkersSocket.close();

					}

					boolean loop = true;
					while (loop) {
						String check = "Manager" + managerPack.getIdManager();
						for (ClientRequest i : Master.requestsUsrMngr) {
							if (i.getRequestBody().equals(check)) {
								ManagerPack manPack = (ManagerPack) i.getPaketaReducer();
								out.writeObject(manPack);
								out.flush();
								Master.requestsUsrMngr.remove(i);// Remove processed request
								loop = false;// Exit the loop
								out.close();
								in.close();
							}
						}
					}

				} else {
					System.out.print("No option, end of manager program");
					connectionTemp.close();
				}
			} catch (UnknownHostException unknownHost) {
				System.err.println("You are trying to connect to an unknown host!");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}