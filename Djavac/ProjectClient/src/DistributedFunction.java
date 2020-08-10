import java.util.ArrayList;

public class DistributedFunction extends Function {

	public DistributedFunction() {

	}

	public DistributedFunction(String FunctionName, String ReturnType, String Params, ArrayList<String> Implementation,
			String ClassName) {
		this.FunctionName = FunctionName;
		this.ReturnType = ReturnType;
		this.Params = Params;
		this.Implementation = Implementation;
		this.ClassName = ClassName;
	}
}
