package MutilSocket;

import maingame.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

	//max client connected.
	private static final int maxClientsCount = 10;
	
	private static final clientThread[] threads = new clientThread[maxClientsCount];
	
	//hash map user
	static HashMap<String, String> userList = new HashMap<String, String>();
	
	static HashMap<String, Integer> hostUsers = new HashMap<String, Integer >();
	
	static int limitUsersPerHost = 4;
	
	public static void readUserData()
	{
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
		//host server
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			//cant host server
			System.out.println(e);
		}
		
		readUserData();
		
		while (true) {
			try {
				//accept a client and add to null position
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new clientThread(clientSocket, threads)).start();
						break;
					}
				}
				//if server is full
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
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private int maxClientsCount;

	private String hostGameName;
	
	private boolean isHost;
	
	private int Status;
	
	public clientThread(Socket clientSocket, clientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
		this.isHost  = false;
		this.Status = 0;
	}

	private void sendListHostToUser()
	{
		String hostList = "";
		Set<Entry<String, Integer>> keys = Server.hostUsers.entrySet();
		
		for(Entry<String,Integer> e : keys)
		{
			String k = e.getKey();
			int value = e.getValue();
			hostList += k + "\t" + value + "\t";
		}
		
		os.println(maingame.Message.GETHOSTLIST.value() + "\t" + 
				Server.hostUsers.size() + "\t" + hostList);
	}
	
	public void hostGame()
	{
		// if there is a client want to host a game
		Server.hostUsers.put(clientName, 1);
		synchronized (this) {
			for (int i = 0; i < maxClientsCount; i++) {
				// notify all client about host
				if (threads[i] != null
						&& threads[i].clientName != null) {
					threads[i].sendListHostToUser();
				}
			}
		}
	}
	
	private void cancelHost()
	{
		if (Server.hostUsers.containsKey(clientName))
			;
		{
			Server.hostUsers.remove(clientName);
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					// notify all client about host
					if (threads[i] != null
							&& threads[i].clientName != null) {
						threads[i].sendListHostToUser();
					}
				}
			}
		}
	
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
				// get user name

				data = is.readLine().trim();
				System.out.println(data);
				String[] part = data.split("\t");
				int messege = Integer.parseInt(part[0]);

				if (messege == maingame.Message.LOGIN.value()) {

					if (Server.userList.get(part[1]).compareTo(part[2]) == 0) {

						boolean isAlreadyLogin = false;
						for (int i = 0; i < maxClientsCount; i++) {
							// notify all client about host
							if (threads[i] != null
									&& threads[i].clientName != null
									&& threads[i].clientName.compareTo(part[1]) == 0) {
								os.println(maingame.Message.LOGIN_FAIL_ALREADY_LOGIN
										.value());
								isAlreadyLogin = true;
								break;
							}
						}
						if (!isAlreadyLogin) {
							clientName = part[1];
							this.Status = 1;
							os.println(maingame.Message.LOGIN_SUCCESSFUL
									.value());
							break;
						}
					} else {
						os.println(maingame.Message.LOGIN_FAIL_WROND_PASS
								.value());
					}
				}
			}
			
			while (true) {
				String part[] = is.readLine().split("\t");
				int result = Integer.parseInt(part[0]);
				if (result == Message.GETHOSTLIST.value()) {
					this.sendListHostToUser();
				} else if (result == Message.HOSTGAME.value()) {
					this.hostGame();
				} else if (result == Message.JOINGAME.value()) {
					if (Server.hostUsers.get(part[1]) != null) {
						int numberuserjoin = Server.hostUsers.get(part[1]);
						numberuserjoin++;
						if(numberuserjoin > Server.limitUsersPerHost)
						{
							os.println(maingame.Message.JOIN_FAIL
									.value());
						}
						else
							{
							Server.hostUsers.put(part[1], numberuserjoin);
							os.println(maingame.Message.JOIN_SUCCESSFULL
									.value() + "\t" + numberuserjoin);
							}
					}
				} else if (result == Message.CANCELHOST.value()) {
					cancelHost();
				}
				else
					if(result == Message.QUITGAME.value())
					{
						synchronized (this) {
							for (int i = 0; i < maxClientsCount; i++) {
								if (threads[i] == this) {
									System.out.println(threads[i].clientName + " is about to log out");
									threads[i] = null;
								}
							}
						}
					}
			}
		} catch (IOException e) {
		}
	}
}
