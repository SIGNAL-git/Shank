package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInLeft extends FunctionNode {

	public BuiltInLeft(String name)
	{
		super(name);
	}

	/**
	 * Assigns the first X characters of a string to a StringDataType var
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		String someString;
		int length;
		String resultString;
		
		if (parameters.size() != 3)
		{
			if (parameters.size() < 3)
			{
				throw new SyntaxErrorException("Insufficient arguments given for function " + getClass().getName());
			}
			if (parameters.size() > 3)
			{
				throw new SyntaxErrorException("Too many arguments given for function " + getClass().getName());
			}
		}
		
		if (!(parameters.get(0) instanceof StringDataType))
		{
			throw new SyntaxErrorException("Expected StringDataType for first parameter for function " + getClass().getName());
		}
		if (!(parameters.get(1) instanceof IntegerDataType))
		{
			throw new SyntaxErrorException("Expected IntegerDataType for second parameter for function " + getClass().getName());
		}
		if (!(parameters.get(2) instanceof StringDataType))
		{
			throw new SyntaxErrorException("Expected StringDataType for third parameter for function " + getClass().getName());
		}
		
		someString = parameters.get(0).toString();
		length = Integer.parseInt(parameters.get(1).toString());
		
		resultString = someString.substring(0, length);
		parameters.get(2).fromString(resultString);
	}
}
