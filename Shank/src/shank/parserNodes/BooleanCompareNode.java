package shank.parserNodes;

public class BooleanCompareNode extends Node {
	
	public enum BooleanOps
	{
		GreaterThan,
		LessThan,
		GreaterThanOrEquals,
		LessThanOrEquals,
		Equals,
		NotEquals
	}
	
	private BooleanOps operator = null;
	private Node left = null;
	private Node right = null;
	
	public BooleanCompareNode(BooleanOps operator)
	{
		this.operator = operator;
	}
	
	public BooleanCompareNode(BooleanOps operator, Node left, Node right)
	{
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(Node left)
	{
		this.left = left;
	}
	
	public void setRight(Node right)
	{
		this.right = right;
	}
	
	public Node getLeft()
	{
		return left;
	}
	
	public Node getRight()
	{
		return right;
	}
	
	public BooleanOps getOperator()
	{
		return operator;
	}
	
	public String toString()
	{
		return "BooleanCompareNode(" + left.toString() + " " + operator + " " + right.toString() + ")";
	}
}
