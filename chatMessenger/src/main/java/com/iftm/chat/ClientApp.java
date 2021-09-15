package com.iftm.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.iftm.chat.net.ClientThread;
import com.iftm.chat.ui.ChatWindow;

public class ClientApp {

	private static boolean showUI = true;
	
	public static void main(String args[]) {
		try {
			// instancia a thread para ip e porta conectados e depois a inicia
			var thread = new ClientThread();
			
			if (showUI) {
				var name = JOptionPane.showInputDialog("Informe o seu nome:");
				thread.send(name.toUpperCase());
				thread.start();
				
				SwingUtilities.invokeLater(() -> {
					var window = new ChatWindow(name, thread);
					window.setLocationRelativeTo(null);
					window.setVisible(true);
				});
			} else {
				var teclado = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Digite seu nome: ");
				var name = teclado.readLine();
				// envia o nome digitado para o servidor
				thread.send(name.toUpperCase());
				thread.start();
				
				// Cria a variavel msg responsavel por enviar a mensagem para o servidor
				String msg;
				
				while (true) {
					// cria linha para digita��o da mensagem e a armazena na variavel msg
					System.out.print("Mensagem > ");
					msg = teclado.readLine();
					// envia a mensagem para o servidor
					thread.send(msg);
				}
			}
		} catch (IOException e) {
			System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
		}
	}
}
