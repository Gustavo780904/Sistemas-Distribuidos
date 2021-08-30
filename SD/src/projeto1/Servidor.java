package projeto1;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

public class Servidor {

	public static void main(String[] args) {

		ServerSocket s;
		try {
			s = new ServerSocket(2000);
			System.out.println("Esperando conex�o............................");
			Socket conexao = s.accept();
			System.out.println("Conex�o aceita, esperando dados..............");
			DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
			DataInputStream entrada = new DataInputStream(conexao.getInputStream());
			
			for (int i = 0; i < 100; i++) {
				int linha = entrada.readInt();
				System.out.println("recebi");
				saida.writeUTF("recebi seu dado: " + linha);
			}
		} catch (IOException ex) {

			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

}
