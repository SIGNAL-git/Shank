package shank.parserNodes;

public class FloatNode extends Node {

	private float value = 0.0f;
	
	public FloatNode() {} // Declared variable (No value yet)
	
	public FloatNode(float value) // Declared constant
	{
		this.value = value;
	}
	
	public FloatNode(String value) // Declared constant
	{
		this.value = Float.parseFloat(value);
	}
	
	public void setValue(float value)
	{
		this.value = value;
	}
	
	public float getValue()
	{
		return value;
	}
	
	public String toString()
	{
		return "RealNode(" + value + ")";
	}
	
}
