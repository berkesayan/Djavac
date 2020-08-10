import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunCommandsServer {
	public static void main(String[] args) throws IOException, InterruptedException {
//		runcommands();
	}

	public void runcommands(String directory) {
		String[] command = new String[] { "sh", "-c", "cd &&" + " cd " + directory + " &&"
				+ " javac *.java && rmic RMIImplement && java RMIServer" };
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
