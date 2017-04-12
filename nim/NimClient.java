/**
 *	NimClient.java
 *
 * 	This program implements a simple multithreaded chat client and uses it to
 * 	play the game of Nim.
 * 	It connects to the server (assumed to be localhost on port 7654) and starts
 * 	two threads: one for listening for data sent from the server, and another
 * 	that waits for the user to type something in that will be sent to the server.
 * 	Anything sent to the server is broadcast to all clients.
 *
 * 	The NimClient uses a NimClientListener whose code is in a seperate file.
 * 	The NimClientListener runs in a seperate thread, receives messages from the
 * 	server, and displays them on the screen.
 *
 * 	@author: Michael Fahy
 */


import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class NimClient
{
	public static void main(String[] args)
	{
		try
		{
			String hostname = "localhost";
			int port = 7654;

			System.out.println("Connecting to server on port " + port + "...");
			Socket connectionSock = new Socket(hostname, port);

			DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());

			System.out.println("Connection successful.");

			// START A THREAD TO LISTEN AND DISPLAY DATA SENT BY THE SERVER
			NimClientListener listener = new NimClientListener(connectionSock);
			Thread theThread = new Thread(listener);
			theThread.start();

			// READ INPUT FROM THE KEYBOARD AND SEND IT TO EVERYONE ELSE
			Scanner keyboard = new Scanner(System.in);
			while (true)
			{
				String data = keyboard.nextLine();
				serverOutput.writeBytes(data + "\n");
			}
		}

		catch (IOException e)
		{
			// ACTS AS QUIT FUNCTION SINCE IT EXITS AFTER ANY EXCESS INPUT IS ADDED
		}
	}
}
