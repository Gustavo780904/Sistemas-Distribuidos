package com.iftm.chat.net;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClientThread extends Thread {
    
	private List<String> clients = Collections.emptyList();
	private String lastMessage;
	
	/** Parte que controla a recep??o de mensagens do cliente */
    private Socket socket;
    private PrintStream output;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public ClientThread() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", 5555);
		output = new PrintStream(socket.getOutputStream());
    }

    /** Execu??o da thread */
    @Override
	public void run() {
		try {
            // recebe mensagens de outro cliente atrav?s do servidor
            var input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
            // cria variavel de mensagem
			String msg = null;
			
			while (true) {
				// pega o que o servidor enviou
				msg = input.readLine();
				
				// se a mensagem contiver dados, passa pelo if,
				// caso contrario cai no break e encerra a conexao
				if (msg == null) {
					System.out.println("Conex?o encerrada!");
					System.exit(0);
				} else {
					if (msg.startsWith("Conectados: ["))
						setClients(msg);
					else
						setLastMessage(msg);
				}
				
				System.out.println();
				// imprime a mensagem recebida
				System.out.println(msg);
				// cria uma linha visual para resposta
				System.out.print("Responder > ");
			}
        } catch (IOException e) {
            // caso ocorra alguma exce??o de E/S, mostra qual foi.
            System.out.println("Ocorreu uma Falha... .. ." + " IOException: " + e);
        }
    }
    
	public void send(String message) {
    	output.println(message);
    }
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}
	
	public String getLastMessage() {
		return lastMessage;
	}
	
	private void setLastMessage(String lastMessage) {
		var oldValue = this.lastMessage;
		this.lastMessage = lastMessage;
		
		pcs.firePropertyChange("lastMessage", oldValue, lastMessage);
	}
	
	public List<String> getClients() {
		return new ArrayList<>(clients);
	}

	private void setClients(String msg) {
		var oldValue = clients;
		msg = msg.trim().replace("Conectados: [", "").replace("]", "");
		clients = Arrays.asList(msg.split(", "));
		
		// notifica que a propriedade foi alterada
		pcs.firePropertyChange("clients", oldValue, clients);
	}
}