import java.io.*;
import java.net.*;

public class CheckAvailableServerSocket {
	private static ServerSocket server;
	private static int port = 9876;

	public static void execute() throws IOException, ClassNotFoundException {
		server = new ServerSocket(port);
		while (true) {
			Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String message = (String) ois.readObject();
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			if (!RMIServer.isWorking)
				oos.writeObject("true");
			else
				oos.writeObject("false");

			System.out.println(RMIServer.isWorking);

			ois.close();
			oos.close();
			socket.close();
		}
	}
}