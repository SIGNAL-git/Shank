package shank.parserNodes;
public class ParameterNode extends Node {

	private VariableReferenceNode varParameter;
	private Node parameter;
	
	public ParameterNode(VariableReferenceNode varParameter)
	{
		this.varParameter = varParameter;
		this.parameter = null;
	}
	
	public ParameterNode(Node parameter)
	{
		this.varParameter = null;
		this.parameter = parameter;
	}
	
	public String toString()
	{
		if (varParameter != null)
		{
			return "ParameterNode(" + varParameter + ")";
		}
		
		return "ParameterNode(" + parameter + ")";
	}
	
}
