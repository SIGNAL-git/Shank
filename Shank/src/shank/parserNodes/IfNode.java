package shank.parserNodes;
import java.util.ArrayList;

public class IfNode extends StatementNode {

	private BooleanCompareNode condition;
	private ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
	private IfNode elseNode;
	
	public IfNode(BooleanCompareNode condition)
	{
		this.condition = condition;
		this.elseNode = null;
	}
	
	public IfNode() // Essentially an else block
	{
		this.condition = null;
		this.elseNode = null;
	}
	
	public void addStatements(ArrayList<StatementNode> statements)
	{
		if (statements != null) // Avoid NullPointerExceptions
		{
			this.statements.addAll(statements);
		}
	}
	
	public void setElse(IfNode elseNode)
	{
		this.elseNode = elseNode;
	}
	
	public IfNode getElse()
	{
		return elseNode;
	}
	
	public BooleanCompareNode getCondition()
	{
		return condition;
	}
	
	public ArrayList<StatementNode> getStatements()
	{
		return statements;
	}
	
	public String toString()
	{
		return "\nIfNode(" + condition + ")" + "\nStatements: " + statements + "\nElse(" + elseNode + ")";
	}
	
}
