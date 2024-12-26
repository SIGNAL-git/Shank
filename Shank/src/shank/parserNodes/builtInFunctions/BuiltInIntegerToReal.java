package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInIntegerToReal extends FunctionNode {

	public BuiltInIntegerToReal(String name)
	{
		super(name);
	}

	/**
	 * Takes in an integer and assigns it as a real to a RealDataType var 
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		int inputNumber;
		float outputNumber;
		
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
		
		if (!(parameters.get(0) instanceof IntegerDataType))
		{
			throw new SyntaxErrorException("Expected IntegerDataType for first parameter for function " + getClass().getName());
		}
		if (!(parameters.get(1) instanceof RealDataType))
		{
			throw new SyntaxErrorException("Expected RealDataType for second parameter for function " + getClass().getName());
		}
		
		inputNumber = Integer.parseInt(parameters.get(0).toString());
		outputNumber = inputNumber;
		
		parameters.get(2).fromString(String.valueOf(outputNumber));
	}
}
