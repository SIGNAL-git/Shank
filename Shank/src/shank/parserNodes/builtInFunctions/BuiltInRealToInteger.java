package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInRealToInteger extends FunctionNode {

	public BuiltInRealToInteger(String name)
	{
		super(name);
	}

	/**
	 * Takes in a real and assigns it as an integer to an IntegerDataType var 
	 * @throws SyntaxErrorException on invalid parameters
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		float inputNumber;
		int outputNumber;
		
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
		if (!(parameters.get(1) instanceof IntegerDataType))
		{
			throw new SyntaxErrorException("Expected IntegerDataType for second parameter for function " + getClass().getName());
		}
		
		inputNumber = Float.parseFloat(parameters.get(0).toString());
		outputNumber = (int) inputNumber;
		
		parameters.get(2).fromString(String.valueOf(outputNumber));
	}
	
}
