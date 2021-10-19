package com.iftm.chat.ui;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.iftm.chat.net.ClientThread;

@SuppressWarnings("serial")
public class ChatWindow extends JFrame {
	
	private JTextArea chatArea;
	private JCheckBox allCheckBox;
	private JComboBox<String> clientsComboBox;
	private JTextField messageTextField;
	private JButton sendButton;
	
	private final String clientName;
	private final ClientThread clientThread;

	public ChatWindow (String name, ClientThread clientThread) {
		this.clientName = name.toUpperCase();
		this.clientThread = clientThread;
		
		setTitle("Chat: " + clientName);
		
		//e se o nome ja existir? Está conectando pra depois conectar, tem que criar 
		//um meio para não abrir a janela. 
		init();
		updateClients();
	}

	private void init() {
		// Area do chat:
		var chatLabel = new JLabel("Conversa:");
		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		clientThread.addPropertyChangeListener("lastMessage", e -> updateChat());
		
		// Envio / Lista de clientes
		var clientsLabel = new JLabel("Enviar Para:");
		allCheckBox = new JCheckBox("Todos", false);
		allCheckBox.addItemListener(e -> {
			clientsComboBox.setEnabled(!allCheckBox.isSelected());
		});
		clientsComboBox = new JComboBox<>();
		clientsComboBox.setEnabled(!allCheckBox.isSelected());
		clientThread.addPropertyChangeListener("clients", e -> updateClients());
		
		// Campo para escrever a mensagem
		var messageLabel = new JLabel("Digite:");
		messageTextField = new JTextField();
		messageTextField.addActionListener(e -> sendMessage());
		
		// Botao de envio
		sendButton = new JButton("Enviar");
		sendButton.setToolTipText("Clique para enviar a mensagem");
		sendButton.addActionListener(e -> sendMessage());
		
		// Layout dos components
		var layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.CENTER, true)
				.addComponent(chatLabel, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(chatArea, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addComponent(clientsLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
   						.addComponent(clientsComboBox, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
   						.addComponent(allCheckBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(messageLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(messageTextField, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				)
				.addComponent(sendButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(chatLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				.addComponent(chatArea, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(clientsLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(clientsComboBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(allCheckBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(messageLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(messageTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addComponent(sendButton, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
		);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				//teste para atualizar a lista ao desconectar
				updateClients();
			}
		});
		
		setPreferredSize(new Dimension(400, 600));
		pack();
		
		messageTextField.requestFocusInWindow();
	}

	private void updateChat() {
		chatArea.append("\n" + clientThread.getLastMessage());
	}
	
	//atualisa a lista de clientes na caixa de seleção
	private void updateClients() {
		clientsComboBox.removeAllItems();
		var clients = clientThread.getClients();
		
		for (var name : clients) {
			if (!name.equalsIgnoreCase(clientName))
				clientsComboBox.addItem(name.toUpperCase());
		}
	}

	private void sendMessage() {
		var msg = messageTextField.getText();
		
		if (msg.isBlank() || clientsComboBox.getItemCount() == 0)
			return;
		
		messageTextField.setText("");
		
		if (!allCheckBox.isSelected())
			msg += " " + "@" + clientsComboBox.getSelectedItem();
		
		chatArea.append("\n\t" + msg);
		
		var finalMsg = msg;
		
		// Pra nao enviar na thread do Swing
		new Thread(() -> {
			clientThread.send(finalMsg);
		}).start();
	}
	
	private void disconnect() {
		new Thread(() -> {
			clientThread.send("");
		}).start();
		//teste
		updateClients();
	};
}
