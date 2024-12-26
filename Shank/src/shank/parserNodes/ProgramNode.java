package shank.parserNodes;
import java.util.HashMap;

import shank.parserNodes.builtInFunctions.*;

public class ProgramNode extends Node {

	private HashMap<String, FunctionNode> hash = new HashMap<String, FunctionNode>();
	
	public void addFunction(FunctionNode function)
	{
		if (hash.get(function.getName()) == null)
		{
			hash.put(function.getName(), function);
		}
	}
	
	public void addBuiltInFunctions()
	{
		hash.put("Write", new BuiltInWrite("Write"));
		hash.put("Read", new BuiltInRead("Read"));
		hash.put("Left", new BuiltInLeft("Left"));
		hash.put("Right", new BuiltInRight("Right"));
		hash.put("Substring", new BuiltInSubstring("Substring"));
		hash.put("SquareRoot", new BuiltInSquareRoot("SquareRoot"));
		hash.put("GetRandom", new BuiltInGetRandom("GetRandom"));
		hash.put("IntegerToReal", new BuiltInIntegerToReal("IntegerToReal"));
		hash.put("RealToInteger", new BuiltInRealToInteger("RealToInteger"));
		hash.put("Start", new BuiltInStart("Start"));
		hash.put("End", new BuiltInEnd("End"));
	}
	
	public FunctionNode getFunction(String functionName)
	{
		return hash.get(functionName);
	}
	
	public int numFunctions()
	{
		return hash.size();
	}
	
	public String toString()
	{
		return hash.toString();
	}
	
}
