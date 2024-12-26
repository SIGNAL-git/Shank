package shank.parserNodes;
import java.util.ArrayList;

public class ForNode extends StatementNode {

	private VariableReferenceNode iterator;
	private Node from;
	private Node to;
	private ArrayList<StatementNode> statements = new ArrayList<StatementNode>();

	public ForNode(VariableReferenceNode iterator, Node from, Node to)
	{
		this.iterator = iterator;
		this.from = from;
		this.to = to;
	}
	
	public Node getFrom()
	{
		return from;
	}
	
	public Node getTo()
	{
		return to;
	}
	
	public VariableReferenceNode getIterator()
	{
		return iterator;
	}
	
	public ArrayList<StatementNode> getStatements()
	{
		return statements;
	}
	
	public void addStatements(ArrayList<StatementNode> statements)
	{
		if (statements != null) // Avoid NullPointerExceptions
		{
			this.statements.addAll(statements);
		}
	}
	
	public String toString()
	{
		return "\nForNode(For " + iterator + " from " + from + " to " + to + "){\nStatements: " + statements.toString();
	}
}
