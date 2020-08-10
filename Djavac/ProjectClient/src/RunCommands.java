import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunCommands {

	public void runcommands(String directory) {
		String[] command = new String[] { "sh", "-c", "cd;" + " cd " + directory + ";"
				+ " javac Copy_ClientMain2.java; java Copy_ClientMain2" };
		int counter = 0;
		Process proc;

		try {
			proc = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (Exception e) {
			int a = 5;
		}
	}

}
