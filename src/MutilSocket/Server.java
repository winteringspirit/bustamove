package MutilSocket;

import maingame.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
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

	private static final clientThread[] threads = new clientThread[maxClientsCount];

	// hash map user
	static HashMap<String, String> userList = new HashMap<String, String>();

	static HashMap<String, String[]> hostUsers = new HashMap<String, String[]>();

	static int limitUsersPerHost = 4;

	public static void readUserData() {
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

	public clientThread(Socket clientSocket, clientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
		this.isHost = false;
		this.Status = 0;
		client =  new clientThread[4];
	}

	private void notifyAddUser(String []parts)
	{
		String message = maingame.Message.EDITPLAYER.value() + "\t";
		for(int i = 0; i < parts.length; i++)
		{
			message += parts[0]  + "\t" + parts[1] + "\t" + parts[2]
					+ "\t" + parts[3] + "\t" ;
		}
		os.println(message);
	}
	
	private void notifyAbandonHost(String []parts)
	{
		os.println(String.valueOf(Message.ABANDON_HOST.value()));
	}
	
	private void notifyStartGame()
	{
		if(Server.hostUsers.get(this.clientName) != null)
		{
			String[] user = Server.hostUsers.get(this.clientName);
			for(int i = 0; i < user.length; i++)
			{
				if(user[i] != null)
				{
					for(int j = 0; j < maxClientsCount; j++)
					{
						if (threads[j] != null 
								&& threads[j].clientName != null 
								&& threads[j].clientName.compareTo(user[i])==0) {
							client[i] = threads[j];
						}
					}
				}
			}
			String userString = "";
			for(int i = 0; i < user.length; i++)
			{
				userString+= user[i]+"\t";
			}
			
			for(int i = 0; i < user.length; i++)
			{
				if( client[i] != null)
				{
					client[i].os.println(String.valueOf(Message.STARTGAME.value()) + "\t" + userString);
				}
			}
		}
	}
	
	private void sendListHostToUser() {
		
		String hostList = "";
		Set<Entry<String, String[]>> keys = Server.hostUsers.entrySet();

		for (Entry<String, String[]> e : keys) {
			String k = e.getKey();
			String[] value = e.getValue();
			
			int availableHostCount = 0;
			for(int i = 0; i < value.length; i++)
			{
				if(value[i] != null)
					availableHostCount++;
			}
			hostList += k + "\t" + availableHostCount + "\t" + value[1] + "\t" + value[2]
					+ "\t" + value[3] + "\t" ;
		}

		os.println(maingame.Message.GETHOSTLIST.value() + "\t"
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
	}

	private void cancelHost() {
		if (Server.hostUsers.containsKey(clientName))
			;
		{
			Server.hostUsers.remove(clientName);
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					// notify all client about host
					if (threads[i] != null && threads[i].clientName != null) {
						threads[i].sendListHostToUser();
					}
				}
			}
		}
	}

	private void joinGame(String[] part)
	{
		if (Server.hostUsers.get(part[1]) != null) {
			String[] userjoin = Server.hostUsers.get(part[1]);
			boolean isJoinable = false;
			for (int i = 0; i < Server.limitUsersPerHost; i++) {
				if (userjoin[i] == null) {
					isJoinable = true;
					userjoin[i] = this.clientName;
					this.hostName = part[1];
					Server.hostUsers.put(part[1], userjoin);
					os.println(maingame.Message.JOIN_SUCCESSFULL
							.value()
							+ "\t"
							+ userjoin[0]
							+ "\t"
							+ userjoin[1]
							+ "\t"
							+ userjoin[2]
							+ "\t" + userjoin[3] + "\t");
					break;
				}
			}

			if (!isJoinable) {
				os.println(maingame.Message.JOIN_FAIL.value());
			} else {
				// notify all client in host about there is a player
				// join
				System.out.println("there is a joinner join and notify all client");
				synchronized (this) {
					for (int m = 0; m < userjoin.length; m++) {
						for (int k = 0; k < maxClientsCount; k++) {
							if (threads[k] != null
									&& threads[k].clientName != null
									&& userjoin[m]!=null
									&& threads[k].clientName.compareTo(userjoin[m]) == 0)
								{
								threads[k].notifyAddUser(userjoin);
							}
						}
					}
				}
			}
		}
	}
	
	private void quitGame()
	{
		synchronized (this) {
			for (int i = 0; i < maxClientsCount; i++) {
				if (threads[i] == this) {
					System.out.println(threads[i].clientName
							+ " is about to log out");
					threads[i] = null;
				}
			}
		}
	}
	
	private void abandonJoinGame(String[] part)
	{
		if(Server.hostUsers.get(this.hostName)!=null)
		{
			String _userList[] = Server.hostUsers.get(this.hostName);
			for(int i = 0; i < _userList.length; i++)
			{
				if(_userList[i] != null && _userList[i] == this.clientName)
				{
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
								&& threads[k].clientName.compareTo(_userList[m]) == 0)
							{
							threads[k].notifyAddUser(_userList);
						}
					}
				}
			}
		}
	}
	
	private void abandonHostGame(String[] part)
	{
		if(Server.hostUsers.get(this.clientName) != null)
		{
			String[] _userList = Server.hostUsers.get(this.clientName);
			synchronized (this) {
				for (int m = 0; m < _userList.length; m++) {
					for (int k = 0; k < maxClientsCount; k++) {
						if (threads[k] != null
								&& threads[k].clientName != null
								&& _userList[m] != null
								&& _userList[m].compareTo(this.clientName) != 0
								&& threads[k].clientName.compareTo(_userList[m]) == 0)
							{
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
					if (Server.userList.get(part[1])!=null
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
				String ms = is.readLine().trim();
				String part[] = ms.split("\t");
				
				int result = Integer.parseInt(part[0]);
				
				if (result == Message.ABANDON_JOIN.value()) {
					abandonJoinGame(part);
				} else if (result == Message.ABANDON_HOST.value()) {
					abandonHostGame(part);
				} else if (result == Message.GETHOSTLIST.value()) {
					this.sendListHostToUser();
				} else if (result == Message.HOSTGAME.value()) {
					this.hostGame();
				} else if (result == Message.JOINGAME.value()) {
					joinGame(part);
				} else if (result == Message.CANCELHOST.value()) {
					cancelHost();
				} else if (result == Message.QUITGAME.value()) {
					quitGame();
					break;
				} else if(result == Message.STARTGAME.value()){
					notifyStartGame();
				} else if(result == Message.KEY_FIRE.value())
				{
					
				}else if(result == Message.KEY_LEFT.value())
				{
					
				}else if(result==Message.KEY_RIGHT.value())
				{
					
				}
			}
		} catch (IOException e) {
		}
	}
}
