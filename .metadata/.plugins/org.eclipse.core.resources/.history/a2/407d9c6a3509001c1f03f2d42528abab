package projeto1;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

public class Cliente {

	public static void main(String[] args) {
		Socket s;
		try {
			s = new Socket("localhost", 2000);
			DataOutputStream saida = new DataOutputStream(s.getOutputStream());
			DataInputStream entrada = new DataInputStream(s.getInputStream());
			
			for (int i=0; i<100; i++) {
				saida.writeInt(i);
				System.out.println("Enviei: "+ i);
				String en = entrada.readUTF();
				System.out.println("Recebi: " + en);
			}
		} catch (IOException ex) {

			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);

		}



	}

}
