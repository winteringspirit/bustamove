package maingame;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import MutilSocket.ClientListener;

public class Globals {
	// collision
	static float normalx = 0, normaly = 0;

	public static int BubbleColor = 4;

	static public GameStatus gameStatus;

	static public int FrameRate = 30;

	static final int SCREENWIDTH = 1200;
	static final int SCREENHEIGHT = 600;
	
	// The client socket
	public static Socket clientSocket = null;
	// The output stream
	public static PrintStream os = null;
	// The input stream
	public static DataInputStream is = null;

	public static BufferedReader inputLine = null;

	public static ArrayList<String> ServerMessage = new ArrayList<String>();

	public static String userName = "";

	public static boolean isHost = false;

	// returns true if the boxes are colliding (velocities are not used)
	public static boolean AABBCheck(Box b1, Box b2) {
		return !(b1.x + b1.w < b2.x || b1.x > b2.x + b2.w || b1.y + b1.h < b2.y || b1.y > b2.y
				+ b2.h);
	}

	static boolean AABB(Box box1, Box box2) {
		Box b1 = new Box(box1);
		Box b2 = new Box(box2);

		float moveX, moveY;
		b1.x = b1.x - b1.w / 2;
		b1.y = b1.y - b1.h / 2;
		b2.x = b2.x - b2.w / 2;
		b2.y = b2.y - b2.h / 2;

		float l = b2.x - (b1.x + b1.w);
		float r = (b2.x + b2.w) - b1.x;
		float t = b2.y - (b1.y + b1.h);
		float b = (b2.y + b2.h) - b1.y;

		// check that there was a collision
		if (l > 0 || r < 0 || t > 0 || b < 0)
			return false;

		// find the offset of both sides
		moveX = Math.abs(l) < r ? l : r;
		moveY = Math.abs(t) < b ? t : b;

		// only use whichever offset is the smallest
		if (Math.abs(moveX) < Math.abs(moveY))
			moveY = 0.0f;
		else
			moveX = 0.0f;

		if (moveX <= b1.vx)
			b1.vx = moveX;
		if (moveY <= b1.vy)
			b1.vy = moveY;

		return true;
	}

	// returns a box the spans both a current box and the destination box
	public static Box GetSweptBroadphaseBox(Box b) {
		Box broadphasebox = new Box(0.0f, 0.0f, 0.0f, 0.0f);

		broadphasebox.x = b.vx > 0 ? b.x : b.x + b.vx;
		broadphasebox.y = b.vy > 0 ? b.y : b.y + b.vy;
		broadphasebox.w = b.vx > 0 ? b.vx + b.w : b.w - b.vx;
		broadphasebox.h = b.vy > 0 ? b.vy + b.h : b.h - b.vy;

		return broadphasebox;
	}

	public static float SweptAABB(Box b1, Box b2) {
		float xInvEntry, yInvEntry;
		float xInvExit, yInvExit;

		// find the distance between the objects on the near and far sides for
		// both x and y
		if (b1.vx > 0.0f) {
			xInvEntry = b2.x - (b1.x + b1.w);
			xInvExit = (b2.x + b2.w) - b1.x;
		} else {
			xInvEntry = (b2.x + b2.w) - b1.x;
			xInvExit = b2.x - (b1.x + b1.w);
		}

		if (b1.vy > 0.0f) {
			yInvEntry = b2.y - (b1.y + b1.h);
			yInvExit = (b2.y + b2.h) - b1.y;
		} else {
			yInvEntry = (b2.y + b2.h) - b1.y;
			yInvExit = b2.y - (b1.y + b1.h);
		}

		// find time of collision and time of leaving for each axis (if
		// statement is to prevent divide by zero)
		float xEntry, yEntry;
		float xExit, yExit;

		if (b1.vx == 0.0f) {
			xEntry = -Float.POSITIVE_INFINITY;
			xExit = Float.POSITIVE_INFINITY;
		} else {
			xEntry = xInvEntry / b1.vx;
			xExit = xInvExit / b1.vx;
		}

		if (b1.vy == 0.0f) {
			yEntry = -Float.POSITIVE_INFINITY;
			yExit = Float.POSITIVE_INFINITY;
		} else {
			yEntry = yInvEntry / b1.vy;
			yExit = yInvExit / b1.vy;
		}

		// find the earliest/latest times of collision
		float entryTime = Math.max(xEntry, yEntry);
		float exitTime = Math.min(xExit, yExit);

		// if there was no collision
		if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f
				|| xEntry > 1.0f || yEntry > 1.0f) {
			normalx = 0.0f;
			normaly = 0.0f;
			return 1.0f;
		} else // if there was a collision
		{
			// calculate normal of collided surface
			if (xEntry > yEntry) {
				if (xInvEntry < 0.0f) {
					normalx = 1.0f;
					normaly = 0.0f;
				} else {
					normalx = -1.0f;
					normaly = 0.0f;
				}
			} else {
				if (yInvEntry < 0.0f) {
					normalx = 0.0f;
					normaly = 1.0f;
				} else {
					normalx = 0.0f;
					normaly = -1.0f;
				}
			}

			// return the time of collision
			return entryTime;
		}
	}

	static CollisionInfor SweptAABBCheck(Box box1, Box box2) {
		Box b1 = new Box(box1);
		Box b2 = new Box(box2);

		b1.x = b1.x - b1.w / 2;
		b1.y = b1.y - b1.h / 2;
		b2.x = b2.x - b2.w / 2;
		b2.y = b2.y - b2.h / 2;

		CollisionInfor ci = new CollisionInfor();
		// box is the moving box
		// block is the static box
		Box broadphasebox = GetSweptBroadphaseBox(b1);
		if (AABBCheck(broadphasebox, b2)) {

			float collisiontime = SweptAABB(b1, b2);
			if (collisiontime < 1.0f) {
				if (normalx == -1)
					ci.Direction = 2;
				if (normalx == 1)
					ci.Direction = 1;
				if (normaly == -1)
					ci.Direction = 4;
				if (normaly == 1)
					ci.Direction = 3;
				ci.CollisionTime = collisiontime;
				return ci;
			}
		} else if (AABBCheck(box1, box2)) {
			ci.CollisionTime = 0;
			ci.Direction = 0;
			return ci;
		}
		ci.Direction = -1;
		ci.CollisionTime = 1;
		return ci;
	}

	public static void initSocket() {

		final int PORT = 2222;
		String HOST = "localhost";

		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("ipconfig.ini"));
			while ((sCurrentLine = br.readLine()) != null) {
				HOST = sCurrentLine;
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
		
		System.out
				.println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
						+ "Now using host = " + HOST + ", portNumber=" + PORT);

		try {
			clientSocket = new Socket(HOST, PORT);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			os = new PrintStream(clientSocket.getOutputStream());
			is = new DataInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + HOST);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to the host "
							+ HOST);
		}

		if (clientSocket != null && os != null && is != null) {
			/* Create a thread to read from the server. */
			new Thread(new ClientListener()).start();
		}
	}

	// send data to server
	public static void sendData(String x) {
		Globals.os.println(x);
	}

	public static void closeConnection() throws IOException,
			InterruptedException {
		Globals.sendData(String.valueOf(Message.QUIT_GAME.value()));
		Globals.sendData(String.valueOf(Message.CANCEL_HOST.value()));
		os.close();
		is.close();
		clientSocket.close();
	}
}