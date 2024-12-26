package shank.parserNodes;

public class VariableReferenceNode extends Node {

	String name;
	Node arrayIndex;
	
	/**
	 * Variable references are always known by name
	 */
	public VariableReferenceNode(String name)
	{
		this.name = name;
	}
	
	public void setArrayIndex(Node arrayIndex)
	{
		this.arrayIndex = arrayIndex;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		if (arrayIndex != null)
		{
			return "VariableReferenceNode(" + name + "[" +  arrayIndex.toString() + "])";
		}
		return "VariableReferenceNode(" + name + ")";
	}
	
}
