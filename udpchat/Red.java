/**
*	Simple UDP Chat Client Program
*	Connects to a UDP Server
*	Establishes connection with one other user
*	Displays responses from the user until goodbye message is received.
*
*	@author: Tyler Andrews (main)
*	@author: Vince Carpino (partner)
*	@version: 2.0
*/

import java.io.*;
import java.net.*;
import java.util.Arrays;

class Red
{
    public static void main(String args[]) throws Exception
    {
        int state = 0;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");

        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        String receiveSentence;

        DatagramPacket receivePacket = null;
        DatagramPacket sendPacket = null;

        System.out.println("CONNECTING TO IP: " + IPAddress.toString());

        while (state < 3)
        {
            switch (state)
            {
                /* CONNECT USERS */
                case 0:
                {
                    String hello = "HELLO RED";
                    sendData = hello.getBytes();
                    System.out.println("SENDING MESSAGE TO SERVER: " + hello);
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
                    clientSocket.send(sendPacket);
                    System.out.println("Message sent - Waiting for response from server.");

                    // RECEIVE PACKET FROM SERVER WITH 100/200 MESSAGE
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    receiveSentence = new String(receivePacket.getData());

                    System.out.println("RESPONSE FROM SERVER: " + receiveSentence);

                    // THIS USER IS FIRST
                    if (receiveSentence.substring(0,3).equals("100"))
                    {
                        System.out.println("YOU ARE FIRST. Please wait for a second user to connect.");

                        // WAIT FOR SERVER TO INFORM YOU THAT SECOND USER JOINED
                        clientSocket.receive(receivePacket);
                        receiveSentence = new String(receivePacket.getData());

                        // SECOND USER HAS CONNECTED
                        if (receiveSentence.substring(0,3).equals("200"))
                        {
                            System.out.println("SECOND USER CONNECTED. Please start a conversation.");
                            state = 1;
                            break;
                        }

                        else
                        {
                            System.out.println("Error in message from server. Exiting application.");
                            break;
                        }
                    }

                    // THIS USER IS SECOND
                    else if (receiveSentence.substring(0,3).equals("200"))
                    {
                        System.out.println("YOU ARE SECOND. Please wait for a message from user 1.");
                        state = 2;
                        break;
                    }

                    else
                    {
                        System.out.println("CHAT SERVER FULL - EXITING APPLICATION");
                        System.exit(0);
                        break;
                    }
                }

                // THIS USER IS USER 1
                case 1:
                {
                    while (true)
                    {
                        // CLEAR CHAT BUFFERS
                        Arrays.fill(sendData, (byte) 0 );
                        Arrays.fill(receiveData, (byte) 0 );

                        System.out.print("ME: ");
                        // READ INPUT FROM USER, SEND IN PACKET
                        String sentence = inFromUser.readLine();
                        sendData = sentence.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
                        clientSocket.send(sendPacket);

                        // RECEIVE REPLY FROM OTHER USER, PRINT TO SCREEN
                        clientSocket.receive(receivePacket);
                        String modifiedSentence = new String(receivePacket.getData());

                        // IF MESSAGE IS 'GOODBYE', END PROGRAM
                        modifiedSentence = modifiedSentence.toUpperCase();
                        if (modifiedSentence.length() > 6 && modifiedSentence.substring(0,7).equals("GOODBYE"))
                        {
                            System.out.println("\nFROM SERVER: GOODBYE");
                            System.out.println("** EXITING APPLICATION **");
                            state = 3;
                            break;
                        }

                        System.out.println("BLUE: " + modifiedSentence);
                    }

                    break;
                }

                // THIS USER IS USER 2
                case 2:
                {
                    while (true)
                    {
                        // CLEAR CHAT BUFFERS
                        Arrays.fill(sendData, (byte) 0 );
                        Arrays.fill(receiveData, (byte) 0 );

                        // RECEIVE REPLY FROM OTHER USER, PRINT TO SCREEN
                        clientSocket.receive(receivePacket);
                        String modifiedSentence = new String(receivePacket.getData());

                        // IF MESSAGE IS 'GOODBYE', END PROGRAM
                        modifiedSentence = modifiedSentence.toUpperCase();
                        if (modifiedSentence.length() > 6 && modifiedSentence.substring(0,7).equals("GOODBYE"))
                        {
                            System.out.println("\nFROM SERVER: GOODBYE");
                            System.out.println("** EXITING APPLICATION **");
                            state = 3;
                            break;
                        }

                        System.out.println("BLUE: " + modifiedSentence);

                        System.out.print("ME: ");
                        // READ INPUT FROM USER, SEND IN PACKET
                        String sentence = inFromUser.readLine();
                        sendData = sentence.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
                        clientSocket.send(sendPacket);
                    }

                    break;
                }
            }
        }

        clientSocket.close();
    }
}
