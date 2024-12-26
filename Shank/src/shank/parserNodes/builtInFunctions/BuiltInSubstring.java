package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInSubstring extends FunctionNode {

	public BuiltInSubstring(String name)
	{
		super(name);
	}

	/**
	 * Assigns the first X characters of a string starting from some index to a StringDataType var
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		String someString;
		int index;
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
			throw new SyntaxErrorException("Expected IntegerDataType for third parameter for function " + getClass().getName());
		}
		if (!(parameters.get(3) instanceof IntegerDataType))
		{
			throw new SyntaxErrorException("Expected StringDataType for fourth parameter for function " + getClass().getName());
		}
		
		someString = parameters.get(0).toString();
		index = Integer.parseInt(parameters.get(1).toString());
		length = Integer.parseInt(parameters.get(2).toString());
		
		resultString = someString.substring(index, length);
		parameters.get(2).fromString(resultString);
	}
}
