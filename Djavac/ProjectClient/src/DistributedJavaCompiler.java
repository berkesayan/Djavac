import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DistributedJavaCompiler {
	Parser parser;
	CodeGenerator codeGenerator = new CodeGenerator();

	static String port = "12345";
	static String fileName = "ClientMain2.java";
	static String interfaceFileName = "RMIInterface.java";
	static String implementFileName = "RMIImplement.java";
	static String[] classes = { "ClientMain2" };

	public void execute(String directory, String ServerChatGroupIpAddress) throws Exception {
		ArrayList<UserClass> userClasses = new ArrayList<UserClass>();
		for (String c : classes) {
			Parser p = new Parser(directory);
			ClientGUI_Program.appendToPane("\n- The code is started parsing!", Color.YELLOW);
			UserClass userClass = p.getUserClassDetails(c);
			TimeUnit.SECONDS.sleep(2);
			ClientGUI_Program.appendToPane("\n- Parsing has been finished!", Color.YELLOW);
			userClasses.add(userClass);
			TimeUnit.SECONDS.sleep(2);
			ClientGUI_Program.appendToPane("\n- Code has been started to be generated!", Color.YELLOW);
			CodeGenerator cg = new CodeGenerator();
			File interfacefile = new File(directory + interfaceFileName);

			if (interfacefile.isFile()) {
				cg.editInterface(userClass.getDistributedFunctions(), interfaceFileName, directory);
			} else {
				File createinterfacefile = new File("RMIInterface.java");
				cg.editInterface(userClass.getDistributedFunctions(), createinterfacefile.toString(), directory);
			}

			cg.editMain(userClass, fileName, directory, ServerChatGroupIpAddress);
			TimeUnit.SECONDS.sleep(2);
			ClientGUI_Program.appendToPane("\n- Code has been generated!", Color.YELLOW);
		}

		// SENDING CLIENT'S OWN CODE TO MAIN SERVER
		Socket socket = new Socket(ServerChatGroupIpAddress, 7777);
		TimeUnit.SECONDS.sleep(2);
		ClientGUI_Program.appendToPane("\n- Classes is being sended to the server!", Color.CYAN);
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(userClasses);
		socket.close();
		ClientGUI_Program.appendToPane("\n- Objects are sent to RMIServer!", Color.YELLOW);

		// RECEIVING AVAILABLE RMISERVER IP ADDRESS
		ServerSocket ss = new ServerSocket(8888);
		Socket availableSocket = ss.accept();
		InputStream inputStream = availableSocket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		@SuppressWarnings("unchecked")
		String ip = (String) objectInputStream.readObject();
		ClientGUI_Program.appendToPane("\n- Returned IP address from Server is: " + ip, Color.GREEN);
		ss.close();
		availableSocket.close();

		if (ip.equalsIgnoreCase("192.168.56.101"))
			ip = ServerChatGroupIpAddress;
		// Foreach ile edit maindeki ip
		for (String c : classes) {
			Parser p = new Parser(directory);
			UserClass userClass = p.getUserClassDetails(c);
			userClasses.add(userClass);
			CodeGenerator cg = new CodeGenerator();

			cg.editMain(userClass, fileName, directory, ip);
			ClientGUI_Program.appendToPane("\n- User -main implementation area- is updated", Color.YELLOW);
			ClientGUI_Program.appendToPane("\n- Available Server IP Address is " + ip, Color.YELLOW);
		}
	}
}
