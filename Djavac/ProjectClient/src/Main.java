import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Main {
	static String serverIpAddress = "192.168.0.15";
	static String port = "12345";
	static String directory = "C:\\Users\\yaren\\Desktop\\eclipsesworkspace\\server\\src\\";
	private static Socket clientSocket;

	public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
	
		RMIInterface name = (RMIInterface) Naming.lookup("//" + serverIpAddress + ":" + port + "/MyServer");
	}
	
	//netstat -ano|findstr "PID :1099" 
}

