package shank.interpreterDataTypes;

import shank.parserNodes.FloatNode;

public class RealDataType extends InterpreterDataType {
	
	private float realData;
	
	public RealDataType(float realData)
	{
		this.realData = realData;
	}
	
	public RealDataType(FloatNode realData)
	{
		this.realData = realData.getValue();
	}
	
	public float getValue()
	{
		return realData;
	}
	
	public String toString()
	{
		return String.valueOf(realData);
	}
	
	public void fromString(String realData)
	{
		this.realData = Float.parseFloat(realData);
	}
	
}