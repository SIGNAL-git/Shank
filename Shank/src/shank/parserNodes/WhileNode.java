package shank.parserNodes;
import java.util.ArrayList;

public class WhileNode extends StatementNode {

	private BooleanCompareNode condition;
	private ArrayList<StatementNode> statements = new ArrayList<StatementNode>();

	public WhileNode(BooleanCompareNode condition)
	{
		this.condition = condition;
	}
	
	public BooleanCompareNode getCondition()
	{
		return condition;
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
		return "\nWhileNode(" + condition + ")" + "\nStatements: " + statements;
	}
	
}
