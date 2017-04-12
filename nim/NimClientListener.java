/**
 * 	NimClientListener.java
 *
 * 	This class runs on the client end and just
 * 	displays the game board after each move.
 *
 *  Most of the actual process goes on within this program, data is taken from
 *  the server and is then used to change the boards of both players.
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

public class NimClientListener implements Runnable
{
	private Socket connectionSock = null;

	NimClientListener(Socket sock)
	{
		this.connectionSock = sock;
	}

	public void run()
	{
		try
		{
			NimGame gameObject = new NimGame();
			System.out.println(gameObject.getBoard());
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));

			// MAIN GAME
			while (true)
			{
				// GET DATA SENT FROM THE SERVER
				String serverText = serverInput.readLine();
				if (serverInput != null)
				{
					// ONCE PLAYER MAKES A MOVE, THE UPDATED GAME BOARD IS PRINTED TO BOTH CLIENTS
					gameObject.makeMove(serverText);
					System.out.println("\n");
					System.out.println(gameObject.getBoard());
					if (gameObject.winCheck())
					{
						// WHEN GAME BOARD IS EMPTY, THE PLAYER WHO MADE THE LAST MOVE IS DECLARED THE LOSER
						char loser = serverText.charAt(4);
						char winner;

						if (loser == '1')
						{
						 	winner = '2';
						}

						else
						{
							winner = '1';
						}

						System.out.println("The game has ended!\nPlayer " + winner + " is the winner.");
						System.out.println("Press enter to exit.");
						connectionSock.close(); // CLOSES CONNECTION SOCKETS AND EXITS TO END GAME
						break;
					}
				}

				else
				{
					System.out.println("Closing connection for socket " + connectionSock);
					connectionSock.close();
					break;
				}
			}
		}
		
		catch (Exception e)
		{
			//System.out.println("Error: " + e.toString());
			System.out.println("ERROR: Invalid input.");
		}
	}
}
