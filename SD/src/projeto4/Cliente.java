package projeto4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread {
	private static boolean done = false;
	private Socket conexao;

	public Cliente(Socket s) {
		conexao = s;
	}

	public void run() {

		try {

			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			String linha;
			while (true) {
				linha = entrada.readLine();
				if (linha.trim().equals("")) {
					System.out.println("Conexao encerrada!!!");
					break;
				}
				System.out.println();
				System.out.println(linha);
				System.out.print("...> ");
			}
			done = true;
		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);

		}

	}

	public static void main(String[] args) {
			
		try {
			Socket conexao = new Socket("localhost", 2000);
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Entre com o seu nome: ");
			String nome = teclado.readLine();
			saida.println(nome);
			Thread t = new Cliente(conexao);
			t.start();
			String linha;
			while (true) {
				if (done) {
					break;
				}
				System.out.println("> ");
				linha = teclado.readLine();
				saida.println(linha);
			}
		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
		}

	}
}
