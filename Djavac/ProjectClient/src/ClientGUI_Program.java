import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientGUI_Program {
	private static Color BackgroundColor = new Color(240, 248, 255);
	private static JButton startChatClientButton;
	public static JTextPane informationBox;
//	static String directoryForCmd = "C:\\Users\\yaren\\Desktop\\eclipsesworkspace\\Client\\src\"";
	public static JButton startClientButton;
	public static JTextField directoryTextField, chatClientIpTextField, clientipaddress;
	public static String IP = "";
	public static JFrame f;
	public static JPanel p;
	ArrayList<String> connectedclients = new ArrayList<String>();
	File files = null;

	public static void main(String[] args) throws UnknownHostException {
		String osName = getOperatingSystem();
		f = new JFrame("DJAVAC Client GUI");
		p = new JPanel(null);
		JLabel chatClientIpLabel = new JLabel("Main Server Ip:");
		JLabel chatClientPortLabel = new JLabel("Port:");
		JLabel clientipaddresslabel = new JLabel("Client IP:");
		JRadioButton newChatGroupRadioButton = new JRadioButton("New");
		JRadioButton existingChatGroupRadioButton = new JRadioButton("Existing");
		ButtonGroup bg = new ButtonGroup();
		chatClientIpTextField = new JTextField("");
		JTextField chatClientPortTextField = new JTextField("1099");
		clientipaddress = new JTextField();
		startChatClientButton = new JButton("Distribute");
		startClientButton = new JButton("Execute");

		createChatClientIPandPORT(p, chatClientIpLabel, chatClientPortTextField, chatClientIpTextField,
				chatClientPortLabel, clientipaddress);
		createFrame(f, p);
		createDirectoryTextField(p);
		StartChatClientButton(startChatClientButton, existingChatGroupRadioButton, chatClientIpTextField,
				chatClientPortTextField, p, clientipaddress);
		StartClientButton(p, startClientButton);

		JTextPaneWindow(p);
		f.repaint();
	}

	public static void updateChatClientButtonColor(Color c) {
		startChatClientButton.setBackground(c);
	}

	private static String getMyIpAddress() throws UnknownHostException {
		InetAddress localhost = InetAddress.getLocalHost();
		return localhost.getHostAddress().trim();
	}

	public static void appendToPane(String msg, Color c) {
		StyledDocument doc = informationBox.getStyledDocument();

		Style style = informationBox.addStyle("I'm a Style", null);
		StyleConstants.setForeground(style, c);

		try {
			doc.insertString(doc.getLength(), msg, style);
			p.repaint();
			f.repaint();
		} catch (Exception e) {

		}
	}

	private static String getDirectory() {
		String directory = System.getProperty("user.dir");
		String replacedDirectory = "";
		if (directory.indexOf("\"") >= -1) {
			replacedDirectory = directory.replace("\"", "\\");
		}
		if (getOperatingSystem().equalsIgnoreCase("Windows 10"))
			return replacedDirectory + "\\";
		else
			return replacedDirectory + "/";
	}

	public static void createFrame(JFrame f, JPanel p) {
		// JFrame

		f.setBounds(580, 250, 836, 503);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);

		p.setBackground(BackgroundColor);
		p.setSize(836, 503);
		f.getContentPane().add(p);

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

	public static void createChatClientIPandPORT(JPanel p, JLabel chatClientIpLabel, JTextField chatClientPortTextField,
			JTextField chatClientIpTextField, JLabel chatClientPortLabel, JTextField clientipaddresstext)
			throws UnknownHostException {
		// ChatClient IP and PORT Text Field
		chatClientIpLabel.setBounds(360, 20, 90, 30);
		p.add(chatClientIpLabel, null);

		chatClientIpTextField.setBounds(450, 20, 90, 30);
		p.add(chatClientIpTextField, null);
		chatClientIpTextField.setText("10.0.2.?");

		chatClientPortLabel.setBounds(545, 20, 40, 30);
		p.add(chatClientPortLabel, null);
		chatClientPortLabel.setVisible(false);

		chatClientPortTextField.setBounds(580, 20, 40, 30);
		p.add(chatClientPortTextField, null);
		chatClientPortTextField.setText("12345");
		chatClientPortTextField.setVisible(false);
		chatClientIpTextField.setText(getMyIpAddress());
	}

	public static void StartChatClientButton(final JButton startChatClientButton, JRadioButton existingChatGroupRadioButton,
											 final JTextField chatClientIpTextField, JTextField chatClientPortTextField, JPanel p,
											 JTextField clientipaddress) {
		// Start Chat Client Button

		startChatClientButton.setBounds(10, 60, 130, 30);
		p.add(startChatClientButton, null);
		startChatClientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final DistributedJavaCompiler djcompler = new DistributedJavaCompiler();
				try {
					new Thread().start();
					Thread thread = new Thread() {
						@Override
						public void run() {
							try {
								djcompler.execute(directoryTextField.getText(), chatClientIpTextField.getText());
								startChatClientButton.setBackground(Color.GREEN);
							} catch (Exception e) {
								startClientButton.setBackground(Color.RED);
							}
						}
					};
					thread.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					startChatClientButton.setBackground(Color.RED);
					e1.printStackTrace();
				}
			}
		});
	}

	public static String getOperatingSystem() {
		String os = System.getProperty("os.name");
		return os;
	}

	public static void StartClientButton(JPanel p, final JButton startClientButton) {
		// Start Client Button
		startClientButton.setBounds(10, 95, 130, 30);
		p.add(startClientButton, null);
		startClientButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread().start();
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
//							deleteClientClassFiles();
//							deleteServerClassFiles();
							CompileAndRunCopyClientCode();

							ClientGUI_Program.appendToPane("/n -Execution has been ended!", Color.GREEN);

						} catch (Exception e) {
							startClientButton.setBackground(Color.RED);
						}
					}
				};
				thread.start();
			}
		});
	}

	public static void CompileAndRunCopyClientCode() {
		try {

			if (getOperatingSystem().equalsIgnoreCase("Windows 10")) {
				System.out.println("Compile started");
				ClientGUI_Program.appendToPane("\n- Programmer code is ready to start compilation!", Color.GREEN);
				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"" + directoryTextField.getText()
						+ "\"" + " && javac Copy_ClientMain2.java && java Copy_ClientMain2");
				builder.redirectErrorStream(true);
				Process p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				p.waitFor();
				while (true) {
					line = r.readLine();
					if (line == null)
						break;
					ClientGUI_Program.appendToPane("\n-	" + line, Color.PINK);
				}
				((JComponent) startClientButton).setBackground(Color.GREEN);
				ClientGUI_Program.appendToPane("\n- Compile Ended!", Color.CYAN);

			} else {
				RunCommands variable = new RunCommands();
				System.out.println("Compile started");
				ClientGUI_Program.appendToPane("\n- Programmer code is ready to start compilation!", Color.GREEN);
				variable.runcommands(directoryTextField.getText());
				((JComponent) startClientButton).setBackground(Color.GREEN);
				ClientGUI_Program.appendToPane("\n- Compile Ended!", Color.CYAN);
			}
		} catch (Exception e) {
			((JComponent) ClientGUI_Program.startClientButton).setBackground(Color.RED);
		}
	}

	public static void JTextPaneWindow(JPanel p) {
		// JTextPane
		informationBox = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(informationBox);
		scrollPane.setBounds(300, 60, 520, 405);
		p.add(scrollPane);
		informationBox.setEditable(false);
		appendToPane("Welcome to DJAVAC Client GUI!", Color.ORANGE);
		informationBox.setBackground(Color.black);
	}
}