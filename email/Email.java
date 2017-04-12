/**
*	SMTP Email Assignment - PA04
*	Connects to an SMTP Server
*	Receives mail information from user
*	Connects to smtp.chapman.edu server on port 25 and sends email
*
*	@author: Vince Carpino (based on original code by Michael Fahy; received help from Tyler Andrews)
*/

import java.io.*;
import java.net.*;

class Email
{
    public static void main(String argv[]) throws Exception
    {
        String sentence;
        String modifiedSentence;

        String fromAddress, toAddress, subject, line = "";

        // STRING ARRAY FOR 50 LINES OF EMAIL
        String[] message = new String[50];
        int numLines = 0;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = null;

        // OBTAIN VALID 'FROM' ADDRESS
        while (true)
        {
            System.out.print("FROM: ");
            fromAddress = inFromUser.readLine().toString();

            // IF INPUT IS ALL WHITESPACE, INVALID
            if (fromAddress.replaceAll("\\s", "").equals(""))
            {
                System.out.println("\nERROR: CANNOT BE LEFT BLANK");
            }

            else
            {
                break;
            }
        }

        // OBTAIN VALID 'TO' ADDRESS
        while (true)
        {
            System.out.print("TO: ");
            toAddress = inFromUser.readLine().toString();

            // IF INPUT IS ALL WHITESPACE, INVALID
            if (toAddress.replaceAll("\\s", "").equals(""))
            {
                System.out.println("\nERROR: CANNOT BE LEFT BLANK");
            }

            else
            {
                break;
            }
        }

        // OBTAIN VALID 'SUBJECT'
        while (true)
        {
            System.out.print("SUBJECT: ");
            subject = inFromUser.readLine().toString();

            // IF INPUT IS ALL WHITESPACE, INVALID
            if (subject.replaceAll("\\s", "").equals(""))
            {
                System.out.println("\nERROR: CANNOT BE LEFT BLANK");
            }

            else
            {
                break;
            }
        }

        System.out.println("\nBEGIN MESSAGE BODY. TO END MESSAGE, TYPE A '.' ON A LINE BY ITSELF.\nMESSAGE:");
        // OBTAIN 'MESSAGE'
        while (true)
        {
            line = inFromUser.readLine().toString();

            // IF LINE IS ONLY '.', END OF MESSAGE
            if (line.length() == 1 && line.substring(0,1).equals("."))
            {
                System.out.println("<-- END OF MESSAGE -->");
                break;
            }

            // APPEND LINE TO ARRAY
            message[numLines] = line;
            numLines++;

            // IF MAX NUMBER OF LINES REACHED, INFORM USER AND STOP INPUT
            if (numLines == 50)
            {
                System.out.println("\n** MAX NUMBER OF LINES REACHED  -  MESSAGE SAVED **\n");
                break;
            }
        }

        try
        {
            clientSocket = new Socket("smtp.chapman.edu", 25);
        }

        catch(Exception e)
        {
            System.out.println("Failed to open socket connection");
            System.exit(0);
        }

        PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // TALK WITH SERVER TO SEND EMAIL
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);

        outToServer.println("HELO chapman.edu");
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);

        outToServer.println("MAIL FROM: " + fromAddress);
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);

        outToServer.println("RCPT TO: " + toAddress);
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);

        outToServer.println("DATA");
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);

        outToServer.println("From: " + fromAddress);
        outToServer.println("To: " + toAddress);
        outToServer.println("Subject: " + subject);

        for (int i = 0; i < numLines; ++i)
        {
            outToServer.println(message[i]);
        }

        outToServer.println(".");

        modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);

        outToServer.println("QUIT");

        clientSocket.close();
    }
}
