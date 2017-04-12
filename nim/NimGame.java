/**
 * 	NimGame.java
 *
 *	This class will contain the rules and logic for the game.
 *	It will be responsible for printing the board and keeping track
 *	of the sticks in each row.
 *
 * 	@author: Vince Carpino, John Ligon, Gabby Llanillo
 */


import java.util.Arrays;

public class NimGame
{
	public int[] gameBoard = {1, 3, 5, 7};
	public int pile, amount = 0;

	NimGame()
	{
		System.out.println("\nWelcome to Nim!\n");
		System.out.println("There are 4 rows of matches.");
		System.out.println("You can remove any number of matches but only from one row during each move.");
		System.out.println("Enter two numbers in this format '# #'.");
		System.out.println("The first number specifies which row you are removing from.");
		System.out.println("The second number specifies how many you are removing.");
		System.out.println("The player who takes the last match loses.\n");
	}

	public String getBoard()
	{
		String board = "1: ";

		for (int i = 0; i < gameBoard[0]; ++i)
		{
			board += "|";
		}

		board += "\n2: ";

		for (int i = 0; i < gameBoard[1]; ++i)
		{
			board += "|";
		}

		board += "\n3: ";

		for (int i = 0; i < gameBoard[2]; ++i)
		{
			board += "|";
		}

		board += "\n4: ";

		for (int i = 0; i < gameBoard[3]; ++i)
		{
			board += "|";
		}

		board += "\n";

		return board;
	}

	public void makeMove(String command)
	{
		char pilechar = command.charAt(0); // FIRST NUM VALUE IN CLIENT INPUT DESIGNATES THE SELECTED ROW
		pile = Character.getNumericValue(pilechar);
		char amountchar = command.charAt(2); // SECOND NUM VALUE IN CLIENT INPUT DESIGNATES THE AMOUNT OF MATCHES TO BE TAKEN
		amount = Character.getNumericValue(amountchar);

		if (pile > 4)
		{
			System.out.println("That is not valid input resupply an answer.");

		}

		else
		{
			if (gameBoard[pile - 1] < amount)
			{
				System.out.println("Sorry you can't take that many, please supply a different answer.");

			}

			else
			{
				gameBoard[pile - 1] = gameBoard[pile - 1] - amount;
			}
		}
	}

	public boolean winCheck()
	{
		// CHECKS FOR AN EMPTY BOARD WHICH SIGNIFIES THE END OF A GAME
		int wincondition[] = {0, 0, 0, 0};

		if (Arrays.equals(wincondition,gameBoard))
		{
			return true;
		}

		else
		{
			return false;
		}
	}
}
