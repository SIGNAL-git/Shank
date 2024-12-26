package shank.parserNodes;
import java.util.ArrayList;

public class FunctionCallNode extends StatementNode {
	
	private String name;
	private ArrayList<ParameterNode> parameters;
	
	public FunctionCallNode(String name)
	{
		this.name = name;
		parameters = new ArrayList<ParameterNode>();
	}
	
	public void addParameter(ParameterNode parameter)
	{
		parameters.add(parameter);
	}
	
	public String toString()
	{
		return "\nFunctionCallNode(" + name + ", Parameters: " + parameters + ")";
	}
	
}
