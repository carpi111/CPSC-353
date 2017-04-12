/**
 * 	NimServer.java
 *
 * 	This program implements a simple multithreaded chat server and uses it to
 * 	play the game of Nim.
 *
 * 	The NimServer uses a NimClientHandler whose code is in a separate file.
 * 	When a client connects, the NimServer starts a NimClientHandler in a
 * 	seperate thread to receive moves from the client.
 *
 *	@author: Michael Fahy
 */


import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class NimServer
{
	// MAINTAIN LIST OF ALL CLIENT SOCKETS FOR BROADCAST
	private ArrayList<Socket> socketList;

	public NimServer()
	{
		socketList = new ArrayList<Socket>();
	}

	private void getConnection()
	{
		// WAIT FOR A CONNECTION FROM THE CLIENT
		try
		{
			System.out.println("Waiting for client connections on port 7654...");
			ServerSocket serverSock = new ServerSocket(7654);

			while (socketList.size() < 2) // ONLY ACCEPT TWO CLIENTS
			{
				Socket connectionSock = serverSock.accept();
				// ADD THIS SOCKET TO THE LIST
				socketList.add(connectionSock);
				// SEND TO CLIENTHANDLER THE SOCKET AND ARRAYLIST OF ALL SOCKETS
				NimClientHandler handler = new NimClientHandler(connectionSock, this.socketList);
				Thread theThread = new Thread(handler);
				theThread.start();
			}

			serverSock.close(); // REFUSE ALL OTHER CONNECTIONS
		}
		
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		NimServer server = new NimServer();
		server.getConnection();
	}
}
