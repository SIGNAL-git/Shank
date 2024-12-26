package shank.parserNodes;

import java.util.ArrayList;

public class FunctionNode extends Node {

	protected String name;
	protected ArrayList<VariableNode> parameters = new ArrayList<VariableNode>();
	protected ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
	protected ArrayList<VariableNode> variables = new ArrayList<VariableNode>();
	protected boolean isVariadic;
	
	public FunctionNode(String name)
	{
		this.name = name;
		isVariadic = false;
	}
	
	public void setParameters(ArrayList<VariableNode> parameters)
	{
		this.parameters = parameters;
	}
	
	public void addStatements(ArrayList<StatementNode> statements)
	{
		if (statements != null) // Avoid NullPointerExceptions
		{
			this.statements.addAll(statements);
		}
	}
	
	public void addVariables(ArrayList<VariableNode> variables)
	{
		if (variables != null) // Avoid NullPointerExceptions
		{
			this.variables.addAll(variables);
		}
	}
	
	public void addVariables(VariableNode variables)
	{
		this.variables.add(variables);
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<VariableNode> getVariables()
	{
		return variables;
	}
	
	public ArrayList<StatementNode> getStatements()
	{
		return statements;
	}
	
	public String toString()
	{
		return "\nFunctionNode: " + name + " \n{\nIs Variadic?: " + isVariadic + "\nParameters: " + parameters + "\nVariables: " + variables + "\nStatements: " + statements + "\n}\n";
	}
}
