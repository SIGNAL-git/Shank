package shank.parserNodes;

public class IntegerNode extends Node {

	private int value = 0;
	
	public IntegerNode() {} // Declared variable (No value yet)
	
	public IntegerNode(int value)
	{
		this.value = value;
	}
	
	public IntegerNode(String value)
	{
		this.value = Integer.parseInt(value);
	}
	
	public int getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return "IntegerNode(" + value + ")";
	}
}
