import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

public class RMIServer {
	public static Registry reg;
	static String port = "12345";
	static String LOCAL_IP = "localhost";
	// static String INTERNET_IP = "192.168.1.37";
	static String bindLocation = "//" + LOCAL_IP + ":" + port + "/MyServer";
	static RMIInterface in;
	static RMIImplement sv;

	public static boolean isWorking = false;

	public static void execute(boolean isRecreate, String INTERNET_IP) {
		try {
//			System.setProperty("java.rmi.server.useCodebaseOnly", "true");
//			System.setProperty("java.rmi.server.codebase", "C:\\Users\\yaren\\Desktop\\NewEclipse-Workspace\\ProjectServer\\src\\");
//			System.setProperty("java.security.policy","C:\\Users\\yaren\\Desktop\\NewEclipse-Workspace\\ProjectServer\\my.policy");
			System.setProperty("java.rmi.server.hostname", INTERNET_IP);
//			System.setSecurityManager(new RMISecurityManager());
			sv = new RMIImplement();
			try {
				try {

					reg = LocateRegistry.createRegistry(Integer.parseInt(port));
					System.err.println("New RMIRegistry is created");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						reg = LocateRegistry.getRegistry(Integer.parseInt(port));
						System.err.println("Old Registry found " + reg);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				Naming.rebind(bindLocation, sv);

				new Thread().start();
				Thread thread = new Thread() {
					public void run() {
						try {
							CheckAvailableServerSocket.execute();
							System.err.println("Server available socket ready");
						} catch (Exception e) {
							System.err.println("Server available exception: " + e.toString());
						}
					}
				};
				thread.start();

				System.err.println("Server ready");

			} catch (Exception e) {
				System.err.println("Server exception: " + e.toString());
				e.printStackTrace();

			}

			System.out.println("RMI SERVER STARTED!!");
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length > 0)
			execute(false, args[0]);
		else
			execute(false, "");
	}
}