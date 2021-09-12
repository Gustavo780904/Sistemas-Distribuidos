import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Cliente extends Thread {

	private Socket conexao;

	public Cliente(Socket s) {
		this.conexao = s;
	}

	

	public static void main(String[] args) {
			
		try {
			Socket conexao = new Socket("localhost", 2000);
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Entre com o seu nome: ");
			String meuNome = teclado.readLine();
			saida.println(meuNome.toUpperCase());
			Thread t = new Cliente(conexao);
			t.start();
			String msg;
			while (true) {
				
				System.out.println("> ");
				msg = teclado.readLine();
				saida.println(msg);
			}
		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
		}

	}
	public void run() {

		try {

			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			String msg;
			while (true) {
				msg = entrada.readLine();
				if (msg == null) {
					System.out.println("Conexao encerrada!!!");
					System.exit(0);
				}
				System.out.println();
				System.out.println(msg);
				System.out.print("......>>> ");
			}
		
		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);

		}

	}
}
