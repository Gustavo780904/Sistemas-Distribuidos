package com.iftm.chat.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientThread extends Thread {
    
	/** Parte que controla a recepção de mensagens do cliente */
    private Socket socket;
    private PrintStream output;
    
    public ClientThread() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", 5555);
		output = new PrintStream(socket.getOutputStream());
    }

    /** Execução da thread */
    @Override
	public void run() {
		try {
            // recebe mensagens de outro cliente através do servidor
            var input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
            // cria variavel de mensagem
			String msg = null;
			
			while (true) {
				// pega o que o servidor enviou
				msg = input.readLine();
				
				// se a mensagem contiver dados, passa pelo if,
				// caso contrario cai no break e encerra a conexao
				if (msg == null) {
					System.out.println("Conexão encerrada!");
					System.exit(0);
				}
				
				System.out.println();
				// imprime a mensagem recebida
				System.out.println(msg);
				// cria uma linha visual para resposta
				System.out.print("Responder > ");
			}
        } catch (IOException e) {
            // caso ocorra alguma exceção de E/S, mostra qual foi.
            System.out.println("Ocorreu uma Falha... .. ." + " IOException: " + e);
        }
    }
    
    public void send(String message) {
    	output.println(message);
    }
}