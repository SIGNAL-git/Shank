package shank.parserNodes;

public class AssignmentNode extends StatementNode {

	private VariableReferenceNode targetVariable;
	private Node targetValue;
	
	public AssignmentNode(VariableReferenceNode targetVariable, Node targetValue)
	{
		this.targetVariable = targetVariable;
		this.targetValue = targetValue;
	}
	
	public VariableReferenceNode getTarget()
	{
		return targetVariable;
	}
	
	public Node getTargetValue()
	{
		return targetValue;
	}
	
	public String toString()
	{
		// New line character to make it easier to read when printing multiple nodes
		return "\nAssignmentNode(" + targetVariable + " := " + targetValue + ")";
	}
}
