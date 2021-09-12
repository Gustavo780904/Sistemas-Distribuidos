import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread {
	private static Map clientes;
	private Socket conexao;
	private String nomeCliente;
	private static List<Cliente> listaClientes = new ArrayList<>();

	public Servidor(Socket s) {
		this.conexao = s;
	}

	public boolean registroClientes(String newName) {

		for (int i = 0; i < listaClientes.size(); i++) {
			if (listaClientes.get(i).equals(newName))
				return true;
		}
		listaClientes.add(newName);
		return false;
	}

	public void remove(String oldName) {
		for (int i = 0; i < listaClientes.size(); i++) {
			if (listaClientes.get(i).equals(oldName))
				listaClientes.remove(oldName);
		}
	}

	public void send(PrintStream saida, String acao, String[] msg) {
		out: for (Map.Entry<Cliente, PrintStream> cliente : cliente.entrySet()) {
			PrintStream chat = cliente.getValue();
			if (chat != saida) {
				if (msg.length == 1) {
					chat.println(this.nomeCliente + acao + msg[0]);
				} else {
					if (msg[1].equalsIgnoreCase(cliente.getKey())) {
						chat.println(this.nomeCliente + acao + msg[0]);
						break out;
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {

		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
			PrintStream saida = new PrintStream(this.conexao.getOutputStream());
			this.nomeCliente = entrada.readLine();

			if (registroClientes(this.nomeCliente)) {
				saida.println("Este nome de usuário já existe, conecte-se utilizando outro nome");

				this.conexao.close();
				return;
			} else {
				System.out.println(this.nomeCliente + " : Conectado ao Servidor!");
				saida.println("Conectados: " + listaClientes.toString());
			}

			if (this.nomeCliente == null) {
				return;
			}
			clientes.put(this.nomeCliente, saida);
			String[] msg = entrada.readLine().split(":");
			while ((msg != null) && (!msg[0].trim().equals(""))) {
				send(saida, " disse: ", msg);
				msg = entrada.readLine().split(":");
			}
			sendToAll(saida, " saiu ", " do Chat!");
			remove(this.nomeCliente);
			clientes.remove(saida);
			this.conexao.close();

		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	public void sendToAll(PrintStream saida, String acao, String linha) throws IOException {
		Enumeration<PrintStream> e = clientes.elements();

		while (e.hasMoreElements()) {
			PrintStream chat = (PrintStream) e.nextElement();
			if (chat != saida) {
				chat.println(nomeCliente + acao + linha);
			}
			if (acao == "saiu") {
				if (chat == saida)
					chat.println("");
			}
		}
	}

	public static void main(String[] args) {

		try {
			clientes = new HashMap<>();
			ServerSocket s = new ServerSocket(2000);
			while (true) {
				try {
					System.out.print("Esperando conectar...");
					Socket conexao = s.accept();
					System.out.println(" Conectou!");
					Thread t = new Servidor(conexao);
					t.start();
				} catch (IOException e) {
					Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		} catch (IOException e) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, e);

		}
	}
}
