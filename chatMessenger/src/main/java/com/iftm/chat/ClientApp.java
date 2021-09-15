package com.iftm.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.iftm.chat.net.ClientThread;
import com.iftm.chat.ui.ChatWindow;

public class ClientApp {

	//se false, funciona no console 
	private static boolean showUI = true;
	private static boolean connected = false;
	private static String name;
	
	public static void main(String args[]) {
		if (showUI) {
			startUI();
		} else {
			try {
				// instancia a thread para ip e porta conectados e depois a inicia
				var thread = new ClientThread();
				
				var teclado = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Digite seu nome: ");
				var name = teclado.readLine();
				// envia o nome digitado para o servidor
				thread.send(name.toUpperCase());
				thread.start();
				
				// Variavel msg recebe a mensagem a ser enviada ao servidor
				String msg;
				
				while (true) {
					// cria linha para digitação da mensagem e a armazena na variavel msg
					System.out.print("Mensagem > ");
					msg = teclado.readLine();
					// envia a mensagem para o servidor
					thread.send(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void startUI() {
		try {
			// instancia a thread para ip e porta conectados e depois a inicia
			var thread = new ClientThread();
			
			thread.addPropertyChangeListener("clients", e -> {
				if (!connected && thread.getClients().contains(name)) {
					connected = true;
					
					SwingUtilities.invokeLater(() -> {
						var window = new ChatWindow(name, thread);
						window.setLocationRelativeTo(null);
						window.setVisible(true);
					});
				}
			});
			thread.addPropertyChangeListener("lastMessage", e -> {
				var msg = (String) e.getNewValue();
				
				if (!connected && msg.startsWith("Este nome (" + name + ") ja existe!")) {
					JOptionPane.showMessageDialog(null, "Este nome de usuário já existe! Tente com outro nome.");
					System.exit(0);
				}
			});
			
			name = JOptionPane.showInputDialog("Informe o seu nome:").toUpperCase();
			
			thread.send(name);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
