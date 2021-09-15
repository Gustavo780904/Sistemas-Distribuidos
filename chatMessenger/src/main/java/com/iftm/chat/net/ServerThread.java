package com.iftm.chat.net;

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


import com.iftm.chat.util.Logger;

public class ServerThread extends Thread {
    
	private static final String AT = "@";
	private static final String CONECTADOS = "Conectados: ";
	private static final String SAIU = " saiu";
	private static final String ESCREVEU = " escreveu: ";
	
	private static Map<String, PrintStream> MAP_CLIENTES = new HashMap<>();
	private static List<String> LISTA_DE_NOMES = new ArrayList<>();
	
	private Socket conexao;
    private String nomeCliente;
    
    private static final Logger logger = new Logger("chat-server");
    
    public ServerThread(Socket socket) {
        this.conexao = socket;
    }
    
	public static void main(String args[]) {
        try (var server = new ServerSocket(5555)) {
            System.out.println("Socket Servidor rodando na porta 5555");
            
            while (true) {
                var conexao = server.accept();
                var t = new ServerThread(conexao);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void run() {
        try {
        	var port = conexao.getPort();
        	var ip = conexao.getInetAddress().getHostAddress();
        	var machine = conexao.getInetAddress().getHostName();
			
        	var entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            nomeCliente = entrada.readLine();
            
            if (nomeCliente == null)
                return;
            
            var saida = new PrintStream(conexao.getOutputStream());
            
            if (add(nomeCliente, saida)) {
            	//mostra o nome do cliente conectado ao servidor
                System.out.println(nomeCliente + " : Conectado ao Servidor!");
                //Quando o cliente se conectar recebe todos que estão conectados
                send(saida, CONECTADOS, LISTA_DE_NOMES.toArray(new String[LISTA_DE_NOMES.size()]));
            } else {
                saida.println("Este nome (" + nomeCliente + ") ja existe! Conecte novamente com outro nome.");
                saida.close();
                System.out.println(">>>JÁ EXISTE: " + nomeCliente);
                
                return;
            }
            
            var msg = entrada.readLine();
            var parts = msg.split(AT);
            
            while (parts != null && !(parts[0].trim().equals(""))) {
                send(saida, ESCREVEU, parts);
                logger.log(machine, ip, port, parts[0]);
                parts = entrada.readLine().split(AT);
            }
            
            System.out.println(nomeCliente + " saiu do bate-papo!");
            String[] out = { " do bate-papo!" };
            send(saida, SAIU, out);
            remove(nomeCliente);
            send(saida, CONECTADOS, LISTA_DE_NOMES.toArray(new String[LISTA_DE_NOMES.size()]));
            conexao.close();
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }
	
	private boolean add(String newName, PrintStream saida) {
        if (LISTA_DE_NOMES.contains(newName))
            return false;
        
        LISTA_DE_NOMES.add(newName);
        MAP_CLIENTES.put(nomeCliente, saida);
        
        return true;
    }
	
    private void remove(String oldName) {
        if (LISTA_DE_NOMES.remove(oldName))
        	MAP_CLIENTES.remove(nomeCliente);
    }

    /**
     * Se o array da msg tiver tamanho igual a 1, então envia para todos
     * Se o tamanho for 2, envia apenas para o cliente escolhido
     */
    private void send(PrintStream saida, String acao, String[] msg) {
        for (var cliente : MAP_CLIENTES.entrySet()) {
            var chat = (PrintStream) cliente.getValue();
            
			if (acao.equals(CONECTADOS)) {
				chat.println(CONECTADOS + Arrays.asList(msg));
			} else if (chat != saida) {
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