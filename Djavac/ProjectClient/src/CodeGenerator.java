import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeGenerator {
	static String port = "12345";

	void editInterface(ArrayList<DistributedFunction> functions, String interfaceFileName, String directory)
			throws FileNotFoundException {

		// Setup File and Scanner
		ArrayList<String> temp = new ArrayList<String>();
		// ArrayList<String> lines = new ArrayList<String>();
		// String afile="a.java";

		File myObj = new File(directory + interfaceFileName); // RMIInterface.java

		Scanner myReader = new Scanner(myObj);

		// Eðer RMIInterface içi doluysa sil ve diðer interface dosyaya yaz

		ArrayList<String> lines = new ArrayList<String>();

		while (myReader.hasNextLine()) {
			temp.add(myReader.nextLine());
		}
		int index = temp.size() - 1;
		for (; index >= 0; index--) {
			if (temp.get(index).indexOf('}') > -1) {
				break;
			}
		}

		for (int i = 0; i < index; i++) {
			lines.add(temp.get(i));
		}

		String str1 = "import java.rmi.*;";
		int kontrol = 0;
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).equals(str1)) {
				kontrol = -1;
				break;
			}
		}
		if (kontrol != -1) {
			lines.add(0, str1);

		}
		for (DistributedFunction df : functions) {

			// deðiþti
			df.setParams(df.getParams().replaceAll("\\r\\n|\\r|\\n", ""));

			df.setParams(df.getParams().replaceAll("\\r\\t|\\r|\\t", ""));

			String str2 = "\tpublic " + df.ReturnType + " " + df.FunctionName + df.Params + " "
					+ "throws RemoteException;";

			int deneme = 0;
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).equals(str2)) {
					deneme = -1;
					break;
				}
			}
			if (deneme != -1) {
				lines.add(str2);
			}
		}

		lines.add("}");
		WriteToFile(directory, interfaceFileName, lines);
	}

	public void editMain(UserClass userClass, String mainFileName, String directory, String IpAddress)
			throws IOException {
		// Setup File and Scanner
		PrintWriter writer;
		try {
			writer = new PrintWriter(directory + "Copy_" + mainFileName, "UTF-8");

			// imports
			for (String line : userClass.Imports) {
				writer.println("import " + line + ";");
			}
			writer.println("import java.rmi.Naming;");
			// class start
			writer.println("public class Copy_" + mainFileName.substring(0, mainFileName.length() - 5) + "{");

			// regular functions
			for (RegularFunction rf : userClass.getRegularFunctions()) {
				String throwables = "";
				if (!rf.Throwables.isEmpty()) {
					throwables = " throws ";
					int count = 0;
					for (String th : rf.Throwables) {
						count++;
						if (count != rf.Throwables.size())
							throwables += th + ", ";
						else {
							throwables += th + " ";
						}
					}
					throwables.substring(0, throwables.length() - 3);
				}

				String returnType = rf.getReturnType();
				boolean isMain = false;
				if (rf.getFunctionName() == "main") {
					returnType = "static void";
					isMain = true;
				}
				if (!isMain)
					writer.println("\tpublic " + returnType + " " + rf.getFunctionName() + rf.getParams() + throwables);
				else
					writer.println("\tpublic " + returnType + " " + rf.getFunctionName() + rf.getParams()
							+ " throws Exception");
				for (String line : rf.getImplementation()) {
					writer.println("\t" + line);
				}
			}

			// distributed functions
			for (DistributedFunction rf : userClass.getDistributedFunctions()) {
				writer.println("\tpublic static " + rf.getReturnType() + " " + rf.getFunctionName() + rf.getParams()
						+ " throws Exception { ");
				writer.println("\t\tRMIInterface name = (RMIInterface) Naming.lookup(\"//" + IpAddress
						+ ":12345/MyServer\");");

				if (!rf.ReturnType.trim().equalsIgnoreCase("void"))
					writer.println("\t\treturn name." + rf.getFunctionName() + rf.getParamsWithoutTypes() + ";");
				else {
					writer.println("\t\tname." + rf.getFunctionName() + rf.getParamsWithoutTypes() + ";");
				}
				writer.println("\t}");
			}

			// class end
			writer.println("}");

			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public DistributedFunction getDistributedFunction(String line) {
		int index = line.indexOf("(");
		int temp = index;
		index--;

		// Eliminate spaces
		while (line.charAt(index) == ' ') {
			index--;
		}

		// Get Function Name Start Index
		while (line.charAt(index) != ' ') {
			index--;
		}

		DistributedFunction df = new DistributedFunction();
		df.setName(line.substring(index + 1, temp));

		// Eliminate spaces
		while (line.charAt(index) == ' ') {
			index--;
		}
		temp = index + 1;

		// Get Function Return Type Start Index
		while (line.charAt(index) != ' ') {
			index--;
		}
		df.setReturnType((line.substring(index + 1, temp)));

		// start getting parameters
		index = line.indexOf(")");
		temp = index;
		index--;

		// Eliminate spaces
		while (line.charAt(index) == ' ') {
			index--;
		}

		// Get Function Params
		while (line.charAt(index) != '(') {
			index--;
		}

		df.setParams((line.substring(index, temp + 1)));

		return df;
	}

	static void WriteToFile(String directory, String fileName, ArrayList<String> lines) throws FileNotFoundException {
		PrintWriter writer;
		try {
			writer = new PrintWriter(directory + fileName, "UTF-8");

			for (String line : lines) {
				writer.println(line);
			}
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}