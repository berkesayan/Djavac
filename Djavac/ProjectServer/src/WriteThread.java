import java.io.*;
import java.net.*;
import java.util.Scanner;
import src.ChatClient;
public class WriteThread extends Thread {
    public ObjectOutputStream writer;    
    private Socket socket;
    private ChatClient client;
 
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            //writer = new PrintWriter(output, true);
            writer = new ObjectOutputStream(output);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
 
        Scanner scanner=new Scanner(System.in);
        System.out.println("Please enter the client name: ");
        
        String userName = scanner.nextLine();
        client.setUserName(userName);
        
        String text;
 
        do {
            text = scanner.nextLine();
            try {
				writer.writeObject(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
        } while (!text.equals("bye"));
 
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}