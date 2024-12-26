package shank.interpreterDataTypes;

import shank.parserNodes.IntegerNode;

public class IntegerDataType extends InterpreterDataType {
	
	private int integerData;
	
	public IntegerDataType(int integerData)
	{
		this.integerData = integerData;
	}
	
	public IntegerDataType(IntegerNode integerData)
	{
		this.integerData = integerData.getValue();
	}
	
	public int getValue()
	{
		return integerData;
	}
	
	public void setValue(int integerData)
	{
		this.integerData = integerData;
	}
	
	public String toString()
	{
		return String.valueOf(integerData);
	}
	
	public void fromString(String integerData)
	{
		this.integerData = Integer.parseInt(integerData);
	}
	
}
