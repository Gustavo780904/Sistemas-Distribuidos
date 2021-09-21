package projeto5;

import java.io.*;
import java.net.*;

public class Server {

	private static DatagramSocket serverSocket;
	private static DatagramPacket receiver;

	public static void main(String[] args) {

		try {
			serverSocket = new DatagramSocket(4545);
			System.out.println("Servidor aguardando mensagens.......");
			receiver = new DatagramPacket(new byte[512], 512);

			while (true) {
				var send = "";
				serverSocket.receive(receiver);
				System.out.println("Mensagem recebida ");
				for (int i = 0; i < receiver.getLength(); i++) {
					System.out.print((char) receiver.getData()[i]);
				}
				System.out.println();
				var reply = new DatagramPacket(receiver.getData(), receiver.getLength(), receiver.getAddress(),
						receiver.getPort());
				serverSocket.send(reply);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
