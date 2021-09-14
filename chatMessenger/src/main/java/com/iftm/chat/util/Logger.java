package com.iftm.chat.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	private BufferedWriter writer;

	public Logger(String name) {
		try {
			writer = new BufferedWriter(new FileWriter(name + ".log"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void log(String machine, String ip, int port, String message) {
		try {
			writer.write(String.format("%s@%s@%d#%s\n", machine, ip, port, message));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
