/**
 * 	NimClientHandler.java
 *
 * 	This class handles communication between the client
 * 	and the server.  It runs in a seperate thread but has a
 * 	link to a common list of sockets to handle broadcast.
 *
 * 	@author: John Ligon, Gabby Llanillo, Vince Carpino
 */


import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class NimClientHandler implements Runnable
{
	private Socket connectionSock = null;
	private ArrayList<Socket> socketList;
	private int playernum;

	NimClientHandler(Socket sock, ArrayList<Socket> socketList)
	{
		playernum = socketList.size();
		this.connectionSock = sock;
		this.socketList = socketList;
	}

	public void run()
	{
		try
		{
			System.out.println("Connection made with socket " + connectionSock);
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			while (true)
			{
				// GET DATA SENT FROM A CLIENT
				String clientText = clientInput.readLine();
				if (clientText != null)
				{
					System.out.println("Received: " + clientText);

					// SEND TO EVERYONE
					for (Socket s : socketList)
					{
						DataOutputStream clientOutput = new DataOutputStream(s.getOutputStream());
						clientOutput.writeBytes(clientText + " " + playernum + "\n");
					}
				}

				else
				{
				  	System.out.println("Closing connection for socket " + connectionSock);
				   	socketList.remove(connectionSock);
				   	connectionSock.close();
				   	break;
				}
			}
		}

		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
			// REMOVE FROM ARRAYLIST
			socketList.remove(connectionSock);
		}
	}
}
