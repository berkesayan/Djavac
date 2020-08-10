import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Parser {
	static String port = "12345";
	static String directory;
	static String fileName = "ClientMain2.java";
	static String interfaceFileName = "RMIInterface.java";
	static String implementFileName = "RMIImplement.java";

	public Parser(String directory) {
		Parser.directory = directory;
	}

	private String trToEn(String str) {
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

	public UserClass getUserClassDetails(String className) throws Exception {
		UserClass userClass = new UserClass();
		userClass.setClassName(className);
		ArrayList<DistributedFunction> functions = getDistributedFunctions(className);
		userClass.setDistributedFunctions(getDistributedFunctionDetails(functions, className));
		userClass.setImports(getImports(className));
		ArrayList<RegularFunction> regfunctions = getRegularFunctions(className);
		userClass.setRegularFunctions(getRegularFunctions(className));
		userClass.setRegularFunctions(getRegularFunctionDetails(regfunctions, className));
		userClass.setFields(getClassFields(className));

		return userClass;
	}

	public ArrayList<String> getClassFields(String className) throws Exception {
		ArrayList<String> ret = new ArrayList<String>();

		CompilationUnit cu = StaticJavaParser.parse(new File(directory + fileName));

//		Optional<ClassOrInterfaceDeclaration> classA = cu.getClassByName(className);

		List<FieldDeclaration> a = cu.findAll(FieldDeclaration.class);

		for (FieldDeclaration fieldDeclaration : a) {
			ret.add(trToEn(fieldDeclaration.toString()));
		}

		return ret;
	}

	public ArrayList<RegularFunction> getRegularFunctionDetails(ArrayList<RegularFunction> functions, String className)
			throws ClassNotFoundException {
		ArrayList<RegularFunction> rfs = functions;

		try {
			Stack<Character> brackets = new Stack<>();
			ArrayList<Character> implementationChars = new ArrayList<Character>();
			ArrayList<Character> parametersChars = new ArrayList<Character>();

			File myObj = new File(directory + fileName);
			Scanner myReader = new Scanner(myObj);
			boolean fncFound = false, fncStarted = false;
			boolean searching = false, searchingParameters = false, parametersStarted = false;
			int currentDistributedFunctionIndex = -1;
			int counter = 0;
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();

				for (counter = 0; counter < rfs.size(); counter++) {
					if (line.indexOf(rfs.get(counter).FunctionName) > -1) {
						searching = true;

					}
					if (searching) {
						for (int d = 0; d < rfs.size(); d++) {
							if (line.indexOf(rfs.get(d).FunctionName) > -1) {
								searching = false;
								searchingParameters = true;
								currentDistributedFunctionIndex = d;
							}

						}
					}

					if (searchingParameters) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == '(') {
								brackets.push('(');
								parametersStarted = true;
							} else if (line.charAt(i) == ')') {
								brackets.pop();
							}

							if (parametersStarted && !brackets.isEmpty()) {
								parametersChars.add(line.charAt(i));
							}

							if (brackets.isEmpty() && parametersStarted) {
								parametersChars.add(')');
								fncFound = true;
								searchingParameters = false;
								parametersStarted = false;
								break;
							}
						}
						parametersChars.add('\n');
					}
				}
				if (fncFound) {
					for (int i = 0; i < line.length(); i++) { //
						if (line.charAt(i) == '{') {
							brackets.push('{');
							fncStarted = true;
						} else if (line.charAt(i) == '}') {
							brackets.pop();
						}

						if (fncStarted && !brackets.isEmpty()) {
							implementationChars.add(line.charAt(i));
						}

						if (brackets.isEmpty() && fncStarted) {
							implementationChars.add('}');
							fncStarted = false;
							fncFound = false;
							searching = false;

							// CHAR[] -> STRING -> STRING[] -> ARRAYLIST<STRING>
							rfs.get(currentDistributedFunctionIndex).Params = getStringRepresentation(parametersChars);
							Collections.addAll(rfs.get(currentDistributedFunctionIndex).Implementation,
									getStringRepresentation(implementationChars).split("\\r?\\n"));

							currentDistributedFunctionIndex = -1;
							implementationChars = new ArrayList<Character>();
							parametersChars = new ArrayList<Character>();
							break;
						}
					}
					implementationChars.add('\n');
				}
			}

			myReader.close();

			int i = 0;
			for (RegularFunction fnc : rfs) {
				while (fnc.Implementation.get(0).isEmpty() || fnc.Implementation.get(0) == "{") {
					fnc.Implementation.remove(0);
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return rfs;
	}

	public ArrayList<String> getImports(String className) {
		ArrayList<String> imports = new ArrayList<String>();

		boolean searching = false;
		String str = "";
		try {
			File myObj = new File(directory + fileName);
			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				int i = 0;
				if (line.indexOf("import ") > -1 && !searching) {
					searching = true;
					i = line.indexOf("import ") + 6;
				}

				if (searching) {
					while (i != line.length() && line.charAt(i) != ';') {
						str += line.charAt(i);
						i++;
					}
					if (i != line.length() && line.charAt(i) == ';') {
						searching = false;
						str = str.replaceAll(" ", "");
						str = str.replaceAll("\t", "");
						str = str.replaceAll("\n", "");
						str = str.trim();
						imports.add(str);
						str = "";
						searching = false;
					}
				}
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return imports;
	}

	public ArrayList<DistributedFunction> getDistributedFunctionDetails(ArrayList<DistributedFunction> functions,
			String className) {
		ArrayList<DistributedFunction> dfs = functions;

		try {
			Stack<Character> brackets = new Stack<>();
			ArrayList<Character> implementationChars = new ArrayList<Character>();
			ArrayList<Character> parametersChars = new ArrayList<Character>();

			File myObj = new File(directory + fileName);
			Scanner myReader = new Scanner(myObj);
			boolean fncFound = false, fncStarted = false;
			boolean searching = false, searchingParameters = false, parametersStarted = false;
			int currentDistributedFunctionIndex = -1;

			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				if (line.indexOf("@distributed") > -1) {
					searching = true;
				}
				if (searching) {
					for (int d = 0; d < dfs.size(); d++) {
						if (line.indexOf(dfs.get(d).FunctionName + "(") > -1
								|| line.indexOf(dfs.get(d).FunctionName + " ") > -1
								|| line.indexOf(dfs.get(d).FunctionName + "\n") > -1
								|| line.indexOf(dfs.get(d).FunctionName + "\t") > -1) {

							int indexOf = line.indexOf(dfs.get(d).FunctionName);
							if (indexOf == 0 || line.charAt(indexOf - 1) == ' ' || line.charAt(indexOf - 1) == '\n'
									|| line.charAt(indexOf - 1) == '\t') {
								searching = false;
								searchingParameters = true;
								currentDistributedFunctionIndex = d;
							}
						}
					}
				}

				if (searchingParameters) {
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == '(') {
							brackets.push('(');
							parametersStarted = true;
						} else if (line.charAt(i) == ')') {
							brackets.pop();
						}

						if (parametersStarted && !brackets.isEmpty()) {
							parametersChars.add(line.charAt(i));
						}

						if (brackets.isEmpty() && parametersStarted) {
							parametersChars.add(')');
							fncFound = true;
							searchingParameters = false;
							parametersStarted = false;
							break;
						}
					}
					parametersChars.add('\n');
				}

				if (fncFound) {
					for (int i = 0; i < line.length(); i++) { //
						if (line.charAt(i) == '{') {
							brackets.push('{');
							fncStarted = true;
						} else if (line.charAt(i) == '}') {
							brackets.pop();
						}

						if (fncStarted && !brackets.isEmpty()) {
							implementationChars.add(line.charAt(i));
						}

						if (brackets.isEmpty() && fncStarted) {
							implementationChars.add('}');
							fncStarted = false;
							fncFound = false;
							searching = false;

							// CHAR[] -> STRING -> STRING[] -> ARRAYLIST<STRING>
							dfs.get(currentDistributedFunctionIndex).Params = getStringRepresentation(parametersChars);
							Collections.addAll(dfs.get(currentDistributedFunctionIndex).Implementation,
									getStringRepresentation(implementationChars).split("\\r?\\n"));

							currentDistributedFunctionIndex = -1;
							implementationChars = new ArrayList<Character>();
							parametersChars = new ArrayList<Character>();
							break;
						}
					}
					implementationChars.add('\n');
				}
			}
			myReader.close();

			for (int i = 0; i < parametersChars.size(); i++) {
				System.out.print(parametersChars.get(i));
			}

			for (int i = 0; i < implementationChars.size(); i++) {
				System.out.print(implementationChars.get(i));
			}
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return dfs;
	}

	public ArrayList<RegularFunction> getRegularFunctions(String className) {
		ArrayList<RegularFunction> regs = new ArrayList<RegularFunction>();
		try {
			// create class object
			Class classobj = Class.forName(className);

			// get list of methods
			Method[] methods = classobj.getMethods();

			// get the name of every method present in the list
			for (Method method : methods) {

				String MethodName = method.getName();
				Annotation[] ants = method.getAnnotations();
				boolean distributedFound = false;
				for (Annotation ant : ants) {
					if (ant.toString().equals("@distributed()")) {
						distributedFound = true;
					}
				}

				if (!distributedFound) {
					RegularFunction rf = new RegularFunction();
					rf.ClassName = className;
					rf.FunctionName = MethodName;
					rf.ReturnType = method.getReturnType().getName();

					for (int i = 0; i < method.getExceptionTypes().length; i++) {
						String var = method.getExceptionTypes()[i].toString();
						String[] array = var.split("\\.");
						rf.Throwables.add(array[array.length - 1]);
					}
					regs.add(rf);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 9; i++) {
			regs.remove(regs.size() - 1);
		}

		return regs;
	}

	public ArrayList<DistributedFunction> getDistributedFunctions(String className) {
		ArrayList<DistributedFunction> distributedFunctions = new ArrayList<DistributedFunction>();
		try {
			// create class object
			Class classobj = Class.forName(className);

			// get list of methods
			Method[] methods = classobj.getMethods();

			// get the name of every method present in the list
			for (Method method : methods) {

				String MethodName = method.getName();
				Annotation[] ants = method.getAnnotations();
				for (Annotation ant : ants) {
					if (ant.toString().equals("@distributed()")) {

						DistributedFunction df = new DistributedFunction();
						df.ClassName = className;
						df.FunctionName = MethodName;
						df.ReturnType = method.getReturnType().getName();
						for (int i = 0; i < method.getExceptionTypes().length; i++) {
							String var = method.getExceptionTypes()[i].toString();
							String[] array = var.split("\\.");
							df.Throwables.add(array[array.length - 1]);

						}
						// df.Throwables=method.getExceptionTypes();
						distributedFunctions.add(df);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return distributedFunctions;
	}

	String getStringRepresentation(ArrayList<Character> list) {
		StringBuilder builder = new StringBuilder(list.size());
		for (Character ch : list) {
			builder.append(ch);
		}
		return builder.toString();
	}

	public Field[] turningtheobject(String className) throws SecurityException, ClassNotFoundException { // Obje bulmak
																											// i�in
																											// gerekli
																											// olan kod
		Field[] fd = Class.forName(className).getDeclaredFields();
		for (int i = 0; i < fd.length; i++) {
			System.out.println(fd[i].getName());
		}
		return fd;
	}

}
