package com.iftm.chat;

import java.io.IOException;
import java.net.ServerSocket;

import com.iftm.chat.net.ServerThread;

public class ServerApp {

	public static void main(String args[]) {
        try (var server = new ServerSocket(5555)) {
            System.out.println("Servidor de Chat rodando na porta 5555");
            
            while (true) {
                var socket = server.accept();
                var t = new ServerThread(socket);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
