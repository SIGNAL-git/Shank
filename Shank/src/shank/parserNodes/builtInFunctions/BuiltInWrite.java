package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInWrite extends FunctionNode {
	
	public BuiltInWrite(String name)
	{
		super(name);
		isVariadic = true;
	}
	
	/**
	 * Prints out the parameters to the console
	 * @throws SyntaxErrorException on a lack of parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		if (parameters.size() == 0)
		{
			throw new SyntaxErrorException("No arguments given for function " + getClass().getName());
		}
		
		for (int i = 0; i < parameters.size(); i++)
		{
			System.out.print(parameters.get(i).toString());
		}
	}
}