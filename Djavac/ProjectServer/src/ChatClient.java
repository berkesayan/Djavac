package src;

import ReadThread.ReadThread;

import java.net.*;
import java.io.*;

public class ChatClient {
	private String hostname;
	private int port;
	private String userName;
	public Socket socket;
	public ReadThread readThread;
	public WriteThread writeThread;
	private String dir;

	public ChatClient(String hostname, int port, String dir) {
		this.hostname = hostname;
		this.port = port;
		this.dir = dir;
	}

	public void execute() throws ClassNotFoundException, UnknownHostException, IOException {
		socket = new Socket(hostname, port);

		System.out.println("Connected to the chat server");

		writeThread = new WriteThread(socket, this);
		writeThread.start();
		readThread = new ReadThread(socket, this, this.dir);
		readThread.start();
	}

	void setUserName(String userName) {
		this.userName = userName;
	}

	String getUserName() {
		return this.userName;
	}
}
