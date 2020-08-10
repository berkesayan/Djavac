import java.io.*;
import java.net.*;

public class CheckAvailableClientSocket {
	public static boolean execute(String ip) throws Exception {
		Socket socket = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		socket = new Socket(ip, 9876);
		oos = new ObjectOutputStream(socket.getOutputStream());

		oos.writeObject("isAvailable?");

		ois = new ObjectInputStream(socket.getInputStream());
		String message = (String) ois.readObject();

		boolean ret = false;

		if (message.equalsIgnoreCase("true"))
			ret = true;

		oos.writeObject("exit");

		ois.close();
		oos.close();

		return ret;
	}

	public static void main(String[] args) throws Exception {
		execute("127.0.0.1");
	}
}