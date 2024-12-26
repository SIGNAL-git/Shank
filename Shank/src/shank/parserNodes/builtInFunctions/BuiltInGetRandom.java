package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;
import java.util.Random;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInGetRandom extends FunctionNode {

	public BuiltInGetRandom(String name)
	{
		super(name);
	}

	/**
	 * Assigns an IntegerDataType var a random integer
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		Random rng = new Random();
		
		if (parameters.size() != 1)
		{
			if (parameters.size() == 0)
			{
				throw new SyntaxErrorException("No arguments given for function " + getClass().getName());
			}
			if (parameters.size() > 1)
			{
				throw new SyntaxErrorException("Too many arguments given for function " + getClass().getName());
			}
		}
		
		if (!(parameters.get(0) instanceof IntegerDataType))
		{
			throw new SyntaxErrorException("Expected IntegerDataType for parameter for function " + getClass().getName());
		}
		
		parameters.get(0).fromString(String.valueOf(rng.nextInt()));
	}
}
