import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import src.ChatClient;

public class ServerGUI_Program {
	public static ChatServer chatserver;
	public static UserThread userthread;
	private static Color BackgroundColor = new Color(240, 248, 255);
	public static JButton startChatServerButton, startServerButton, startRMIServerButton;
	public static JTextPane informationBox;
	public static Thread RMIServerThread;
	static String IP = "";
	public static JTextField directoryTextField = new JTextField(), publicIpTextField = new JTextField();
	String osName = getOperatingSystem();
	static ArrayList<ConnectedRMIServer> connectedusers = new ArrayList<ConnectedRMIServer>();
	static ArrayList<String> connectablechannel = new ArrayList<String>();

	public static void main(String[] args) throws UnknownHostException {
		// JFrame
		JFrame f = new JFrame("DJAVAC Server GUI");
		f.setBounds(580, 250, 836, 503);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);
		JPanel p = new JPanel(null);
		p.setBackground(BackgroundColor);
		p.setSize(836, 503);
		f.getContentPane().add(p);

		// Directory Text Field
		JLabel directoryLabel = new JLabel("Directory:");
		directoryLabel.setBounds(10, 20, 60, 30);
		p.add(directoryLabel, null);
		final JTextField directoryTextField = new JTextField("asd");
		directoryTextField.setBounds(70, 20, 280, 30);
		p.add(directoryTextField, null);
		directoryTextField.setText(getDirectory());

		// ChatServer IP and PORT Text Field
		final JLabel chatServerIpLabel = new JLabel("Main Server Ip:");
		chatServerIpLabel.setBounds(515, 20, 90, 30);
		p.add(chatServerIpLabel, null);
		final JTextField chatServeIpTextField = new JTextField("");
		chatServeIpTextField.setBounds(605, 20, 90, 30);
		p.add(chatServeIpTextField, null);
		chatServeIpTextField.setText("192.168.1.?");
		chatServeIpTextField.setVisible(false);
		chatServerIpLabel.setVisible(false);

		final JLabel chatServerPortLabel = new JLabel("Port:");
		chatServerPortLabel.setBounds(645, 20, 40, 30);
		p.add(chatServerPortLabel, null);
		final JTextField chatServePortTextField = new JTextField("1099");
		chatServePortTextField.setBounds(580, 20, 40, 30);
		p.add(chatServePortTextField, null);
		chatServePortTextField.setText("1099");
		chatServePortTextField.setVisible(false);
		chatServerPortLabel.setVisible(false);
		chatServeIpTextField.setText(getMyIpAddress());

		// ChatServer IP and PORT Text Field
		JLabel publicIpLabel = new JLabel("Public Ip:");
		publicIpLabel.setBounds(360, 20, 90, 30);
		p.add(publicIpLabel, null);
		publicIpTextField = new JTextField("");
		publicIpTextField.setBounds(420, 20, 90, 30);
		p.add(publicIpTextField, null);
		publicIpTextField.setText("192.168.1.?");
		publicIpTextField.setText(getMyIpAddress());

		// Chat Server Radio Buttons
		final JRadioButton newChatGroupRadioButton = new JRadioButton("New");
		final JRadioButton existingChatGroupRadioButton = new JRadioButton("Existing");
		newChatGroupRadioButton.setBounds(150, 60, 50, 30);
		existingChatGroupRadioButton.setBounds(200, 60, 80, 30);
		newChatGroupRadioButton.setBackground(BackgroundColor);
		existingChatGroupRadioButton.setBackground(BackgroundColor);
		ButtonGroup bg = new ButtonGroup();
		bg.add(newChatGroupRadioButton);
		bg.add(existingChatGroupRadioButton);
		p.add(newChatGroupRadioButton, null);
		p.add(existingChatGroupRadioButton, null);
		newChatGroupRadioButton.setSelected(true);
		// Start Chat Server Button
		startChatServerButton = new JButton("Main Server");
		startChatServerButton.setBounds(10, 60, 130, 30);
		p.add(startChatServerButton, null);
		startChatServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (existingChatGroupRadioButton.isSelected()) {
					try {
						if (Server.CheckChatGroupConnection(chatServeIpTextField.getText(),
								Integer.parseInt(chatServePortTextField.getText()))) {
							startChatServerButton.setBackground(Color.GREEN);
						} else {
							startChatServerButton.setBackground(Color.RED);
						}
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Check Your Port Number!");
					}
				} else {
					new Thread().start();
					Thread thread = new Thread() {
						public void run() {
							try {
								chatserver = new ChatServer(1099);
								chatserver.execute();
							} catch (Exception e) {
							}
						}
					};
					thread.start();
				}
			}
		});
		// Radio Button Change Listener
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changEvent) {
				AbstractButton aButton = (AbstractButton) changEvent.getSource();
				ButtonModel aModel = aButton.getModel();
				if (newChatGroupRadioButton.isSelected()) {
					chatServeIpTextField.setVisible(false);
					chatServerIpLabel.setVisible(false);
					chatServePortTextField.setVisible(false);
					chatServerPortLabel.setVisible(false);
				} else {
					chatServeIpTextField.setVisible(true);
					chatServerIpLabel.setVisible(true);
				}
			}
		};
		newChatGroupRadioButton.addChangeListener(changeListener);
		existingChatGroupRadioButton.addChangeListener(changeListener);
		// Start Server Button
		startServerButton = new JButton("Server");
		startServerButton.setBounds(10, 95, 130, 30);
		p.add(startServerButton, null);
		startServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread().start();
				Thread thread = new Thread() {
					public void run() {
						if (newChatGroupRadioButton.isSelected()) {
							// new
							Server server = new Server();
							server.directory = directoryTextField.getText();
							server.execute(directoryTextField.getText(), directoryTextField.getText() + "\"",
									chatServeIpTextField.getText(), newChatGroupRadioButton.isSelected(),
									publicIpTextField.getText());
						} else {
							// String hostname, int port
							ChatClient clientch = new ChatClient(chatServeIpTextField.getText(),
									Integer.parseInt(chatServePortTextField.getText()), directoryTextField.getText());
							try {
								clientch.execute();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};
				thread.start();
			}
		});
		// Start RMIServer Button
		startRMIServerButton = new JButton("RMI Server");
		startRMIServerButton.setBounds(10, 130, 130, 30);
		p.add(startRMIServerButton, null);
		startRMIServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread().start();
				Thread thread = new Thread() {
					public void run() {
						RunRMIServer(directoryTextField.getText(), publicIpTextField.getText());
					}
				};
				thread.start();
			}
		});
		// JTextPane
		informationBox = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(informationBox);
		scrollPane.setBounds(300, 60, 520, 405);
		p.add(scrollPane);
		informationBox.setEditable(false);
		appendToPane("Welcome to DJAVAC!", Color.ORANGE);
		informationBox.setBackground(Color.black);
		f.repaint();
	}

	public static void RunRMIServer(String dir, String publicIp) {
		try {
			if (ServerGUI_Program.getOperatingSystem().equalsIgnoreCase("Windows 10")) {
				CmdKillProcess();
				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
						"cd \"" + dir + "\"" + " && javac *.java && java RMIServer " + publicIp);
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (line == null)
						break;
					else if (line.equalsIgnoreCase("RMI SERVER STARTED!!")) {
						startRMIServerButton.setBackground(Color.GREEN);
					}
					ServerGUI_Program.appendToPane("\n-	" + line, Color.PINK);
					count++;
				}
			} else {
				try {
					String[] args = new String[] { "bash", "-c",
							"fuser -k 12345/tcp; cd " + dir + "; javac *.java; java RMIServer " + publicIp };
					Process proc = new ProcessBuilder(args).start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					String line = "";
					while ((line = reader.readLine()) != null) {
						ServerGUI_Program.appendToPane("\n" + line, Color.YELLOW);
					}
					startRMIServerButton.setBackground(Color.GREEN);
				} catch (Exception e) {
					e.printStackTrace();
					ServerGUI_Program.appendToPane("\n RMI Server Error!\n", Color.RED);
					startRMIServerButton.setBackground(Color.RED);
				}
			}
		} catch (Exception e) {
			ServerGUI_Program.appendToPane("\n RMI Server Error!\n", Color.RED);
			startRMIServerButton.setBackground(Color.RED);
			e.printStackTrace();
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

	private static String getMyIpAddress() throws UnknownHostException {
		InetAddress localhost = InetAddress.getLocalHost();
		return localhost.getHostAddress().trim();
	}

	public static String getOperatingSystem() {
		String os = System.getProperty("os.name");
		return os;
	}

	public static void createDirectoryTextField(JPanel p) {
		// Directory Text Field
		JLabel directoryLabel = new JLabel("Directory:");
		directoryLabel.setBounds(10, 20, 60, 30);
		p.add(directoryLabel, null);
		directoryTextField = new JTextField("asd");
		directoryTextField.setBounds(70, 20, 280, 30);
		p.add(directoryTextField, null);
		directoryTextField.setText(getDirectory());
	}

	public static void appendToPane(String msg, Color c) {
		StyledDocument doc = informationBox.getStyledDocument();
		Style style = informationBox.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, c);
		try {
			doc.insertString(doc.getLength(), msg, style);
		} catch (BadLocationException e) {
		}
	}

	public static void updateChatServerButtonColor(Color c) {
		startChatServerButton.setBackground(c);
	}

	private static String getDirectory() {
		String directory = System.getProperty("user.dir");
		String replacedDirectory = "";
		if (directory.indexOf("\"") >= -1) {
			replacedDirectory = directory.replace("\"", "\\");
		}
		if (getOperatingSystem().equalsIgnoreCase("Windows 10"))
//			return replacedDirectory + "\\src\\";
			return replacedDirectory + "\\";
		else
//			return replacedDirectory + "/src/";
			return replacedDirectory + "/";
	}
}
