
import java.awt.Color;

import java.awt.image.Kernel;

import java.io.BufferedInputStream;

import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.DataInput;

import java.io.DataInputStream;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.io.OutputStream;

import java.io.OutputStreamWriter;

import java.lang.reflect.Array;

import java.lang.reflect.Field;

import java.net.Inet4Address;

import java.net.InetAddress;

import java.net.InetSocketAddress;

import java.net.ServerSocket;

import java.net.Socket;

import java.net.SocketAddress;

import java.net.UnknownHostException;

import java.rmi.Naming;

import java.rmi.NotBoundException;

import java.rmi.Remote;

import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;

import java.rmi.server.ExportException;

import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;

import java.util.List;

import java.util.Scanner;

import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import src.ChatClient;
public class Server {

	static String interfaceFileName = "RMIInterface.java";

	static String implementFileName = "RMIImplement.java";

	static String directory, clientIPAddress = "";

	static String directoryForCmd;

	static long server_p_id = -1;

	public String clientName[];

	public String clientPass[];

//	private ArrayList<RMIInterface> clientList;	

	static ChatClient client;

	static boolean processIdSuccessful = false;

	static boolean isMainServer = false;
	static String publicIp = "";

	public void execute(String directory, String directoryForCmd, String ipAddress, boolean isMainServer,
			String publicIp) {

		try {

			this.publicIp = publicIp;
			this.isMainServer = isMainServer;

			this.directory = directory;

			this.directoryForCmd = directoryForCmd;

			set_group(ipAddress, directory);

			while (true) {

				listenClientAndEditFiles();

				CompileAndRunProject(directory);

			}

		} catch (Exception e) {

			ServerGUI_Program.appendToPane("\n- Error on Server!", Color.RED);

			ServerGUI_Program.startServerButton.setBackground(Color.RED);

		}

	}

	public static boolean CheckChatGroupConnection(String ipAddress, int port) {

		try {

			Socket s = new Socket(ipAddress, port);

			s.close();

			TimeUnit.MILLISECONDS.sleep(2000);

			ServerGUI_Program.appendToPane("\n- Sucessfully connected to the Central Server!", Color.GREEN);

			return true;

		} catch (Exception e) {

			ServerGUI_Program.appendToPane("\n- Error connecting to the Central Server!", Color.RED);

			return false;

		}

	}

	public static void SendAvailableServerIpAddressToClient() throws Exception {

		if (isMainServer) {

			Socket socket = new Socket(clientIPAddress, 8888);

			OutputStream outputStream = socket.getOutputStream();

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

			String availableServerIP = ConnectedRMIServer

					.GetAvailableServer(ServerGUI_Program.connectedusers).IPAddress;

			ServerGUI_Program.appendToPane("\n- Available Server is : " + availableServerIP, Color.GREEN);

			objectOutputStream.writeObject(availableServerIP);

			TimeUnit.SECONDS.sleep(2);

			socket.close();

		}

	}

	public static void CompileAndRunProject(String dir) {

		try {

			if (ServerGUI_Program.getOperatingSystem().equalsIgnoreCase("Windows 10")) {

				new Thread().start();

				ServerGUI_Program.RMIServerThread = new Thread() {

					public void run() {

						System.out.println("Compile started");

						ServerGUI_Program.appendToPane("\n- Programmer code is ready to start compilation!",

								Color.GREEN);

						ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",

								"cd \"" + directoryForCmd + " && javac *.java && java RMIServer " + publicIp);

						builder.redirectErrorStream(true);

						Process p = null;

						try {

							p = builder.start();

						} catch (IOException e1) {

							// TODO Auto-generated catch block

							e1.printStackTrace();

						}

						BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

						String line = "";

						int count = 0;

						while (true) {

							try {

								line = r.readLine();

								if (count == 0) {

									try {

										SendAvailableServerIpAddressToClient();

									} catch (Exception e) {

										ServerGUI_Program.appendToPane("\n-	ERROR in Server.java compilation",

												Color.RED);

										e.printStackTrace();

									}

								}

							} catch (IOException e) {

								// TODO Auto-generated catch block

								e.printStackTrace();

							}

							if (line == null)

								break;

							ServerGUI_Program.appendToPane("\n-	" + line, Color.PINK);

							count++;

						}

					}

				};

				ServerGUI_Program.RMIServerThread.start();

			} else {

				// del();

				new Thread().start();

				ServerGUI_Program.RMIServerThread = new Thread() {

					public void run() {

						try {

							System.out.println("Compile started");

							TimeUnit.MILLISECONDS.sleep(2000);

							ServerGUI_Program.appendToPane("\n- Programmer code is ready to start compilation!",

									Color.GREEN);

							String[] args = new String[] { "bash", "-c",

									"fuser -k 12345/tcp;cd " + dir + "; javac *.java; java RMIServer " + publicIp };

							Process proc = new ProcessBuilder(args).start();

							BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

							String line = "";

							int count = 0;

							while ((line = reader.readLine()) != null) {

								if (count == 0) {

									try {

										SendAvailableServerIpAddressToClient();

									} catch (Exception e) {

										ServerGUI_Program.appendToPane("\n-	ERROR in Server.java compilation",

												Color.RED);

										e.printStackTrace();

									}

								}

								ServerGUI_Program.appendToPane("\n" + line, Color.GREEN);

								count++;

							}

							TimeUnit.MILLISECONDS.sleep(2000);

							ServerGUI_Program.appendToPane("\n- Compile Ended!", Color.CYAN);

						} catch (Exception e) {

							e.printStackTrace();

						}

					}

				};

				ServerGUI_Program.RMIServerThread.start();

			}

		} catch (Exception e) {

			((JComponent) ServerGUI_Program.startServerButton).setBackground(Color.RED);

		}

	}

	public static void del() {

		File f = new File(directory + "RMIImplement_Stub.class");

		f.delete();

		File f2 = new File(directory + "RMIInterface.class");

		f2.delete();

		File f3 = new File(directory + "RMIImplement.class");

		f3.delete();

	}

	private static void listenClientAndEditFiles() {

		try {

			System.out.println("Listening for client update");

			ServerSocket ss = new ServerSocket(7777);

			TimeUnit.MILLISECONDS.sleep(2000);

			ServerGUI_Program.appendToPane("\n- Listening for Client code on Port 7777!", Color.YELLOW);

			Socket socket = ss.accept();

			InetSocketAddress sockaddr = (InetSocketAddress) socket.getRemoteSocketAddress();

			InetAddress inaddr = sockaddr.getAddress();

			Inet4Address in4addr = (Inet4Address) inaddr;

			String ip4string = in4addr.toString();

			clientIPAddress = ip4string.substring(1);

			ServerGUI_Program.appendToPane("\n- Client with IP Address: " + clientIPAddress + " sent code!",

					Color.GREEN);

			InputStream inputStream = socket.getInputStream();

			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			@SuppressWarnings("unchecked")

			List<UserClass> listOfMessages = (List<UserClass>) objectInputStream.readObject();

			ServerGUI_Program.appendToPane("\n- Client code updated. Killing the RMIServer!", Color.GREEN);

			client.writeThread.writer.writeObject(listOfMessages);

			KillProcess(server_p_id);

			ServerCodeGenerator scg = new ServerCodeGenerator();

			for (UserClass userClass : listOfMessages) {

				scg.editImplementation(userClass.getDistributedFunctions(), implementFileName, directory, userClass);

				scg.editInterface(userClass.getDistributedFunctions(), interfaceFileName, directory);

			}

			ss.close();

			socket.close();

			TimeUnit.MILLISECONDS.sleep(3500);

			ServerGUI_Program.appendToPane("\n- Server Code is updated!", Color.GREEN);

		} catch (Exception e) {

			ServerGUI_Program.appendToPane("\n- Error on listening Client Code!", Color.RED);

			ServerGUI_Program.startServerButton.setBackground(Color.RED);

		}

	}

	public static void KillProcess(long p_id) throws Exception {

		try {

			if (ServerGUI_Program.getOperatingSystem().equalsIgnoreCase("Windows 10")) {

				CmdKillProcess();

			}

		} catch (Exception e) {

			ServerGUI_Program.startServerButton.setBackground(Color.RED);

		}

	}

	public static void CmdKillProcess() throws Exception {

		ProcessBuilder findbuilder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano|findstr \"PID :12345\" ");

		findbuilder.redirectErrorStream(true);

		Process p = findbuilder.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line;

		int count = 0;

		int i;

		Integer pid = 0;

		while (true) {

			line = r.readLine();

			if (line == null) {

				break;

			}

			System.out.println(line);

			if (count > 0) {

				for (i = line.length() - 1; i > 0; i--) {

					if (!Character.isDigit(line.charAt(i))) {

						pid = Integer.parseInt(line.substring(i + 1));

						break;

					}

				}

				if (pid > 0) {

					break;

				}

			}

			count++;

		}

		ProcessBuilder killbuilder = new ProcessBuilder("cmd.exe", "/c", " taskkill /pid " + pid + " /f");

		killbuilder.redirectErrorStream(true);

		Process pkiller = killbuilder.start();

		BufferedReader readkiller = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String linekiller = null;

		while (true) {

			line = readkiller.readLine();

			if (linekiller == null) {

				break;

			}

			System.out.println(linekiller);

		}

	}

	public void set_group(String ip, String dir) {

		try {

			String hostname = ip;// ipli halini yazicaz

			int port = 1099;

			client = new ChatClient(hostname, port, dir);

			client.execute();

			TimeUnit.MILLISECONDS.sleep(2000);

			ServerGUI_Program.startServerButton.setBackground(Color.GREEN);

			ServerGUI_Program.appendToPane("\n- Successfully connected to chat group", Color.GREEN);

		} catch (Exception e) {

			ServerGUI_Program.startServerButton.setBackground(Color.RED);

			ServerGUI_Program.appendToPane("\n- Error connecting to chat group", Color.RED);

		}

	}

}
