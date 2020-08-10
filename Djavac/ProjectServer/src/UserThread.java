
import java.io.*;
import java.net.*;
import java.util.*;

public class UserThread extends Thread {
	private Socket socket;
	public ChatServer server;
	private ObjectOutputStream writer;

	public UserThread(Socket socket, ChatServer server) {
		this.socket = socket;
		this.server = server;
	}

	public void run() {
		try {
			InputStream input = socket.getInputStream();
			ObjectInputStream objectInputStream = null;
			try {
				objectInputStream = new ObjectInputStream(input);
			} catch (Exception exxx) {
				// TODO Auto-generated catch block
				exxx.printStackTrace();
			}

			OutputStream output = socket.getOutputStream();
			writer = new ObjectOutputStream(output);

			List<UserClass> clientMessage;

			do {
				clientMessage = (List<UserClass>) objectInputStream.readObject();
				server.broadcast(clientMessage, this);

			} while (clientMessage.toString() != "bye");

			socket.close();
		} catch (IOException ex) {
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the client.
	 * 
	 * @throws IOException
	 */
	void sendMessage(List<UserClass> message) throws IOException {
		writer.writeObject(message);
	}
}