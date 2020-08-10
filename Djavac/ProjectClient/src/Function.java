import java.io.Serializable;
import java.util.ArrayList;

public class Function implements Serializable {
	protected String FunctionName = "";
	protected String ReturnType = "";
	protected String Params = "";
	protected String ClassName = "";
	protected ArrayList<String> Implementation = new ArrayList<String>();
	protected ArrayList<String> Throwables = new ArrayList<String>();

	public Function() {
		// TODO Auto-generated constructor stub
	}

	public void setName(String name) {
		this.FunctionName = name;
	}

	public void setReturnType(String type) {
		this.ReturnType = type;
	}

	public String getFunctionName() {
		return FunctionName;
	}

	public String getReturnType() {
		return ReturnType;
	}

	public String getParams() {
		return Params.replaceAll("\n", "");
	}

	public String getParamsWithoutTypes() {
//		(int x, int y)
		String params = Params.replaceAll("\n", "");
		params = params.replaceAll("[()]", "");
		params = params.trim();

		String retValue = "";

		for (int i = 0; i < params.length(); i++) {
			char curr = params.charAt(i);
			if (curr == ' ') {
				if (params.contains(",")) {
					int comma = params.indexOf(",");
					retValue += params.substring(i, comma + 1);
					i = 0;
					params = params.substring(comma + 1, params.length());
					params = params.trim();
				} else {
					retValue += params.substring(i, params.length());
				}
			}
		}

		return "(" + retValue + ")";
	}

	public String getClassName() {
		return ClassName;
	}

	public ArrayList<String> getImplementation() {
		return Implementation;
	}

	public void setParams(String params) {
		this.Params = params;
	}

	public void setImplementation(ArrayList<String> Implementation) {
		this.Implementation = Implementation;
	}
}
