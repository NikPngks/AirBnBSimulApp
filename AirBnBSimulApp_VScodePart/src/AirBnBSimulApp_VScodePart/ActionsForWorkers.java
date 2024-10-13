package AirBnBSimulApp_VScodePart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ActionsForWorkers extends Thread {
	private static final long serialVersionUID = 8626459955423694374L;
	ObjectInputStream in;
	ObjectOutputStream out;

	private List<ManagerPack> packetsM = new ArrayList<>();
	private List<UserPack> packetsU = new ArrayList<>();

	public ActionsForWorkers(Socket connectionWR, List<ManagerPack> packetsM, List<UserPack> packetsU) {
		try {
			this.packetsM = packetsM;
			this.packetsU = packetsU;
			out = new ObjectOutputStream(connectionWR.getOutputStream());
			in = new ObjectInputStream(connectionWR.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// so it makes results towards the Master
			Socket requestToMasterSocket = null;
			ObjectOutputStream out4 = null;
			ObjectInputStream in4 = null;

			System.out.println("Worker's request is cought, plz wait");
			// here receives worker's pack
			Object workerPack = (Object) in.readObject();
			// check the pack type, is it manager's or user's pack
			if (workerPack instanceof ManagerPack) { // if it is manager's pack...
				ManagerPack masterPackNew = (ManagerPack) workerPack;
				// I will check the package pointer to know what action to take
				// and I will also check the sender id so I know where to keep it
				/* idManager */ int idManager = masterPackNew.getIdManager();
				// up to here it receive the packs
				// the list is in the tempItem type of RoomNode
				/* listaX */ int listPackCounter = 0;// I'm counting the package I just received
				if (packetsM.isEmpty()) {
					packetsM.add(masterPackNew);
					System.out.println("Worker's pack received by reducer");
					String ansToWorker = "A pack received by worker";
					out.writeObject(ansToWorker);
					out.flush();
				} else {
					packetsM.add(masterPackNew); // add to list
					for (int i = 0; i < packetsM.size(); i++) {
						/* for the 4th choice do */if (packetsM.get(i).getIdManager() == idManager) {
							// in this if, I check if the packs from all workers have been received
							/* exactly the same actions */listPackCounter++;
						}
					}
					if (listPackCounter == packetsM.get(0).getNoOfWorkers()) {// if the packs I found are as the number
																				// of the workers
						ManagerPack returnedToManager = new ManagerPack();// I build a new pack with the other packs I
																			// want to send
						returnedToManager.setManagerSelectIndicator(masterPackNew.getManagerSelectIndicator());
						returnedToManager.setTempItem(new RoomNode());// I build a new RoomNode item in order to add the
																		// other packs
						returnedToManager.setIdManager(idManager);
						/* 4 */ if (returnedToManager.getTempItem() == null)
							returnedToManager.setTempItem(new RoomNode());
						/* 4 */ if (returnedToManager.getTempItem().getPrwthAreaBookings() == null)
							returnedToManager.getTempItem().setPrwthAreaBookings(new AreaBookings[20]);// I will set 20
																										// temporarily
																										// and if I see
																										// that it is
																										// not enough, I
																										// will call for
																										// an extension
						for (int j = packetsM.size(); j > 0; j--) {
							/* here check the */ if (packetsM.get(j - 1).getIdManager() == idManager) {
								// if the package I found is from the manager I was looking for
								/* pack's indicator ---> /*3 */ if (masterPackNew.getManagerSelectIndicator() == 3) {
									// display of reservations for your property
									/* 3 */ for (int w = 0; w < packetsM.get(j - 1).getTempItem().getN(); w++) {
										/* 3 */ if (packetsM.get(j - 1).getTempItem().getHeadRoomNode() != null) {
											// checks that the list was not returned empty
											/* 3 */ if (packetsM.get(j - 1).getTempItem().getHeadRoomNode()
													.getRoomItem() != null) {
												/* 3 */ returnedToManager.setManagerSelectIndicator(3);
												/* 3 */ returnedToManager.getTempItem().addRoom(packetsM.get(j - 1)
														.getTempItem().getHeadRoomNode().getRoomItem());
												// prothetw to dwmatio sto neo ManagerPack pou eftiaksa
												/* 3 */ packetsM.get(j - 1).getTempItem().setHeadRoomNode(packetsM
														.get(j - 1).getTempItem().getHeadRoomNode().getPrevRoomNode());
												/* 3 */ }
											/* 3 */ } else {
											/* 3 */ w = packetsM.get(j - 1).getTempItem().getN() + 1;
											// I stop it if it is empty
											/* 3 */ }
										/* 3 */ }
									/* here check the */ /* 3 */ packetsM.remove(j - 1);
									// vgazw apo th lista tou worker
									/* pack's indicator---> /*4 */ } else {// means
																			// masterPackNew.getManagerSelectIndicator()==4
									returnedToManager.setManagerSelectIndicator(4);
									/* 4 */ // display of accommodation reservations in a specific period of time
									/* 4 */ if (packetsM.get(j - 1).getTempItem().getPrwthAreaBookings() != null) {
										// checks that the list was not returned empty
										/* for the length of the list of the first packet */ for (int q = 0; q < packetsM
												.get(j - 1).getTempItem().getPrwthAreaBookings().length; q++) {
											// node counter of array I got from workers
											/* for the length of the list of the packet which I will send */for (int p = 0; p < returnedToManager
													.getTempItem().getPrwthAreaBookings().length; p++) {
												// node counter of array I send back
												/* if the field of the list is null, add it */ if (packetsM.get(j - 1)
														.getTempItem().getPrwthAreaBookings()[q] != null) {
													if (returnedToManager.getTempItem()
															.getPrwthAreaBookings()[p] == null) {// here it checks if it
																									// has gone to empty
																									// which has no area
														returnedToManager.getTempItem()
																.getPrwthAreaBookings()[p] = packetsM.get(j - 1)
																		.getTempItem().getPrwthAreaBookings()[q];
														returnedToManager.getTempItem().getPrwthAreaBookings()[p]
																.setArea(packetsM.get(j - 1).getTempItem()
																		.getPrwthAreaBookings()[q].getArea());// kai an
																												// den
																												// exei
																												// thn
																												// prosthetei
														returnedToManager.getTempItem().getPrwthAreaBookings()[p]
																.setSumReservations(packetsM.get(j - 1).getTempItem()
																		.getPrwthAreaBookings()[q]
																		.getSumReservations());// kai an den exei thn
																								// prosthetei
														p = returnedToManager.getTempItem()
																.getPrwthAreaBookings().length;// to break the
																								// intermediate for loop
													} else if (returnedToManager.getTempItem().getPrwthAreaBookings()[p]
															.getArea().equals(packetsM.get(j - 1).getTempItem()
																	.getPrwthAreaBookings()[q].getArea())) {
														returnedToManager.getTempItem().getPrwthAreaBookings()[p]
																.setSumReservations(returnedToManager.getTempItem()
																		.getPrwthAreaBookings()[p].getSumReservations()
																		+ packetsM.get(j - 1).getTempItem()
																				.getPrwthAreaBookings()[q]
																				.getSumReservations());// kai an den
																										// exei thn
																										// prosthetei
														p = returnedToManager.getTempItem()
																.getPrwthAreaBookings().length;// to break the
																								// intermediate for loop
													}
												} else {
													p = returnedToManager.getTempItem().getPrwthAreaBookings().length;// to
																														// break
																														// the
																														// intermediate
																														// for
																														// loop
													q = packetsM.get(j - 1).getTempItem().getPrwthAreaBookings().length;
												}
											}
										}
										packetsM.remove(j - 1);// remove from worker's list
									}
									/* 4 */}
							}
						}
						// I pass the package to the master from the reducer, the room reservations
						returnedToManager.setSender("Reducer");
						/* ADD Master's PORT */ requestToMasterSocket = new Socket("127.0.0.1", 1234);// to host tou
																										// Master
						out4 = new ObjectOutputStream(requestToMasterSocket.getOutputStream());
						out4.writeObject(returnedToManager);
						out4.flush();
						System.out.println("the request response is sent to the master.(printing in reducer)");
						String ansToWorker = "A packet was received from worker";
						out.writeObject(ansToWorker);
						out.flush();

					} else {
						System.out.println("A packet (manager) was received from worker (printing in reducer)");
						String ansToWorker = "A packet was received from worker and has beed saved to the DB";
						out.writeObject(ansToWorker);
						out.flush();
					}
				}

			} else { // means workerPack instanceof UserPack
				UserPack masterPackNew = (UserPack) workerPack;
				// I will check the package pointer to know what action to take
				// and I will also check the sender id so I know where to keep it
				/* idManager */ int idUser = masterPackNew.getIdUser();
				// the only case is the return of the rooms after the ocher filters
				int listPackCounter = 0;// I'm counting the package I just received
				if (packetsU.isEmpty()) {
					packetsU.add(masterPackNew);
					System.out.println("The first package (user " + masterPackNew.getIdUser()
							+ ") was recieved from worker (printing in reducer) and waits");
					String ansToWorker = "The  package  was recieved from worker";
					out.writeObject(ansToWorker);
					out.flush();
				} else {
					packetsU.add(masterPackNew);
					for (int i = 0; i < packetsU.size(); i++) {
						if (packetsU.get(i).getIdUser() == idUser) {// this if is to see if the packets have arrived
																	// from all oorkers
							listPackCounter++;
						}
					}
					if (listPackCounter == packetsU.get(0).getNoOfWorkers()) {
						UserPack returnedToUser = new UserPack();
						returnedToUser.setIdUser(idUser);
						returnedToUser.setUserSelectIndicator(1);
						returnedToUser.setTempItem(new RoomNode());
						if (returnedToUser.getTempItem() == null)
							returnedToUser.setTempItem(new RoomNode());
						for (int j = packetsU.size(); j > 0; j--) {
							if (packetsU.get(j - 1).getIdUser() == idUser) {
								for (int w = 0; w < packetsU.get(j - 1).getTempItem().getN(); w++) {
									if (packetsU.get(j - 1).getTempItem().getHeadRoomNode() != null) {// checks that the
																										// list was not
																										// returned
																										// empty
										if (packetsU.get(j - 1).getTempItem().getHeadRoomNode().getRoomItem() != null) {
											returnedToUser.getTempItem().addRoom(
													packetsU.get(j - 1).getTempItem().getHeadRoomNode().getRoomItem());// add
																														// the
																														// room
																														// to
																														// the
																														// new
																														// UserPack
																														// I
																														// made
											packetsU.get(j - 1).getTempItem().setHeadRoomNode(packetsU.get(j - 1)
													.getTempItem().getHeadRoomNode().getPrevRoomNode());
										}
									} else {
										j = packetsU.get(j).getTempItem().getN() + 1;// I stop it if it is empty
									}
								}
								packetsU.remove(j - 1);// remove from worker's list
							}
						}
						returnedToUser.setSender("Reducer");
						/* ADD Master's PORT */ requestToMasterSocket = new Socket("127.0.0.1", 1234);
						out4 = new ObjectOutputStream(requestToMasterSocket.getOutputStream());
						out4.writeObject(returnedToUser);
						out4.flush();
						System.out.println("the response of the ocher request is sent " + returnedToUser.getIdUser()
								+ " to master.(printing in reducer)");
						String ansToWorker = "The  package  was recieved from worker (sent by user "
								+ returnedToUser.getIdUser() + ")";
						out.writeObject(ansToWorker);
						out.flush();
					} else {
						System.out.println("A package (user " + masterPackNew.getIdUser()
								+ ") was recieved from worker (printing in reducer)");
						String ansToWorker = "A package (user " + masterPackNew.getIdUser()
								+ ") was recieved from worker and has beed saved to the DB";
						out.writeObject(ansToWorker);
						out.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}