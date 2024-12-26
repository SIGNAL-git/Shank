package shank.parserNodes;

public class CharacterNode extends Node {

	private char value;
	
	public CharacterNode() {} // Declared variable (No value yet)
	
	public CharacterNode(char value) // Declared constant
	{
		this.value = value;
	}
	
	public CharacterNode(String value) // Declared constant
	{
		this.value = value.charAt(0);
	}
	
	public char getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return "CharacterNode(" + value + ")";
	}
}
