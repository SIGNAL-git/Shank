package shank;

import java.util.ArrayList;
import java.util.HashMap;

import shank.interpreterDataTypes.*;
import shank.parserNodes.*;

public class Interpreter
{
	private ProgramNode program;
	
	public Interpreter(ProgramNode program)
	{
		this.program = program;
	}
	
	/**
	 * Interprets a program by first interpreting the function "start"
	 * @throws SyntaxErrorException If the start function doesn't exist
	 */
	public void interpretProgram() throws SyntaxErrorException
	{
		System.out.println();
		
		if (program.getFunction("start") == null) // Program always runs start
		{
			throw new SyntaxErrorException("start function not found");
		}
		
		interpretFunction(program.getFunction("start"));
	}
	
	/**
	 * Interprets a function by adding the variables and statements to a list,
	 * then calling interpretBlock on the list of statements, passing the variables
	 * @param function The function being interpreted
	 */
	public void interpretFunction(FunctionNode function) throws SyntaxErrorException
	{	
		HashMap<String, InterpreterDataType> variables = new HashMap<String, InterpreterDataType>();
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		
		ArrayList<VariableNode> variableList;
		VariableNode currentVariable;
		
		variableList = function.getVariables();
		for (int i = 0; i < variableList.size(); i++)
		{
			currentVariable = variableList.get(i);
			switch (currentVariable.getType())
			{
				case Integer:
					variables.put(currentVariable.getName(), new IntegerDataType((IntegerNode) currentVariable.getValue()));
					break;
					
				case Boolean:
					variables.put(currentVariable.getName(), new BooleanDataType((BooleanNode) currentVariable.getValue()));
					break;
					
				case Character:
					variables.put(currentVariable.getName(), new CharacterDataType((CharacterNode) currentVariable.getValue()));
					break;
					
				case Real:
					variables.put(currentVariable.getName(), new RealDataType((FloatNode) currentVariable.getValue()));
					break;
					
				case String:
					variables.put(currentVariable.getName(), new StringDataType((StringNode) currentVariable.getValue()));
					break;
					
				default:
					break;
			}
		}
		
		statements = function.getStatements();
		
		System.out.println("Current variables");
		System.out.println(variables);
		
		interpretBlock(statements, variables);
		
		System.out.println("Interpreted function");
		System.out.println(variables);
	}
	
	/**
	 * Interprets a block of statements by calling functions based on the node type
	 * @param statements The block of statements to be interpreted
	 * @param variables The HashMap containing the variables and their associated values
	 * @throws SyntaxErrorException Placeholder: Unhandled statement is found
	 */
	private void interpretBlock(ArrayList<StatementNode> statements, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		StatementNode currentStatement;
		
		for (int i = 0; i < statements.size(); i++)
		{
			currentStatement = statements.get(i);
			if (currentStatement instanceof AssignmentNode)
			{
				AssignmentNode((AssignmentNode) currentStatement, variables);
			}
			else if (currentStatement instanceof IfNode)
			{
				IfNode((IfNode) currentStatement, variables);
			}
			else if (currentStatement instanceof WhileNode)
			{
				WhileNode((WhileNode) currentStatement, variables);
			}
			else if (currentStatement instanceof ForNode)
			{
				ForNode((ForNode) currentStatement, variables);
			}
			else if (currentStatement instanceof RepeatNode)
			{
				RepeatNode((RepeatNode) currentStatement, variables);
			}
			else if (currentStatement instanceof FunctionCallNode)
			{
				// To do in the next assignment
				FunctionCallNode((FunctionCallNode) currentStatement, variables);
			}
			
			else
			{
				throw new SyntaxErrorException("Unhandled statement");
			}
		}
	}
	
	/**
	 * Searches for a variable's value if it is referenced.
	 * If the variable exists, return its value.
	 * Otherwise, throw an exception.
	 * @param name The name of the variable being referenced
	 * @param variables The HashMap containing the variables and their associated values
	 * @return A subclass of InterpreterDataType that depends on which variable was referenced
	 * @throws SyntaxErrorException Invalid variable referenced
	 */
	private InterpreterDataType VariableReferenceNode(String name, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		if (variables.get(name) == null)
		{
			throw new SyntaxErrorException(name + " cannot be resolved to a variable");
		}
		else
		{
			return variables.get(name);
		}
	}
	
	/**
	 * Evaluates a MathOpNode and returns its value as a subclass of InterpreterDataType.
	 * Can only evaluate IntegerNode, FloatNode, and StringNode.
	 * @param mathNode The MathOpsNode being evaluated
	 * @param variables A HashMap containing the function's variables, for VariableReferenceNode() calls
	 * @return A subclass of InterpreterDataType, the type depending on the node types evaluated
	 * @throws SyntaxErrorException If the types on both sides of an operator are different, or invalid for operation
	 */
	private InterpreterDataType MathOpNode(MathOpNode mathNode, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		Node left = expression(mathNode.getLeft(), variables);
		Node right = expression(mathNode.getRight(), variables);
		Object leftValue = null;
		Object rightValue = null;
		
		if (left.getClass() != right.getClass())
		{
			throw new SyntaxErrorException("Cannot perform " + mathNode.getOperator() + " on values of different types");
		}
		
		if (left instanceof IntegerNode)
		{
			leftValue = ((IntegerNode) left).getValue();
			rightValue = ((IntegerNode) right).getValue();
		}
		else if (left instanceof FloatNode)
		{
			leftValue = ((FloatNode) left).getValue();
			rightValue = ((FloatNode) right).getValue();
		}
		else if (left instanceof StringNode)
		{
			leftValue = ((StringNode) left).getValue();
			rightValue = ((StringNode) right).getValue();
		}
		else
		{
			throw new SyntaxErrorException("Cannot perform " + mathNode.getOperator() + " on type " + left.getClass());
		}
		
		switch (mathNode.getOperator())
		{
			case add:
				if (left instanceof IntegerNode)
				{
					return new IntegerDataType((int) leftValue + (int) rightValue);
				}
				else if (left instanceof FloatNode)
				{
					return new RealDataType((float) leftValue + (float) rightValue);
				}
				else if (left instanceof StringNode)
				{
					return new StringDataType((String) leftValue + (String) rightValue);
				}
				break;
				
			case subtract:
				if (left instanceof IntegerNode)
				{
					return new IntegerDataType((int) leftValue - (int) rightValue);
				}
				else if (left instanceof FloatNode)
				{
					return new RealDataType((float) leftValue - (float) rightValue);
				}
				break;
				
			case multiply:
				if (left instanceof IntegerNode)
				{
					return new IntegerDataType((int) leftValue * (int) rightValue);
				}
				else if (left instanceof FloatNode)
				{
					return new RealDataType((float) leftValue * (float) rightValue);
				}
				break;
				
			case divide:
				if (left instanceof IntegerNode)
				{
					return new IntegerDataType((int) leftValue / (int) rightValue);
				}
				else if (left instanceof FloatNode)
				{
					return new RealDataType((float) leftValue / (float) rightValue);
				}
				break;
				
			case mod:
				if (left instanceof IntegerNode)
				{
					return new IntegerDataType((int) leftValue % (int) rightValue);
				}
				else if (left instanceof FloatNode)
				{
					return new RealDataType((float) leftValue % (float) rightValue);
				}
				break;
				
			default:
				break;
		}
		
		throw new SyntaxErrorException("Cannot perform " + mathNode.getOperator() + " on type " + left.getClass());
	}
	
	/**
	 * Evaluates a BooleanCompareNode and returns its value as a BooleanDataType
	 * @param compareNode The BooleanCompareNode being evaluated
	 * @param variables A HashMap containing the function's variables, for VariableReferenceNode() calls
	 * @return A BooleanDataType
	 * @throws SyntaxErrorException If the statement contains a type that cannot be compared
	 */
	private BooleanDataType BooleanCompareNode(BooleanCompareNode compareNode, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		Node left = expression(compareNode.getLeft(), variables);
		Node right = expression(compareNode.getRight(), variables);
		
		float leftValue;
		float rightValue;
		
		if (left.getClass() != right.getClass())
		{
			throw new SyntaxErrorException("Cannot perform boolean comparison on different types");
		}
		
		if (left instanceof IntegerNode)
		{
			leftValue = ((IntegerNode) left).getValue();
			rightValue = ((IntegerNode) right).getValue();
		}
		else if (left instanceof FloatNode)
		{
			leftValue = ((FloatNode) left).getValue();
			rightValue = ((FloatNode) right).getValue();
		}
		else
		{
			throw new SyntaxErrorException("Cannot perform boolean comparison on type " + left.getClass());
		}
		
		switch (compareNode.getOperator())
		{
			case LessThan:
				return new BooleanDataType(leftValue < rightValue);
				
			case LessThanOrEquals:
				return new BooleanDataType(leftValue <= rightValue);
				
			case GreaterThan:
				return new BooleanDataType(leftValue > rightValue);
				
			case GreaterThanOrEquals:
				return new BooleanDataType(leftValue >= rightValue);
				
			case Equals:
				return new BooleanDataType(leftValue == rightValue);
				
			case NotEquals:
				return new BooleanDataType(leftValue != rightValue);
				
			default:
				break;
		}
		
		throw new SyntaxErrorException("Cannot perform boolean comparison on type " + left.getClass());
	}
	
	/**
	 * Evaluates an expression and returns its value as a single Node.
	 * @param node The node to be evaluated
	 * @param variables A HashMap containing the function's variables, for VariableReferenceNode() calls
	 * @return A Node, the type depending on what node was read
	 * @throws SyntaxErrorException If an invalid type was used
	 */
	private Node expression(Node node, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		if (node instanceof MathOpNode)
		{
			InterpreterDataType nodeValue;
			
			nodeValue = MathOpNode((MathOpNode) node, variables);
			
			if (nodeValue instanceof IntegerDataType)
			{
				return new IntegerNode(nodeValue.toString());
			}
			else if (nodeValue instanceof RealDataType)
			{
				return new FloatNode(nodeValue.toString());
			}
			else if (nodeValue instanceof StringDataType)
			{
				return new StringNode(nodeValue.toString());
			}
		}
		else if (node instanceof VariableReferenceNode)
		{
			InterpreterDataType nodeValue;
			
			nodeValue = VariableReferenceNode(((VariableReferenceNode) node).getName(), variables);
			
			if (nodeValue instanceof IntegerDataType)
			{
				return new IntegerNode(nodeValue.toString());
			}
			else if (nodeValue instanceof RealDataType)
			{
				return new FloatNode(nodeValue.toString());
			}
			else if (nodeValue instanceof StringDataType)
			{
				return new StringNode(nodeValue.toString());
			}
			else if (nodeValue instanceof BooleanDataType)
			{
				return new BooleanNode(nodeValue.toString());
			}
			else if (nodeValue instanceof CharacterDataType)
			{
				return new CharacterNode(nodeValue.toString());
			}
		}
		else if (node instanceof IntegerNode || node instanceof FloatNode || node instanceof StringNode || node instanceof CharacterNode)
		{
			return node;
		}
		
		throw new SyntaxErrorException("Invalid type in expression");
	}
	
	/**
	 * Evaluates an AssignmentNode and replaces a variable's value in the HashMap with the assignment value.
	 * @param statement The AssignmentNode to be evaluated
	 * @param variables A HashMap containing the function's variables, for variable replacement
	 * @throws SyntaxErrorException If the target and the assignment values are of different types
	 */
	private void AssignmentNode(StatementNode statement, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		AssignmentNode assignment = (AssignmentNode) statement;
		
		Node targetValueAsNode = expression(assignment.getTargetValue(), variables);
		
		String targetName = assignment.getTarget().getName();
		InterpreterDataType targetValue = null;
		
		// Confirm that the variable does exist
		VariableReferenceNode(targetName, variables);
		
		if (targetValueAsNode instanceof IntegerNode)
		{
			targetValue = new IntegerDataType((IntegerNode) targetValueAsNode);
		}
		else if (targetValueAsNode instanceof FloatNode)
		{
			targetValue = new RealDataType((FloatNode) targetValueAsNode);
		}
		else if (targetValueAsNode instanceof BooleanNode)
		{
			targetValue = new BooleanDataType((BooleanNode) targetValueAsNode);
		}
		else if (targetValueAsNode instanceof CharacterNode)
		{
			targetValue = new CharacterDataType((CharacterNode) targetValueAsNode);
		}
		else if (targetValueAsNode instanceof StringNode)
		{
			targetValue = new StringDataType((StringNode) targetValueAsNode);
		}
		
		if (variables.get(targetName).getClass() != targetValue.getClass())
		{
			throw new SyntaxErrorException("Cannot perform assignment on variable of different type");
		}
		
		variables.replace(targetName, targetValue);
		
	}
	
	/**
	 * Evaluates an IfNode and interprets the block if the condition is true.
	 * Looks for an else statement if the condition is false and calls self.
	 * @param statement The IfNode to be evaluated
	 * @param variables A HashMap containing the function's variables, for variable replacement
	 */
	private void IfNode(IfNode statement, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		IfNode node = statement;
		
		if (node.getCondition() != null) // Has condition? Check it
		{
			BooleanDataType condition = BooleanCompareNode(node.getCondition(), variables);
			
			if (condition.getValue() == true) // True statement, interpret the block
			{
				interpretBlock(node.getStatements(), variables);
			}
			else if (node.getElse() != null) // False statement, interpret else if/else, if present
			{
				IfNode(node.getElse(), variables);
			}
		}
		else // No condition; interpret the else block
		{
			interpretBlock(node.getStatements(), variables);
		}
	}
	
	/**
	 * Interprets a WhileNode by checking the condition and calling itself after interpreting the block.
	 * @param statement The WhileNode to be evaluated
	 * @param variables A HashMap containing the function's variables, for variable replacement
	 */
	private void WhileNode(WhileNode statement, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		WhileNode node = statement;
		BooleanDataType condition = BooleanCompareNode(node.getCondition(), variables);
		
		if (condition.getValue() == true) // True statement, interpret the block and check again
		{
			interpretBlock(node.getStatements(), variables);
			WhileNode(node, variables); // Loop
		}
	}
	
	/**
	 * Works similarly to WhileNode, except loops if the condition is false
	 * @param statement The RepeatNode to be evaluated
	 * @param variables A HashMap containing the function's variables, for variable replacement
	 */
	private void RepeatNode(RepeatNode statement, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		RepeatNode node = statement;
		BooleanDataType condition = BooleanCompareNode(node.getCondition(), variables);
		
		if (condition.getValue() == false) // False statement, interpret the block and check again
		{
			interpretBlock(node.getStatements(), variables);
			RepeatNode(node, variables); // Loop
		}
	}
	
	/**
	 * Loops over a ForNode using provided IntegerNodes
	 * @param statement The ForNode to be evaluated
	 * @param variables A HashMap containing the function's variables, for variable replacement
	 * @throws SyntaxErrorException If a non-integer is used as an iterator or bound
	 */
	private void ForNode(ForNode statement, HashMap<String, InterpreterDataType> variables) throws SyntaxErrorException
	{
		ForNode node = statement;
		VariableReferenceNode iteratorNode = node.getIterator();
		
		// In short, throw an exception if a non-integer is involved in the for condition
		if (!(VariableReferenceNode(iteratorNode.getName(), variables) instanceof IntegerDataType) || !(node.getFrom() instanceof IntegerNode) || !(node.getTo() instanceof IntegerNode))
		{
			throw new SyntaxErrorException("Invalid type in for loop; only integers allowed");
		}
		
		int from = ((IntegerNode) node.getFrom()).getValue();
		int to = ((IntegerNode) node.getTo()).getValue();
		
		// If to is greater than from, the loop simply doesn't run as per the language definition
		if (from < to) // Set the iterator to from if the loop is valid
		{
			variables.replace(iteratorNode.getName(), new IntegerDataType(from));
			
			while (from < to)
			{
				interpretBlock(node.getStatements(), variables);
				from++;
				variables.replace(iteratorNode.getName(), new IntegerDataType(from));
			}
		}
	}
	
	private void FunctionCallNode(StatementNode statement, HashMap<String, InterpreterDataType> variables)
	{
		// To do in the next assignment
	}
}