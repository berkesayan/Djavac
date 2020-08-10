import java.util.ArrayList;

public class RegularFunction extends Function {

	public RegularFunction() {
		// TODO Auto-generated constructor stub
	}
	
	public RegularFunction(String FunctionName, String ReturnType, String Params, ArrayList<String> Implementation,
			String ClassName) {
		this.FunctionName = FunctionName;
		this.ReturnType = ReturnType;
		this.Params = Params;
		this.Implementation = Implementation;
		this.ClassName = ClassName;
	}
}
