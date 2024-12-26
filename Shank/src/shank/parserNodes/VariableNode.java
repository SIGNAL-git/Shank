package shank.parserNodes;

public class VariableNode extends Node {

	public enum VariableType
	{
		Integer,
		Real,
		Boolean,
		Character,
		String
	}
	
	boolean isChangeable;
	
	private String name;
	private VariableType type;
	private Node value;
	
	private boolean isArray;
	private boolean isBounded;
	private int from;
	private int to;
	private float realFrom;
	private float realTo;
	
	/**
	 * Default, names are always known when declared
	 * Assume variable unless specified otherwise
	 */
	public VariableNode(String name)
	{
		this.name = name;
		this.type = null;
		this.isChangeable = true;
		this.isArray = false;
		this.isBounded = false;
	}
	
	public VariableNode(String name, boolean isChangeable)
	{
		this.name = name;
		this.type = null;
		this.isChangeable = isChangeable;
		this.isArray = false;
		this.isBounded = false;
	}
	
	public void setType(VariableNode.VariableType type)
	{
		this.type = type;
	}
	
	public void setValue(Node value)
	{
		this.value = value;
	}
	
	public void setArray()
	{
		isArray = true;
	}
	
	public void setArray(int from, int to)
	{
		isArray = true;
		this.from = from;
		this.to = to;
	}
	
	public void setBounds(int from, int to)
	{
		isBounded = true;
		this.from = from;
		this.to = to;
	}
	
	public void setBounds(float realFrom, float realTo)
	{
		isBounded = true;
		this.realFrom = realFrom;
		this.realTo = realTo;
	}
	
	public VariableNode.VariableType getType()
	{
		return type;
	}
	
	public Node getValue()
	{
		return value;
	}
	
	public String getName()
	{
		return name;
	}
	
	// Declared variables initially have no value
	public String toString()
	{
		if (isArray)
		{
			return "\nVariableNode(" + name + " of type " + type.toString() + " from " + from + " to " + to + ")";
		}
		if (isBounded && from != to)
		{
			return "\nVariableNode(" + name + " of type " + type.toString() + ", bounded from " + from + " to " + to + ")";
		}
		if (isBounded && realFrom != realTo)
		{
			return "\nVariableNode(" + name + " of type " + type.toString() + ", bounded from " + realFrom + " to " + realTo + ")";
		}
		return "\nVariableNode(" + name + " of type " + type.toString() + ")";
	}
	
}
