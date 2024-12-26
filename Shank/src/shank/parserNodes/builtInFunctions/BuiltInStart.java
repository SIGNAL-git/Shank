package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInStart extends FunctionNode {

	public BuiltInStart(String name)
	{
		super(name);
	}

	/**
	 * Assigns the position of the first index of an array to an IntegerDataType var
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		ArrayDataType<?> arrayInfo;
		int arrayStartIndex;
		
		if (parameters.size() != 2)
		{
			if (parameters.size() < 2)
			{
				throw new SyntaxErrorException("Insufficient arguments given for function " + getClass().getName());
			}
			if (parameters.size() > 2)
			{
				throw new SyntaxErrorException("Too many arguments given for function " + getClass().getName());
			}
		}
		
		if (!(parameters.get(0) instanceof ArrayDataType))
		{
			throw new SyntaxErrorException("Expected ArrayDataType for first parameter for function " + getClass().getName());
		}
		if (!(parameters.get(1) instanceof IntegerDataType))
		{
			throw new SyntaxErrorException("Expected IntegerDataType for second parameter for function " + getClass().getName());
		}
		
		arrayInfo = (ArrayDataType<?>) parameters.get(0);
		
		arrayStartIndex = arrayInfo.getFrom();
		parameters.get(1).fromString(String.valueOf(arrayStartIndex));
	}
}
