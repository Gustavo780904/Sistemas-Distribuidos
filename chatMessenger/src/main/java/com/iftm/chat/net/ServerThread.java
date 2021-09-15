package com.iftm.chat.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
	
	private Socket socket;
    private String clientName;
    
    private static final Logger logger = new Logger("chat-server");
    
    public ServerThread(Socket socket) {
        this.socket = socket;
    }
    
	@Override
	public void run() {
        try {
        	var port = socket.getPort();
        	var ip = socket.getInetAddress().getHostAddress();
        	var machine = socket.getInetAddress().getHostName();
			
        	var input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientName = input.readLine();
            
            if (clientName == null)
                return;
            
            var output = new PrintStream(socket.getOutputStream());
            
            if (add(clientName, output)) {
            	//mostra o nome do cliente conectado ao servidor
                System.out.println(clientName + " : Conectado ao Servidor!");
                //Quando o cliente se conectar recebe todos que estão conectados
                send(output, CONECTADOS, LISTA_DE_NOMES.toArray(new String[LISTA_DE_NOMES.size()]));
            } else {
                output.println("Este nome (" + clientName + ") ja existe! Conecte novamente com outro nome.");
                output.close();
                
                return;
            }
            
            var msg = input.readLine();
            var parts = msg.split(AT);
            
            while (parts != null && !(parts[0].trim().equals(""))) {
                send(output, ESCREVEU, parts);
                logger.log(machine, ip, port, parts[0]);
                parts = input.readLine().split(AT);
            }
            
            System.out.println(clientName + " saiu do bate-papo!");
            String[] out = { " do bate-papo!" };
            send(output, SAIU, out);
            remove(clientName);
            send(output, CONECTADOS, LISTA_DE_NOMES.toArray(new String[LISTA_DE_NOMES.size()]));
            socket.close();
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }
	
	private boolean add(String newName, PrintStream saida) {
        if (LISTA_DE_NOMES.contains(newName))
            return false;
        
        LISTA_DE_NOMES.add(newName);
        MAP_CLIENTES.put(clientName, saida);
        
        return true;
    }
	
    private void remove(String oldName) {
        if (LISTA_DE_NOMES.remove(oldName))
        	MAP_CLIENTES.remove(clientName);
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
					chat.println(clientName + acao + msg[0]);
				} else {
					if (msg[1].equalsIgnoreCase((String) cliente.getKey())) {
						chat.println(clientName + acao + msg[0]);
						break;
					}
				}
			}
        }
    }
}