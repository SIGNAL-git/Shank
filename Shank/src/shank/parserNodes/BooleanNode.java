package shank.parserNodes;

public class BooleanNode extends Node {

	private boolean value;
	
	public BooleanNode() {} // Declared variable (No value yet)

	public BooleanNode(boolean value) // Declared constant
	{
		this.value = value;
	}
	
	public BooleanNode(String value) // Declared constant
	{
		this.value = Boolean.parseBoolean(value);
	}
	
	public boolean getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return "BooleanNode(" + value + ")";
	}
	
}
