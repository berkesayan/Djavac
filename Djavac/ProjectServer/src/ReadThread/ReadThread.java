package ReadThread;

import java.net.DatagramPacket;

import java.net.InetAddress;

import java.net.MulticastSocket;

import java.net.Socket;

import java.util.List;

import java.util.stream.Collectors;

import java.awt.Color;

import java.io.*;

import java.net.*;

import com.sun.security.ntlm.Server;
import src.ChatClient;
public class ReadThread extends Thread {

    private ObjectInputStream objectInputStream;

    private Socket socket;

    private ChatClient client;

    private static String dir;

    public ReadThread(Socket socket, ChatClient client, String dir) throws ClassNotFoundException {
        this.dir = dir;

        this.socket = socket;

        this.client = client;

        try {

            InputStream input = socket.getInputStream();

            objectInputStream = new ObjectInputStream(input);

            // List<UserClass> listOfMessages = (List<UserClass>)

            // objectInputStream.readObject();

            ServerGUI_Program.appendToPane("\n- Server started succesfully", Color.GREEN);

            ServerGUI_Program.startServerButton.setBackground(Color.GREEN);

        } catch (IOException ex) {

            System.out.println("READTHREAD BOZULDU: " + ex);

            ex.printStackTrace();

        }

    }

    public <UserClass> void run() {

        while (true) {

            try {

                List<UserClass> listOfMessages = (List<UserClass>) objectInputStream.readObject();

                System.out.println(listOfMessages.get(0).getClassName());

                Server.KillProcess(Server.server_p_id);

                ServerGUI_Program.appendToPane("Server stopped RMIServer process", Color.GREEN);

                ServerCodeGenerator scg = new ServerCodeGenerator();

                for (UserClass userClass : listOfMessages) {

                    scg.editImplementation(userClass.getDistributedFunctions(), Server.implementFileName,

                            this.dir, userClass);

                    scg.editInterface(userClass.getDistributedFunctions(), Server.interfaceFileName, this.dir);

                }

                ServerGUI_Program.appendToPane("Code generator finish its mission", Color.GREEN);

                Server.CompileAndRunProject(this.dir);

                ServerGUI_Program.appendToPane("Server compiled and run the project", Color.GREEN);

            } catch (Exception e) {

                System.out.println("READTHREAD IS NOT WORKING WELL" + e);

            }

        }

    }

    public static void main(String[] args) {

        System.out.println("Do you want to Continue? ");

        // declare the BufferedReader Class

        // used the InputStreamReader to read the console input

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int readVal;

        // catch the possible IOException by the read() method

        try {

            // assign the return value of the read() method to a variable

            readVal = reader.read();

            // print the read char converted in int

            System.out.println("Character from console in int:" + readVal);

            // print the char read from console input

            // by the BufferedReader class

            System.out.println("Character from console in char:" + (char) readVal);

            // close the BufferedReader object

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
