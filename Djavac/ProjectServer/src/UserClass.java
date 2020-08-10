import java.io.Serializable;
import java.util.ArrayList;

public class UserClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ClassName = "";
	protected ArrayList<String> Imports = new ArrayList<String>();
	private ArrayList<DistributedFunction> DistributedFunctions = new ArrayList<DistributedFunction>();
	private ArrayList<RegularFunction> RegularFunctions = new ArrayList<RegularFunction>();

	private ArrayList<String> Fields = new ArrayList<String>();

	public UserClass() {
		// TODO Auto-generated constructor stub
	}

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public ArrayList<String> getImports() {
		return Imports;
	}

	public void setImports(ArrayList<String> imports) {
		Imports = imports;
	}

	public ArrayList<DistributedFunction> getDistributedFunctions() {
		return DistributedFunctions;
	}

	public void setDistributedFunctions(ArrayList<DistributedFunction> distributedFunctions) {
		DistributedFunctions = distributedFunctions;
	}

	public ArrayList<RegularFunction> getRegularFunctions() {
		return RegularFunctions;
	}

	public void setRegularFunctions(ArrayList<RegularFunction> regularFunctions) {
		RegularFunctions = regularFunctions;
	}

	public ArrayList<String> getFields() {
		return Fields;
	}

	public void setFields(ArrayList<String> fields) {
		Fields = fields;
	}

}
