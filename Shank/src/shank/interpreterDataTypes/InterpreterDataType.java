package shank.interpreterDataTypes;

import shank.SyntaxErrorException;

public abstract class InterpreterDataType {
	
	public abstract String toString();
	
	public abstract void fromString(String input) throws SyntaxErrorException;
	
}
