package shank.parserNodes;
public class MathOpNode extends Node {

	public enum MathOps
	{
		add,
		subtract,
		multiply,
		divide,
		mod
	}
	
	private MathOps operator = null;
	private Node left = null;
	private Node right = null;
	
	public MathOpNode(MathOps operator)
	{
		this.operator = operator;
	}
	
	public MathOpNode(MathOps operator, Node left, Node right)
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
	
	public MathOps getOperator()
	{
		return operator;
	}
	
	public String toString()
	{
		return "MathOpNode(" + left.toString() + " " + operator + " " + right.toString() + ")";
	}
	
}