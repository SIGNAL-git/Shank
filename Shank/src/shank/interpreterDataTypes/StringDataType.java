package shank.interpreterDataTypes;

import shank.parserNodes.StringNode;

public class StringDataType extends InterpreterDataType {
	
	private String stringData;
	
	public StringDataType(String stringData)
	{
		this.stringData = stringData;
	}
	
	public StringDataType(StringNode stringData)
	{
		this.stringData = stringData.getValue();
	}
	
	public String getValue()
	{
		return stringData;
	}
	
	public String toString()
	{
		return stringData;
	}
	
	public void fromString(String stringData)
	{
		this.stringData = stringData;
	}
	
}