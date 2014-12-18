package MutilSocket;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import maingame.Globals;

public class ClientListener implements Runnable {

	public void run() {
		String responseLine;
		try {
			while ((responseLine = Globals.is.readLine()) != null) {
				Globals.ServerMessage.add(responseLine);
			
			}
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
		
	}
}