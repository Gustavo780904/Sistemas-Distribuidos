package projeto5;

import java.io.*;
import java.net.*;

public class Client {

	private static DatagramSocket clientSocket;
	private static InetAddress receiver;
	private static BufferedReader keyboard;

	public static void main(String[] args) throws UnknownHostException, SocketException {

		try {

			clientSocket = new DatagramSocket();
			receiver = InetAddress.getByName("localhost");
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			System.out.print(">> ");
			var send = keyboard.readLine();

			while (!send.equalsIgnoreCase("")) {
				
				byte[] buffer = send.getBytes();
				var msg = new DatagramPacket(buffer, buffer.length, receiver, 4545);
				clientSocket.send(msg);
				var reply = new DatagramPacket(new byte[512], 512);
				clientSocket.receive(reply);
				
				for (int i = 0; i < reply.getLength(); i++) {
					System.out.print((char) reply.getData()[i]);
				}
				System.out.println();
				System.out.print(">> ");
				send = keyboard.readLine();
			}
			clientSocket.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
