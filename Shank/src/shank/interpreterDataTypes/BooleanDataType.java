package shank.interpreterDataTypes;

import shank.parserNodes.BooleanNode;

public class BooleanDataType extends InterpreterDataType {
	
	private boolean booleanData;
	
	public BooleanDataType(boolean booleanData)
	{
		this.booleanData = booleanData;
	}
	
	public BooleanDataType(BooleanNode booleanData)
	{
		this.booleanData = booleanData.getValue();
	}
	
	public boolean getValue()
	{
		return booleanData;
	}
	
	public void setValue(boolean booleanData)
	{
		this.booleanData = booleanData;
	}
	
	public String toString()
	{
		return String.valueOf(booleanData);
	}
	
	public void fromString(String booleanData)
	{
		this.booleanData = Boolean.parseBoolean(booleanData);
	}
	
}