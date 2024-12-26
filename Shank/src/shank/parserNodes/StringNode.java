package shank.parserNodes;
public class StringNode extends Node {

	private String value;
	
	public StringNode() // Declared variable
	{
		this.value = null;
	}
	
	public StringNode(String value) // Declared constant
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return "StringNode(" + value + ")";
	}
}
