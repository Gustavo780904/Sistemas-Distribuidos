package teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servidor extends Thread {
    
	private static final String AT = "@";
	private static final String CONECTADOS = "Conectados: ";
	private static final String SAIU = " saiu";
	private static final String ESCREVEU = " escreveu: ";
	
	private static Map<String, PrintStream> MAP_CLIENTES = new HashMap<>();
	private static List<String> LISTA_DE_NOMES = new ArrayList<>();
	
	private Socket conexao;
    private String nomeCliente;
    
    public Servidor(Socket socket) {
        this.conexao = socket;
    }
    
	public static void main(String args[]) {
        try (var server = new ServerSocket(5555)) {
            System.out.println("ServidorSocket rodando na porta 5555");
            
            while (true) {
                var conexao = server.accept();
                var t = new Servidor(conexao);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
    
	@Override
	public void run() {
        try {
			var entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            nomeCliente = entrada.readLine();
            
            if (nomeCliente == null)
                return;
            
            System.out.println(">>> 1: " + nomeCliente);
            var saida = new PrintStream(conexao.getOutputStream());
            
            if (!armazena(this.nomeCliente)) {
                saida.println("Este nome ja existe! Conecte novamente com outro Nome.");
                saida.close();
                System.out.println(">>> EXISTE: " + nomeCliente);
                
                return;
            } else {
            	//adiciona os dados de saida do cliente no objeto MAP_CLIENTES
                //A chave ser? o nome e valor o printstream
                MAP_CLIENTES.put(nomeCliente, saida);
            	
                //mostra o nome do cliente conectado ao servidor
                System.out.println(nomeCliente + " : Conectado ao Servidor!");
                //Quando o cliente se conectar recebe todos que est?o conectados
                send(saida, CONECTADOS, LISTA_DE_NOMES.toArray(new String[LISTA_DE_NOMES.size()]));
            }
            
            var msg = entrada.readLine();
            System.out.println("\n-- " + msg);
            var parts = msg.split(AT);
            System.out.println("\t- " + Arrays.asList(parts));
            
            while (parts != null && !(parts[0].trim().equals(""))) {
                send(saida, ESCREVEU, parts);
                parts = entrada.readLine().split(AT);
                System.out.println("\t-- " + Arrays.asList(parts));
            }
            
            System.out.println(nomeCliente + " saiu do bate-papo!");
            String[] out = {" do bate-papo!"};
            send(saida, SAIU, out);
            remove(nomeCliente);
            MAP_CLIENTES.remove(nomeCliente);
            conexao.close();
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }
	
	private boolean armazena(String newName) {
        for (int i = 0; i < LISTA_DE_NOMES.size(); i++) {
            if (LISTA_DE_NOMES.get(i).equals(newName))
                return false;
        }
        
        LISTA_DE_NOMES.add(newName);
        
        return true;
    }
	
    private void remove(String oldName) {
        for (int i = 0; i < LISTA_DE_NOMES.size(); i++) {
            if (LISTA_DE_NOMES.get(i).equals(oldName))
                LISTA_DE_NOMES.remove(oldName);
        }
    }
	
    /**
     * Se o array da msg tiver tamanho igual a 1, ent?o envia para todos
     * Se o tamanho for 2, envia apenas para o cliente escolhido
     */
    private void send(PrintStream saida, String acao, String[] msg) {
    	System.out.println("\t>> " + acao);
        for (var cliente : MAP_CLIENTES.entrySet()) {
        	System.out.println("\t\t> " + cliente.getKey());
            var chat = (PrintStream) cliente.getValue();
            
			if (acao.equals(CONECTADOS)) {
				chat.println(CONECTADOS + Arrays.asList(msg));
			} else if (chat != saida) {
				System.out.println("\t\t> " + Arrays.asList(msg));
				if (msg.length == 1) {
					chat.println(nomeCliente + acao + msg[0]);
				} else {
					if (msg[1].equalsIgnoreCase((String) cliente.getKey())) {
						chat.println(nomeCliente + acao + msg[0]);
						break;
					}
				}
			}
        }
    }
}