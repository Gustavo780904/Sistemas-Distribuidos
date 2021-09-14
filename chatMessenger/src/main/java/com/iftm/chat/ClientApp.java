package com.iftm.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.iftm.chat.net.ClientThread;

public class ClientApp {

	public static void main(String args[]) {
		try {
			// instancia a thread para ip e porta conectados e depois a inicia
			var thread = new ClientThread();
			
			var teclado = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Digite seu nome: ");
			var meuNome = teclado.readLine();
			// envia o nome digitado para o servidor
			thread.send(meuNome.toUpperCase());
			
			thread.start();
			
			// Cria a variavel msg responsavel por enviar a mensagem para o servidor
			String msg;
			
			while (true) {
				// cria linha para digitação da mensagem e a armazena na variavel msg
				System.out.print("Mensagem > ");
				msg = teclado.readLine();
				// envia a mensagem para o servidor
				thread.send(msg);
			}
		} catch (IOException e) {
			System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
		}
	}
}
