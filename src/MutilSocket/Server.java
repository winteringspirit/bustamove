package MutilSocket;

import maingame.Globals;
import maingame.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;

/*
 * A chat server that delivers public and private messages.
 */
public class Server {

	// The server socket.
	private static ServerSocket serverSocket = null;
	// The client socket.
	private static Socket clientSocket = null;

	// max client connected.
	private static final int maxClientsCount = 10;

	// total client connect to server
	private static final clientThread[] threads = new clientThread[maxClientsCount];

	// hash map user from textfile
	static HashMap<String, String> userList = new HashMap<String, String>();

	// hash map all current host
	static HashMap<String, String[]> hostUsers = new HashMap<String, String[]>();

	public static ArrayList<String> currentStartedHost = new ArrayList<String>();

	// limit user join per host
	static int limitUsersPerHost = 4;

	public static void readUserData() {
		// clear list user
		userList.clear();
		// read user data
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("datas\\users.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				String[] parts = sCurrentLine.split("\t");
				userList.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		// The default port number.
		int portNumber = 2222;
		System.out.println("server game was hosted at port: " + portNumber);
		// host server
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			// cant host server
			System.out.println(e);
		}

		// read user data
		readUserData();

		while (true) {
			try {
				// accept a client and add to null position
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new clientThread(clientSocket, threads))
								.start();
						break;
					}
				}
				// if server is full
				if (i == maxClientsCount) {
					PrintStream os = new PrintStream(
							clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}

class clientThread extends Thread {

	private String clientName = null;
	public String hostName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private clientThread[] client;
	private int maxClientsCount;

	private boolean isHost;

	private int Status;

	Random randomGenerator = new Random();

	public clientThread(Socket clientSocket, clientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
		this.isHost = false;
		this.Status = 0;
		client = new clientThread[4];
	}

	private void notifyEditUser(String[] parts) {
		String message = maingame.Message.EDIT_PLAYER.value() + "\t";
		for (int i = 0; i < parts.length; i++) {
			message += parts[0] + "\t" + parts[1] + "\t" + parts[2] + "\t"
					+ parts[3] + "\t";
		}
		os.println(message);
	}

	private void notifyAbandonHost(String[] parts) {
		os.println(String.valueOf(Message.ABANDON_HOST.value()));
	}

	private void notifyStartGame() {
		if (Server.hostUsers.get(this.clientName) != null) {
			Server.currentStartedHost.add(this.clientName);
			String[] user = Server.hostUsers.get(this.clientName);
			for (int i = 0; i < user.length; i++) {
				if (user[i] != null) {
					for (int j = 0; j < maxClientsCount; j++) {
						if (threads[j] != null
								&& threads[j].clientName != null
								&& threads[j].clientName.compareTo(user[i]) == 0) {
							client[i] = threads[j];
						}
					}
				}
			}

			// pointing client to all client
			for (int i = 0; i < client.length; i++) {
				if (client[i] != null)
					client[i].client = client;
			}

			String userString = "";
			for (int i = 0; i < user.length; i++) {
				userString += user[i] + "\t";
			}

			// send list bubble
			String bubbleColorMessage = String.valueOf(Message.BUBBLE_COLOR
					.value()) + "\t";
			for (int i = 0; i < 8; i++) {
				int randomInt = randomGenerator.nextInt(Globals.BubbleColor);
				int percentCreate = randomGenerator.nextInt(100);
				if (percentCreate < 90)
					bubbleColorMessage += String.valueOf(randomInt) + "\t";
				else
					bubbleColorMessage += "null\t";
			}

			for (int i = 0; i < client.length; i++) {
				if (client[i] != null) {
					client[i].os.println(bubbleColorMessage);
				}
			}

			String bulletcolormessage = String.valueOf(Message.BUBBLE_BULLET
					.value()) + "\t";
			for (int i = 0; i < 5; i++) {
				int randomInt = randomGenerator.nextInt(Globals.BubbleColor);
				bulletcolormessage += String.valueOf(randomInt) + "\t";
			}
			// send list bubble bullet
			for (int i = 0; i < client.length; i++) {
				if (client[i] != null) {
					client[i].os.println(bulletcolormessage);
				}
			}

			// send user list
			for (int i = 0; i < client.length; i++) {
				if (client[i] != null) {
					client[i].os.println(String.valueOf(Message.START_GAME
							.value()) + "\t" + userString);
				}
			}
		}
	}

	private void notifyHostAbandonGame() {

		if (Server.hostUsers.get(this.clientName) != null) {
			Server.hostUsers.remove(this.clientName);
			Server.currentStartedHost.remove(this.clientName);
		}

		for (int i = 0; i < client.length; i++) {
			if (client[i] != null) {
				client[i].os.println(String.valueOf(Message.HOST_ABANDON_GAME
						.value()));
				this.isHost = false;
			}
		}
		// client = null;
	}

	private void notifyClientAbandonGame() {
		for (int i = 0; i < client.length; i++) {
			if (client[i] != null)
				client[i].os.println(String.valueOf(Message.CLIEN_ABANDON_GAME
						.value()) + "\t" + this.clientName + "\t");
		}
	}

	private void notifyKeyPressed(int key) {
		if (Server.hostUsers.get(this.hostName) != null) {
			String _userList[] = Server.hostUsers.get(this.hostName);
			String message = String.valueOf(Message.KEY_PRESS.value()) + "\t";
			for (int i = 0; i < _userList.length; i++) {
				String keypress = null;
				if (_userList[i] != null
						&& _userList[i].compareTo(this.clientName) == 0) {
					keypress = String.valueOf(key);
				}
				message += keypress + "\t";
			}

			synchronized (this) {
				for (int m = 0; m < _userList.length; m++) {
					for (int k = 0; k < maxClientsCount; k++) {
						if (threads[k] != null
								&& threads[k].clientName != null
								&& _userList[m] != null
								&& threads[k].clientName
										.compareTo(_userList[m]) == 0) {
							threads[k].os.println(message);
						}
					}
				}
			}
		}
	}

	private void notifyCreateBubble(String message) {
		for (int i = 0; i < client.length; i++) {
			if (client[i] != null)
				client[i].os.println(message);
		}
	}

	private void addBubbleBullet() {
		String bulletcolormessage = String.valueOf(Message.BUBBLE_BULLET
				.value()) + "\t";
		for (int i = 0; i < 1; i++) {
			int randomInt = randomGenerator.nextInt(Globals.BubbleColor);
			bulletcolormessage += String.valueOf(randomInt) + "\t";
		}

		// send list bubble bullet
		for (int i = 0; i < client.length; i++) {
			if (client[i] != null) {
				client[i].os.println(bulletcolormessage);
			}
		}
	}

	private void sendListHostToUser() {
		String hostList = "";
		Set<Entry<String, String[]>> keys = Server.hostUsers.entrySet();

		for (Entry<String, String[]> e : keys) {

			String k = e.getKey();
			boolean isHostStart = false;
			for (int j = 0; j < Server.currentStartedHost.size(); j++) {
				if (Server.currentStartedHost.get(j).compareTo(k) == 0) {
					isHostStart = true;
				}
			}

			if (!isHostStart) {
				String[] value = e.getValue();

				int availableHostCount = 0;
				for (int i = 0; i < value.length; i++) {
					if (value[i] != null)
						availableHostCount++;
				}
				hostList += k + "\t" + availableHostCount + "\t" + value[1]
						+ "\t" + value[2] + "\t" + value[3] + "\t";
			}
		}

		os.println(maingame.Message.GET_HOST_LIST.value() + "\t"
				+ Server.hostUsers.size() + "\t" + hostList);
	}

	public void hostGame() {
		// if there is a client want to host a game
		this.hostName = this.clientName;
		String[] joiner = new String[Server.limitUsersPerHost];
		joiner[0] = this.clientName;
		joiner[1] = null;
		joiner[2] = null;
		joiner[3] = null;
		Server.hostUsers.put(clientName, joiner);
		synchronized (this) {
			for (int i = 0; i < maxClientsCount; i++) {
				// notify all client about host
				if (threads[i] != null && threads[i].clientName != null) {
					threads[i].sendListHostToUser();
				}
			}
		}
		System.out.println(this.clientName + " is hosting game");
	}

	private void cancelHost() {
		if (Server.hostUsers.containsKey(clientName)) {
			System.out.println(this.clientName + " is cancled hosted game");
			Server.hostUsers.remove(clientName);
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					// notify all client about host
					if (threads[i] != null && threads[i].clientName != null) {
						threads[i].sendListHostToUser();
					}
				}
			}

			if (Server.currentStartedHost.contains(clientName)) {
				Server.currentStartedHost.remove(clientName);
			}
		}
	}

	private void joinGame(String[] parts) {
		if (Server.hostUsers.get(parts[1]) != null) {
			String[] userjoin = Server.hostUsers.get(parts[1]);
			boolean isJoinable = false;
			for (int i = 0; i < Server.limitUsersPerHost; i++) {
				if (userjoin[i] == null) {
					isJoinable = true;
					userjoin[i] = this.clientName;
					this.hostName = parts[1];
					Server.hostUsers.put(parts[1], userjoin);
					os.println(maingame.Message.JOIN_SUCCESSFULL.value() + "\t"
							+ userjoin[0] + "\t" + userjoin[1] + "\t"
							+ userjoin[2] + "\t" + userjoin[3] + "\t");
					break;
				}
			}

			if (!isJoinable) {
				os.println(maingame.Message.JOIN_FAIL.value());
				System.out.println("this " + parts[1] + " host is full");
			} else {
				// notify all client in host about there is a player
				System.out.println(this.clientName + " is joining "
						+ this.hostName + " host");
				synchronized (this) {
					for (int m = 0; m < userjoin.length; m++) {
						for (int k = 0; k < maxClientsCount; k++) {
							if (threads[k] != null
									&& threads[k].clientName != null
									&& userjoin[m] != null
									&& threads[k].clientName
											.compareTo(userjoin[m]) == 0) {
								threads[k].notifyEditUser(userjoin);
							}
						}
					}
				}
			}
		}
	}

	private void quitGame() {
		synchronized (this) {
			for (int i = 0; i < maxClientsCount; i++) {
				if (threads[i] == this) {
					System.out.println(threads[i].clientName
							+ " is about to quit game");
					threads[i] = null;
				}
			}
		}
	}

	private void abandonJoinGame() {
		if (Server.hostUsers.get(this.hostName) != null) {
			String _userList[] = Server.hostUsers.get(this.hostName);
			for (int i = 0; i < _userList.length; i++) {
				if (_userList[i] != null && _userList[i] == this.clientName) {
					_userList[i] = null;
				}
			}
			Server.hostUsers.put(this.hostName, _userList);
			synchronized (this) {
				for (int m = 0; m < _userList.length; m++) {
					for (int k = 0; k < maxClientsCount; k++) {
						if (threads[k] != null
								&& threads[k].clientName != null
								&& _userList[m] != null
								&& threads[k].clientName
										.compareTo(_userList[m]) == 0) {
							threads[k].notifyEditUser(_userList);
						}
					}
				}
			}
		}
	}

	private void abandonHostGame() {
		if (Server.hostUsers.get(this.clientName) != null) {
			String[] _userList = Server.hostUsers.get(this.clientName);
			synchronized (this) {
				for (int m = 0; m < _userList.length; m++) {
					for (int k = 0; k < maxClientsCount; k++) {
						if (threads[k] != null
								&& threads[k].clientName != null
								&& _userList[m] != null
								&& _userList[m].compareTo(this.clientName) != 0
								&& threads[k].clientName
										.compareTo(_userList[m]) == 0) {
							threads[k].notifyAbandonHost(_userList);
							threads[k].hostName = null;
						}
					}
				}
			}
			Server.hostUsers.remove(this.clientName);
		}

		this.sendListHostToUser();
	}

	public void CheckConnection() {

	}

	public void run() {
		int maxClientsCount = this.maxClientsCount;
		clientThread[] threads = this.threads;
		try {
			/*
			 * Create input and output streams for this client.
			 */
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			String data;
			// login
			while (true) {
				/*
				if (!clientSocket.isConnected()) {
					break;
				}*/
				// get user name
				data = is.readLine().trim();
				String[] part = data.split("\t");
				int messege = Integer.parseInt(part[0]);
				if (messege == maingame.Message.LOGIN.value()) {
					if (Server.userList.get(part[1]) != null
							&& Server.userList.get(part[1]).compareTo(part[2]) == 0) {
						boolean isAlreadyLogin = false;
						for (int i = 0; i < maxClientsCount; i++) {
							// notify all client about host
							if (threads[i] != null
									&& threads[i].clientName != null
									&& threads[i].clientName.compareTo(part[1]) == 0) {
								os.println(maingame.Message.LOGIN_FAIL_ALREADY_LOGIN
										.value());
								isAlreadyLogin = true;

								System.out
										.println(clientName
												+ " login fail because it's already loged");
								break;
							}
						}
						if (!isAlreadyLogin) {
							clientName = part[1];
							this.Status = 1;
							os.println(maingame.Message.LOGIN_SUCCESSFUL
									.value());
							System.out.println(clientName + " login succefull");
							break;
						}
					} else {
						os.println(maingame.Message.LOGIN_FAIL_WROND_PASS
								.value());
						System.out.println(clientName
								+ " wrongs pass or wrongs user name");
					}
				} else if (messege == Message.QUIT_GAME.value()) {
					quitGame();
					return;
				}
			}

			while (true) {
				if (!clientSocket.isConnected()) {
					if (Server.currentStartedHost.contains(clientName)) {
						Server.currentStartedHost.remove(clientName);
					}
					if (Server.hostUsers.containsKey(this.clientName)) {
						Server.hostUsers.remove(this.clientName);
					}
					if (Server.userList.containsKey(this.clientName)) {
						Server.userList.remove(this.clientName);
					}
					break;
				}
				String ms = is.readLine().trim();
				String parts[] = ms.split("\t");

				int result = Integer.parseInt(parts[0]);

				if (result == Message.ABANDON_JOIN.value()) {
					abandonJoinGame();
				} else if (result == Message.ABANDON_HOST.value()) {
					abandonHostGame();
				} else if (result == Message.GET_HOST_LIST.value()) {
					this.sendListHostToUser();
				} else if (result == Message.HOST_GAME.value()) {
					this.hostGame();
				} else if (result == Message.JOIN_GAME.value()) {
					joinGame(parts);
				} else if (result == Message.CANCEL_HOST.value()) {
					cancelHost();
				} else if (result == Message.QUIT_GAME.value()) {
					quitGame();
					break;
				} else if (result == Message.START_GAME.value()) {
					notifyStartGame();
				} else if (result == Message.KEY_FIRE.value()) {
					notifyKeyPressed(Message.KEY_FIRE.value());
					addBubbleBullet();
				} else if (result == Message.KEY_LEFT.value()) {
					notifyKeyPressed(Message.KEY_LEFT.value());
				} else if (result == Message.KEY_RIGHT.value()) {
					notifyKeyPressed(Message.KEY_RIGHT.value());
				} else if (result == Message.NO_KEY.value()) {
					notifyKeyPressed(Message.NO_KEY.value());
				} else if (result == Message.BUBBLE_COLOR.value()) {
					// only host can recieve because this message send form host
					notifyCreateBubble(ms);
				} else if (result == Message.HOST_ABANDON_GAME.value()) {
					notifyHostAbandonGame();
				} else if (result == Message.CLIEN_ABANDON_GAME.value()) {
					notifyClientAbandonGame();
				}
			}
		} catch (IOException e) {
		}
	}
}
