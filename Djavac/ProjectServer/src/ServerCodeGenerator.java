
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class ServerCodeGenerator {

	private void clearImplementation(UserClass userClass, String directory) throws FileNotFoundException {
		// Setup File and Scanner
		PrintWriter writer;
		try {
			writer = new PrintWriter(directory + "RMIImplement.java", "UTF-8");

			// imports
			for (String line : userClass.Imports) {
				writer.println("import " + line + ";");
			}
			writer.println("import java.rmi.*;");
			writer.println("import java.rmi.server.UnicastRemoteObject;");
			// class start
			writer.println("public class RMIImplement extends UnicastRemoteObject implements RMIInterface {");

			writer.println("private static final long serialVersionUID = 1L;");

			writer.println("protected RMIImplement() throws RemoteException {");

			writer.println("super(); ");

			writer.println("}");

			// class fields
			for (String field : userClass.getFields()) {
				writer.println(field);
			}

			// distributed functions
			for (DistributedFunction df : userClass.getDistributedFunctions()) {
				writer.println("@Override");
				writer.println("public " + df.ReturnType + " " + df.FunctionName + df.Params + "{");
				for (String ln : df.Implementation) {
					writer.println(ln);
				}
				writer.println("}");
			}

			// class end
			writer.println("}");

			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void editImplementation(ArrayList<DistributedFunction> functions, String implementFileName, String directory,
			UserClass userClass) throws FileNotFoundException {
		clearImplementation(userClass, directory);

//		ArrayList<String> fields = new ArrayList<String>();
//		ArrayList<String> tempFields = userClass.getFields();
//
//		for (int i = 0; i < tempFields.size(); i++) {
//			fields.add(clearTurkishChars(tempFields.get(i)));
//		}
//
//		userClass.setFields(fields);
//
//		ArrayList<String> temp = new ArrayList<String>();
//		ArrayList<String> lines = new ArrayList<String>();
//		File myObj = new File(directory + implementFileName);
//		Scanner myReader = new Scanner(myObj);
//
//		while (myReader.hasNextLine()) {
//			String ln = myReader.nextLine();
//			temp.add(clearTurkishChars(ln));
//		}
//		int index = temp.size() - 1;
//		for (; index >= 0; index--) {
//			if (temp.get(index).indexOf('}') > -1) {
//				break;
//			}
//		}
//
//		for (int i = 0; i < index; i++) {
//			lines.add(temp.get(i));
//		}
//
//		for (String string : userClass.getImports()) {
//			int k = 0, imports = 0;
//			while (!lines.get(k)
//					.equals("public class RMIImplement extends UnicastRemoteObject implements RMIInterface {")) {
//				if (lines.get(k).equals("import " + string + ";")) {
//					imports = -1;
//					break;
//				}
//				k++;
//			}
//			if (imports > -1) {
//				lines.add(k - 1, "import " + string + ";");
//			}
//		}
//
//		int field = 0, asdf = 0;
//		while (asdf < userClass.getFields().size()) {
//			for (int a = 0; a < lines.size(); a++) {
//
//				if (lines.get(a).equals(userClass.getFields().get(asdf))) {
//					field = -1;
//					break;
//				} else {
//					field = asdf;
//				}
//			}
//
//			if (field >= 0) {
//				int index1 = 0;
//
//				for (int i = 0; i < lines.size(); i++) {
//					if (asdf >= 1) {
//						System.out.println(userClass.getFields().get(asdf - 1).toString());
//					}
//					if (asdf >= 1 && lines.get(i).equals(userClass.getFields().get(asdf - 1).toString())) {
//						index1 = i;
//						break;
//					} else if (asdf == 0) {
//						index1 = lines.indexOf(
//								"public class RMIImplement extends UnicastRemoteObject implements RMIInterface {");
//					}
//				}
//				lines.add(userClass.getFields().get(field));
//			} else {
//				field = -1;
//			}
//			asdf++;
//		}
//		for (DistributedFunction df : functions) {
//
//			// FORMATTING DESIGN
//			df.setParams(df.getParams().replaceAll("\\r\\n|\\r|\\n", ""));
//
//			df.setParams(df.getParams().replaceAll("\\r\\t|\\r|\\t", ""));
//
//			while (df.getImplementation().indexOf("") > -1) {
//				df.getImplementation().remove(df.getImplementation().indexOf(""));
//			}
//			if (df.getImplementation().indexOf("{") > -1) {
//				df.getImplementation().set(df.getImplementation().indexOf("{"), "\t{");
//			}
//
//			String string = "\t@Override\n" + "\tpublic " + df.ReturnType + " " + df.FunctionName + df.Params
//					+ " throws RemoteException";
//			String string1 = "\tpublic " + df.ReturnType + " " + df.FunctionName + df.Params
//					+ " throws RemoteException";
//			int deneme = 0, RMI = 0;
//			for (int i = 0; i < lines.size(); i++) {
//				if (lines.get(i).equals(string1)) {
//					deneme = -1;
//					RMI = i;
//					break;
//				}
//			}
//			if (deneme != -1) {
//				lines.add(string);
//				RMI = lines.indexOf(string);
//				RMI++;
//				for (int i = 0; i < df.getImplementation().size(); i++) {
//					lines.add(df.getImplementation().get(i));
//					if (lines.get(RMI).equals("\t{")) {
//						lines.add("\t\tRMIServer.isWorking = true;");
//					}
//					RMI++;
//					if (lines.get(RMI).contains("return")
//							&& !lines.get(RMI - 1).equals("\t\tRMIServer.isWorking = false;")) {
//						lines.add(RMI, "\t\tRMIServer.isWorking = false;");
//					} else if (lines.get(RMI).equals("\t}")) {
//						lines.add(RMI, "\t\tRMIServer.isWorking = false;");
//					}
//
//				}
//				if (lines.indexOf("{") > -1) {
//					int k = lines.indexOf("{");
//					lines.remove(k);
//					lines.add(k, "\t{");
//				}
//
//			} else {
//				int start = 0, j = 0;
//				for (int i = 0; i < df.getImplementation().size(); i++) {
//					if (!lines.contains(df.getImplementation().get(i))) {
//						j = -1;
//						break;
//					}
//				}
//				if (j != -1) {
//					start = lines.indexOf("\tpublic " + df.ReturnType + " " + df.FunctionName + df.Params
//							+ " throws RemoteException");
//					while (!lines.get(start + 2).equals("\t}")) {
//						lines.remove(start + 2);
//					}
//					int count = 2;
//					for (String string2 : df.getImplementation()) {
//						if (!string2.contentEquals("\t{") && !string2.equals("\t}")) {
//							lines.add(start + count, string2);
//							count++;
//						}
//					}
//				}
//			}
//
//		}
//
//		lines.add("}");
//
//		myReader.close();
//
//		WriteToFile(directory, implementFileName, lines);

	}

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
		/*
		 * if (myReader.hasNextLine()) { myObj.delete();
		 * 
		 * temp.add("public interface RMIInterface extends Remote {"); for
		 * (DistributedFunction df : functions) {
		 * 
		 * 
		 * temp.add( str2.replace("\n", ""));//throws RemoteException larýn ayný satýrda
		 * eklenmesini saðlayan kod
		 * 
		 * } temp.add("\t public String sayHello()  throws RemoteException;");
		 * temp.add("}"); myReader.close();
		 * 
		 * 
		 * 
		 * 
		 * 
		 * } else//eðer RMIInterface içi boþsa direkt dosyaya yaz {
		 * 
		 * temp.add("import java.rmi.Remote;");
		 * temp.add("import java.rmi.RemoteException;");
		 * temp.add("public interface RMIInterface extends Remote {"); for
		 * (DistributedFunction df : functions) { String str3 = "\t public "+
		 * df.ReturnType+" "+ df.FunctionName+" "+df.Params+" "
		 * +"throws RemoteException;"; temp.add( str3.replace("\n", ""));//throws
		 * RemoteException larýn ayný satýrda eklenmesini saðlayan kod
		 * 
		 * } temp.add("\t public String sayHello() throws RemoteException;");
		 * 
		 * temp.add("}"); myReader.close();
		 * 
		 * WriteToFile(directory, interfaceFileName, temp); }
		 */
		WriteToFile(directory, interfaceFileName, lines);
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

	public static String clearTurkishChars(String str) {
		String ret = str;
		char[] turkishChars = new char[] { 0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F,
				0x11E };
		char[] englishChars = new char[] { 'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G' };
		for (int i = 0; i < turkishChars.length; i++) {
			ret = ret.replaceAll(new String(new char[] { turkishChars[i] }),
					new String(new char[] { englishChars[i] }));
		}
		return ret;
	}
}