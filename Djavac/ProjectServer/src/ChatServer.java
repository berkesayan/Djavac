
import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private int port;
	private Set<String> userNames = new HashSet<>();
	public Set<UserThread> userThreads = new HashSet<>();

	public ChatServer(int port) {
		this.port = port;
	}

	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			ServerGUI_Program.updateChatServerButtonColor(Color.GREEN);
			ServerGUI_Program.appendToPane("\n- Main Server is created and listening on port " + port, Color.GREEN);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("New user connected");

				UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				newUser.start();
				ServerGUI_Program.userthread = newUser;
				// sc.connectedusers.add(socket.getRemoteSocketAddress().toString());
				InetAddress inetAddress = InetAddress.getLocalHost();
				System.out.println("IP Address:- " + inetAddress.getHostAddress());
				System.out.println("Host Name:- " + inetAddress.getHostName());
				String ip = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/",
						"");

				ServerGUI_Program.connectedusers.add(new ConnectedRMIServer(ip, false, 0));
				ServerGUI_Program.appendToPane("\n- New User Connected to Chat Server", Color.YELLOW);
				ServerGUI_Program.appendToPane(
						"\n- New Connection from: " + socket.getRemoteSocketAddress().toString() + " IP address",
						Color.YELLOW);

			}
		} catch (Exception ex) {
			ServerGUI_Program.updateChatServerButtonColor(Color.RED);
			ServerGUI_Program.appendToPane("\n- Error Creating the Chat Server", Color.RED);
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	void broadcast(List<UserClass> cl, UserThread excludeUser) throws IOException {
		for (UserThread aUser : userThreads) {
			try {
				if (aUser != excludeUser) {
					aUser.sendMessage(cl);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void addUserName(String userName) {
		userNames.add(userName);
	}

	void removeUser(String userName, UserThread aUser) {
		boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("The user " + userName + " quitted");
		}
	}

	Set<String> getUserNames() {
		return this.userNames;
	}

	boolean hasUsers() {
		return !this.userNames.isEmpty();
	}
}