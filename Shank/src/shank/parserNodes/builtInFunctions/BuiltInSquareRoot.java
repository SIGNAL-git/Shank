package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInSquareRoot extends FunctionNode {

	public BuiltInSquareRoot(String name)
	{
		super(name);
	}

	/**
	 * Calculates the square root of a number and assigns it to a RealDataType var
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		float someFloat;
		float result;
		
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
		
		if (!(parameters.get(0) instanceof RealDataType))
		{
			throw new SyntaxErrorException("Expected RealDataType for first parameter for function " + getClass().getName());
		}
		if (!(parameters.get(1) instanceof RealDataType))
		{
			throw new SyntaxErrorException("Expected RealDataType for second parameter for function " + getClass().getName());
		}
		
		someFloat = Float.parseFloat(parameters.get(0).toString());
		
		result = (float) Math.sqrt(someFloat);
		parameters.get(1).fromString(String.valueOf(result));
	}
}
