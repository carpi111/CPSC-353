/**
*	UDP Server Program
*	Listens on a UDP port
*	Receives a line of input from a UDP client
*	Returns an upper case version of the line to the client
*
*	@author: Vince Carpino (main)
*	@author: Tyler Andrews (partner)
*	@version: 2.0
*/

import java.io.*;
import java.net.*;
import java.util.Arrays;

class ChatServer
{
    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = null;
        DatagramPacket sendPacket = null;

        InetAddress IPAddress1 = null, IPAddress2 = null;

        int port1 = 0, port2 = 0;
        int state = 0;

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        byte[] messageBytes = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        String sentence, capitalizedSentence, message, response, user1 = "", user2 = "";

	    try
		{
			serverSocket = new DatagramSocket(8888);
		}

    	catch(Exception e)
		{
			System.out.println("Failed to open UDP socket");
			System.exit(0);
		}

        while (true)
        {
            switch (state)
            {
                /* CONNECT USER 1 */
                case 0:
                    serverSocket.receive(receivePacket);
                    sentence = new String(receivePacket.getData());
                    IPAddress1 = receivePacket.getAddress();
                    port1 = receivePacket.getPort();
                    capitalizedSentence = sentence.toUpperCase();
                    System.out.println(sentence);

                    if (capitalizedSentence.substring(0,5).equals("HELLO"))
                    {
                        message = "100";
                        sendData = message.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                        serverSocket.send(sendPacket);

                        if (capitalizedSentence.length() > 8)
                        {
                            user1 = capitalizedSentence.substring(6,9);
                        }

                        state = 1;
                    }

                    break;

                /* CONNECT USER 2 */
                case 1:
                    serverSocket.receive(receivePacket);
                    sentence = new String(receivePacket.getData());
                    IPAddress2 = receivePacket.getAddress();
                    port2 = receivePacket.getPort();
                    capitalizedSentence = sentence.toUpperCase();
                    System.out.println(sentence);

                    if (capitalizedSentence.substring(0,5).equals("HELLO"))
                    {
                        message = "200";
                        sendData = message.getBytes();

                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                        serverSocket.send(sendPacket);

                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                        serverSocket.send(sendPacket);

                        if (capitalizedSentence.length() > 8)
                        {
                            user2 = capitalizedSentence.substring(6,9);
                        }

                        state = 2;
                    }

                    break;

                /* CHAT MODE */
                case 2:
                    while (true)
                    {
                        // RESET CHAT BUFFERS
                        Arrays.fill(sendData, (byte) 0 );
                        Arrays.fill(receiveData, (byte) 0 );

                        // RECEIVE FROM ONE, PRINT, SEND TO TWO
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        sentence = new String(receivePacket.getData());
                        System.out.println(user1 + ": " + sentence);

                        capitalizedSentence = sentence.toUpperCase();
                        if (capitalizedSentence.substring(0,7).equals("GOODBYE"))
                        {
                            System.out.println("\n** QUITTING **");

                            // SEND 'GOODBYE' TO BOTH USERS
                            message = "GOODBYE";
                            sendData = message.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                            serverSocket.send(sendPacket);
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                            serverSocket.send(sendPacket);

                            state++;
                            System.exit(0);
                            break;
                        }

                        sendData = sentence.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                        serverSocket.send(sendPacket);

                        // RESET CHAT BUFFERS
                        Arrays.fill(sendData, (byte) 0 );
                        Arrays.fill(receiveData, (byte) 0 );

                        // RECEIVE FROM TWO, PRINT, SEND TO ONE
                        receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        sentence = new String(receivePacket.getData());
                        System.out.println(user2 + ": " + sentence);

                        capitalizedSentence = sentence.toUpperCase();
                        if (capitalizedSentence.substring(0,7).equals("GOODBYE"))
                        {
                            System.out.println("\n** QUITTING **");

                            // SEND 'GOODBYE' TO BOTH USERS
                            message = "GOODBYE";
                            sendData = message.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                            serverSocket.send(sendPacket);
                            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                            serverSocket.send(sendPacket);

                            state++;
                            System.exit(0);
                            break;
                        }

                        sendData = sentence.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                        serverSocket.send(sendPacket);
                    }

                    break;
            }
        }
    }
}
