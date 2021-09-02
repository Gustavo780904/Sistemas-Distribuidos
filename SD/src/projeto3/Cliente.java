package projeto3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

	public static void main(String[] args) {
		Socket conexao;
		try {
			conexao = new Socket("127.0.0.1", 2001);
			DataInputStream entrada = new DataInputStream(conexao.getInputStream());
			DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
			String linha;
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			 
			while(true) {
				System.out.print("> ");
				linha = teclado.readLine();
				saida.writeUTF(linha);
				linha = entrada.readUTF();
				if (linha.equalsIgnoreCase("")) {
					System.out.println("Conexão encerrada!");
					break;
				};
				System.out.println(linha);
				
			}
		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
		}

	}

}
