package shank.parserNodes.builtInFunctions;

import java.util.ArrayList;
import java.util.Scanner;

import shank.SyntaxErrorException;
import shank.interpreterDataTypes.*;
import shank.parserNodes.FunctionNode;

public class BuiltInRead extends FunctionNode {
	
	Scanner input = new Scanner(System.in);
	
	public BuiltInRead(String name)
	{
		super(name);
		isVariadic = true;
	}
	
	/**
	 * Takes input from the user, delimited by spaces, and assigns the input to one or more var variables
	 * @throws SyntaxErrorException if number of inputs does not match the number of targets
	 */
	public void execute(ArrayList<InterpreterDataType> parameters) throws SyntaxErrorException
	{
		if (parameters.size() == 0)
		{
			throw new SyntaxErrorException("No arguments given for function " + getClass().getName());
		}
		
		String[] inputValues = input.next().split(" ");
		
		if (parameters.size() != inputValues.length)
		{
			if (parameters.size() < inputValues.length)
			{
				throw new SyntaxErrorException("Too many inputs given during read");
			}
			if (parameters.size() > inputValues.length)
			{
				throw new SyntaxErrorException("Insufficient inputs given during read");
			}
		}
		
		for (int i = 0; i < inputValues.length; i++)
		{
			parameters.get(i).fromString(inputValues[i]);
		}
	}
}