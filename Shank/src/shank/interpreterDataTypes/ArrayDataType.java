package shank.interpreterDataTypes;

import java.util.ArrayList;

import shank.SyntaxErrorException;

public class ArrayDataType<T extends InterpreterDataType> extends InterpreterDataType {
	
	// Where T is the type of InterpreterDataType the array holds
	private ArrayList<T> arrayContents;
	private int from;
	private int to;
	
	public ArrayDataType(int from, int to) throws SyntaxErrorException
	{
		if (to < from)
		{
			throw new SyntaxErrorException("Invalid array bounds");
		}
		
		this.from = from;
		this.to = to;
		this.arrayContents = new ArrayList<T>(this.to);
		
		for (int i = 0; i < this.from; i++)
		{
			this.arrayContents.get(i).fromString(null);
		}
	}
	
	public int getFrom()
	{
		return from;
	}
	
	public int getTo()
	{
		return to;
	}
	
	public String toString()
	{
		return arrayContents.toString();
	}
	
	public void fromString(String arrayData) throws SyntaxErrorException
	{
		/* 
		 * Left blank, as users shouldn't alter an entire array at once,
		 * instead requiring each individual element to be altered
	     */
	}
	
}