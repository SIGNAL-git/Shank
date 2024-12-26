package shank;

import java.util.ArrayList;

import shank.parserNodes.*;

public class Parser {
	
	private ArrayList<Token> tokenList;
	private Token tokenInfo; // Stores info of the most recently removed token
	private ProgramNode program = new ProgramNode();
	public Parser(ArrayList<Token> tokenList)
	{
		this.tokenList = tokenList;
	}
	
	public Node parse() throws SyntaxErrorException
	{
		FunctionNode currentFunction;
		
		currentFunction = function();
		while (currentFunction != null)
		{
			program.addFunction(currentFunction);
			currentFunction = function();
		}
		
		program.addBuiltInFunctions();
		
		printTree();
		
		return program;
	}
	
	public void printTree()
	{
		System.out.println(program);
	}
	
	public ProgramNode getProgram()
	{
		return program;
	}
	
	private Token matchAndRemove(Token.TokenType tokenType)
	{
		if (tokenList.size() > 0 && tokenList.get(0).getTokenType() == tokenType)
		{
			tokenInfo = tokenList.remove(0);
			return tokenInfo; // tokenInfo can then be used to pass types, values, etc.
		}
		
		return null;
	}
	
	private void expectEndsOfLine() throws SyntaxErrorException
	{
		if (matchAndRemove(Token.TokenType.ENDOFLINE) == null) // Mandatory end of line check
		{
			throw new SyntaxErrorException("Expected ENDOFLINE, received " +  peek(0).getTokenType() + " instead : Line " + peek(0).getTokenLine());
		}
		
		while (matchAndRemove(Token.TokenType.ENDOFLINE) != null); // Remove excess end of line tokens
	}
	
	private Token peek(int lookAhead)
	{
		if (lookAhead < tokenList.size() && lookAhead >= 0)
		{
			return tokenList.get(lookAhead);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * For debugging purposes, usually called when errors occur
	 * @return The current line
	 */
	private int currentLine()
	{
		return peek(0).getTokenLine();
	}
	
	/**
	 * Creates a functionNode with a defined name, parameters, variables, and constants.
	 * @return The functionNode, or null if no function is found
	 * @throws SyntaxErrorException If missing tokens after keywords
	 */
	private FunctionNode function() throws SyntaxErrorException
	{
		FunctionNode funcNode;
		
		if (matchAndRemove(Token.TokenType.DEFINE) == null)
		{
			return null;
		}
		else
		{
			if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
			{
				funcNode = new FunctionNode(tokenInfo.getTokenValue());
			}
			else
			{
				throw new SyntaxErrorException("Expected function name after DEFINE : Line  " + currentLine());
			}
			
			if (matchAndRemove(Token.TokenType.LPARENTHESIS) == null)
			{
				throw new SyntaxErrorException("Missing parenthesis for function definition : Line  " + currentLine());
			}
			
			funcNode.setParameters(parameterDeclarations());
			
			if (matchAndRemove(Token.TokenType.RPARENTHESIS) == null)
			{
				throw new SyntaxErrorException("Unclosed parenthesis : Line " + currentLine());
			}
			
			expectEndsOfLine();
			while (peek(0).getTokenType() == Token.TokenType.VARIABLES || peek(0).getTokenType() == Token.TokenType.CONSTANTS)
			{
				if (matchAndRemove(Token.TokenType.VARIABLES) != null)
				{
					funcNode.addVariables(variableDeclarations());
					expectEndsOfLine();
				}
				if (matchAndRemove(Token.TokenType.CONSTANTS) != null)
				{
					funcNode.addVariables(constantDeclarations());
					expectEndsOfLine();
				}
			}
			
			funcNode.addStatements(statements());
			
			return funcNode;			
		}
	}
	
	/**
	 * Parses a set of parameters, recursively calls self on SEMICOLON to parse multiple sets.
	 * @return All parameters for the function
	 * @throws SyntaxErrorException If missing variable declarations after keywords
	 */
	private ArrayList<VariableNode> parameterDeclarations() throws SyntaxErrorException
	{
		ArrayList<VariableNode> parameters = new ArrayList<VariableNode>();
		boolean isChangeable = false;
		
		if (matchAndRemove(Token.TokenType.VAR) != null) // VAR check, must have IDENTIFIER immediately afterwards
		{
			isChangeable = true;
			
			if (peek(0).getTokenType() != Token.TokenType.IDENTIFIER)
			{
				throw new SyntaxErrorException("Expected variable declaration after VAR : Line  " + currentLine());
			}
		}
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) == null) // Zero parameter check
		{
			return null;
		}
		else // At least one parameter
		{
			parameters.add(new VariableNode(tokenInfo.getTokenValue(), isChangeable));
		}
		
		while (matchAndRemove(Token.TokenType.COMMA) != null) // Multi-parameter check
		{
			if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
			{
				parameters.add(new VariableNode(tokenInfo.getTokenValue(), isChangeable));
			}
			else
			{
				throw new SyntaxErrorException("Expected variable declaration after COMMA : Line  " + currentLine());
			}
		}
		
		if (matchAndRemove(Token.TokenType.TYPEASSIGNMENT) != null)
		{
			if (matchAndRemove(Token.TokenType.ARRAYOF) != null)
			{
				if (matchAndRemove(Token.TokenType.OF) == null)
				{
					throw new SyntaxErrorException("Expected OF after ARRAY : Line  " + currentLine());
				}
				
				for (int i = 0; i < parameters.size(); i++)
				{
					parameters.get(i).setArray();
				}
			}
			
			matchAndRemove(peek(0).getTokenType());
			switch (tokenInfo.getTokenType())
			{
				case INTEGER:
					for (int i = 0; i < parameters.size(); i++)
					{
						parameters.get(i).setType(VariableNode.VariableType.Integer);
					}
					break;
					
				case REAL:
					for (int i = 0; i < parameters.size(); i++)
					{
						parameters.get(i).setType(VariableNode.VariableType.Real);
					}
					break;
					
				case BOOLEAN:
					for (int i = 0; i < parameters.size(); i++)
					{
						parameters.get(i).setType(VariableNode.VariableType.Boolean);
					}
					break;
					
				case CHARACTER:
					for (int i = 0; i < parameters.size(); i++)
					{
						parameters.get(i).setType(VariableNode.VariableType.Character);
					}
					break;
					
				case STRING:
					for (int i = 0; i < parameters.size(); i++)
					{
						parameters.get(i).setType(VariableNode.VariableType.String);
					}
					break;
			
				default:
					throw new SyntaxErrorException("Invalid type declaration : Line  " + currentLine());
			}
			
			if (matchAndRemove(Token.TokenType.SEMICOLON) != null) // Add new set of parameters
			{
				parameters.addAll(parameterDeclarations());
			}
			
		}
		else
		{
			throw new SyntaxErrorException("Undeclared variable type : Line  " + currentLine());
		}
		
		return parameters;
	}
	
	/**
	 * While similar to parameterDeclarations(), differs in that arrays must have a declared size.
	 * Also, does not have to recursively call itself as only one set of variables is declared at a time.
	 * @return Set of declared variables
	 * @throws SyntaxErrorException If tokens are missing after keywords, or variable details are unspecified
	 */
	private ArrayList<VariableNode> variableDeclarations() throws SyntaxErrorException
	{
		ArrayList<VariableNode> variables = new ArrayList<VariableNode>();
		
		int arrayFrom = 0, arrayTo = 0;
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) != null) // Single variable check
		{
			variables.add(new VariableNode(tokenInfo.getTokenValue(), true));
			
			while (matchAndRemove(Token.TokenType.COMMA) != null) // Multi-variable check
			{
				if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
				{
					variables.add(new VariableNode(tokenInfo.getTokenValue()));
				}
				else
				{
					throw new SyntaxErrorException("Expected variable declaration after COMMA : Line  " + currentLine());
				}
			}
			
			if (matchAndRemove(Token.TokenType.TYPEASSIGNMENT) == null)
			{
				throw new SyntaxErrorException("Undeclared variable type : Line  " + currentLine());
			}
			
			if (matchAndRemove(Token.TokenType.ARRAYOF) != null) // Array check
			{
				if (matchAndRemove(Token.TokenType.FROM) == null)
				{
					throw new SyntaxErrorException("Expected FROM after ARRAY : Line  " + currentLine());
				}
				
				if (matchAndRemove(Token.TokenType.NUMBER) != null)
				{
					arrayFrom = Integer.parseInt(tokenInfo.getTokenValue());
				}
				else
				{
					throw new SyntaxErrorException("Missing lower bound for ARRAY : Line  " + currentLine());
				}
				
				if (matchAndRemove(Token.TokenType.TO) == null)
				{
					throw new SyntaxErrorException("Expected TO after array lower bound declaration : Line  " + currentLine());
				}
				
				if (matchAndRemove(Token.TokenType.NUMBER) != null)
				{
					arrayTo = Integer.parseInt(tokenInfo.getTokenValue());
					for (int i = 0; i < variables.size(); i++)
					{
						variables.get(i).setArray(arrayFrom, arrayTo);
					}
				}
				else
				{
					throw new SyntaxErrorException("Missing upper bound for ARRAY : Line  " + currentLine());
				}
				
				if (matchAndRemove(Token.TokenType.OF) == null)
				{
					throw new SyntaxErrorException("Expected OF after array upper bound declaration : Line  " + currentLine());
				}
			}
			
			matchAndRemove(peek(0).getTokenType());
			switch (tokenInfo.getTokenType()) // Type check
			{
				case INTEGER:
					for (int i = 0; i < variables.size(); i++)
					{
						variables.get(i).setType(VariableNode.VariableType.Integer);
						variables.get(i).setValue(new IntegerNode());
					}
					break;
					
				case REAL:
					for (int i = 0; i < variables.size(); i++)
					{
						variables.get(i).setType(VariableNode.VariableType.Real);
						variables.get(i).setValue(new FloatNode());
					}
					break;
					
				case BOOLEAN:
					for (int i = 0; i < variables.size(); i++)
					{
						variables.get(i).setType(VariableNode.VariableType.Boolean);
						variables.get(i).setValue(new BooleanNode());
					}
					break;
					
				case CHARACTER:
					for (int i = 0; i < variables.size(); i++)
					{
						variables.get(i).setType(VariableNode.VariableType.Character);
						variables.get(i).setValue(new CharacterNode());
					}
					break;
					
				case STRING:
					for (int i = 0; i < variables.size(); i++)
					{
						variables.get(i).setType(VariableNode.VariableType.String);
						variables.get(i).setValue(new StringNode());
					}
					break;
			
				default:
					throw new SyntaxErrorException("Invalid type declaration : Line  " + currentLine());
			}
			
			// Type limit declaration
			if (matchAndRemove(Token.TokenType.FROM) != null)
			{
				int from = 0;
				int to = 0;
				float realFrom = 0;
				float realTo = 0;
				
				if (matchAndRemove(Token.TokenType.NUMBER) != null)
				{
					if (tokenInfo.getTokenValue().contains("."))
					{
						realFrom = Float.parseFloat(tokenInfo.getTokenValue());
					}
					else
					{
						from = Integer.parseInt(tokenInfo.getTokenValue());
					}
				}
				else
				{
					throw new SyntaxErrorException("Missing lower bound for type limit : Line  " + currentLine());
				}
				
				if (matchAndRemove(Token.TokenType.TO) == null)
				{
					throw new SyntaxErrorException("Expected TO after type limit lower bound declaration : Line  " + currentLine());
				}
				
				if (matchAndRemove(Token.TokenType.NUMBER) != null)
				{
					if (tokenInfo.getTokenValue().contains("."))
					{
						realTo = Float.parseFloat(tokenInfo.getTokenValue());
						for (int i = 0; i < variables.size(); i++)
						{
							variables.get(i).setBounds(realFrom, realTo);
						}
					}
					else
					{
						to = Integer.parseInt(tokenInfo.getTokenValue());
						for (int i = 0; i < variables.size(); i++)
						{
							variables.get(i).setBounds(from, to);
						}
					}
				}
				else
				{
					throw new SyntaxErrorException("Missing upper bound for type limit : Line  " + currentLine());
				}
			}
			
			return variables;
		}
		else
		{

			return null; // No variables
		}
	}
	
	/**
	 * Reads a single constant and recursively calls itself to read more constants.
	 * Reads a line of constants overall.
	 * @return A set of constants
	 * @throws SyntaxErrorException If constants are undeclared, or invalid type declaration
	 */
	private ArrayList<VariableNode> constantDeclarations() throws SyntaxErrorException
	{
		ArrayList<VariableNode> constants = new ArrayList<VariableNode>();
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) != null) // Single variable check
		{
			constants.add(new VariableNode(tokenInfo.getTokenValue(), false));
			
			if (matchAndRemove(Token.TokenType.EQUALS) == null)
			{
				throw new SyntaxErrorException("Undeclared constant : Line  " + currentLine());
			}
			
			matchAndRemove(peek(0).getTokenType());
			switch (tokenInfo.getTokenType()) // Type check
			{
				case NUMBER:
					if (tokenInfo.getTokenValue().contains("."))
					{
						constants.get(0).setValue(new FloatNode(tokenInfo.getTokenValue()));
					}
					else
					{
						constants.get(0).setValue(new IntegerNode(tokenInfo.getTokenValue()));
					}
					break;
					
				case STRINGLITERAL:
					constants.get(0).setValue(new StringNode(tokenInfo.getTokenValue()));
					break;
					
				case CHARACTERLITERAL:
					constants.get(0).setValue(new CharacterNode(tokenInfo.getTokenValue()));
					break;
					
				case TRUE:
				case FALSE:
					constants.get(0).setValue(new BooleanNode(tokenInfo.getTokenValue()));
					break;
			
				default:
					throw new SyntaxErrorException("Invalid type declaration : Line  " + currentLine());
			}
			
			if (matchAndRemove(Token.TokenType.COMMA) != null)
			{
				constants.addAll(constantDeclarations());
			}
			
			return constants;
		}
		else
		{

			return null; // No variables
		}
	}
	
	/**
	 * Reads and returns a list of statements.
	 * @return An ArrayList of StatementNodes
	 * @throws SyntaxErrorException If a DEDENT is unexpectedly missing
	 */
	private ArrayList<StatementNode> statements() throws SyntaxErrorException
	{
		ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
		StatementNode statement;
		
		if (matchAndRemove(Token.TokenType.INDENT) == null) // No statements found
		{
			return null;
		}
		
		statement = statement();
		
		while (statement != null)
		{
			statements.add(statement);
			statement = statement();
		}
		
		if (matchAndRemove(Token.TokenType.DEDENT) == null)
		{
			throw new SyntaxErrorException("Unexpectedly missing DEDENT : Line " + currentLine());
		}
		
		return statements;
	}
	
	/**
	 * Parses potential statements and returns them.
	 * Returns null if no statement is found.
	 * @return A StatementNode, or null if none is found
	 */
	private StatementNode statement() throws SyntaxErrorException
	{
		StatementNode statement;
		
		// On blank line, call self again to clear it and parse an actual statement
		if (matchAndRemove(Token.TokenType.ENDOFLINE) != null)
		{
			statement = statement();
			return statement;
		}
		
		statement = assignment();
		if (statement == null)
		{
			statement = parseIf();
		}
		if (statement == null)
		{
			statement = parseWhile();
		}
		if (statement == null)
		{
			statement = parseRepeat();
		}
		if (statement == null)
		{
			statement = parseFor();
		}
		if (statement == null)
		{
			statement = parseFunctionCalls();
		}
		return statement;
	}
	
	/**
	 * Checks if the statement could be an assignment.
	 * This requires an IDENTIFIER and TYPEASSIGNMENT token.
	 * BRACKET tokens and tokens in expressions are allowed
	 * @return True if the conditions are met, false on foreign token
	 */
	private boolean isAssignment() throws SyntaxErrorException
	{
		// Missing target exception
		if (peek(0).getTokenType() == Token.TokenType.VALUEASSIGNMENT)
		{
			throw new SyntaxErrorException("Missing target for value assignment : Line " + currentLine());
		}
		
		// Assignments must start with an identifier
		if (peek(0).getTokenType() != Token.TokenType.IDENTIFIER)
		{
			return false;
		}
		
		// Size check to avoid out of bounds
		// If no array element is being accessed, the next token must be VALUEASSIGNMENT to be an assignment
		if (tokenList.size() >= 2 && peek(1).getTokenType() != Token.TokenType.LBRACKET)
		{
			if (peek(1).getTokenType() == Token.TokenType.VALUEASSIGNMENT)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		// Left-side check if an array element is accessed, foreign tokens prevent this from being an assignment
		for (int i = 0; i < tokenList.size(); i++)
		{
			switch (peek(i).getTokenType())
			{
				case IDENTIFIER:
				case LBRACKET:
				case NUMBER:
				case RBRACKET:
				case PLUS:
				case MINUS:
				case TIMES:
				case DIVIDES:
				case LPARENTHESIS:
				case RPARENTHESIS:
				case MODULO:
					break;
					
				case VALUEASSIGNMENT:
					return true;
					
				default: // Typically keywords
					return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Reads a potential assignment statement and creates one if it exists.
	 * @return An AssignmentNode, or null if no such assignment statement exists
	 * @throws SyntaxErrorException Missing right-hand side, or excessive closed brackets
	 */
	private AssignmentNode assignment() throws SyntaxErrorException
	{
		AssignmentNode assignment;
		VariableReferenceNode targetVariable = null;
		Node targetValue;
		
		if (!isAssignment()) // Don't parse if not an assignment statement
		{
			return null;
		}
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) == null) // Should always be false, given isAssignment()
		{
			throw new SyntaxErrorException("IDENTIFIER token unexpectedly missing : Line " + currentLine());
		}
		
		targetVariable = new VariableReferenceNode(tokenInfo.getTokenValue());
		
		if (matchAndRemove(Token.TokenType.LBRACKET) != null)
		{
			targetVariable.setArrayIndex(expression());
			
			if (matchAndRemove(Token.TokenType.RBRACKET) == null)
			{
				throw new SyntaxErrorException("Unclosed bracket : Line " + currentLine());
			}
		}
		
		if (matchAndRemove(Token.TokenType.RBRACKET) != null) // Excessive closed brackets
		{
			throw new SyntaxErrorException("Unexpected RBRACKET : Line " + currentLine());
		}
		
		if (matchAndRemove(Token.TokenType.VALUEASSIGNMENT) == null) // Should always be false, given isAssignment()
		{
			throw new SyntaxErrorException("VALUEASSIGNMENT token unexpectedly missing : Line " + currentLine());
		}
		
		if (matchAndRemove(Token.TokenType.STRINGLITERAL) != null) // String assignment
		{
			targetValue = new StringNode(tokenInfo.getTokenValue());
		}
		else if (matchAndRemove(Token.TokenType.CHARACTERLITERAL) != null) // Character assignment
		{
			targetValue = new CharacterNode(tokenInfo.getTokenValue());
		}
		else if (matchAndRemove(Token.TokenType.TRUE) != null || matchAndRemove(Token.TokenType.FALSE) != null) // Boolean assignment
		{
			targetValue = new BooleanNode(tokenInfo.getTokenValue());
		}
		else // Number assignment
		{
			targetValue = boolCompare();
		}
		
		if (targetValue == null)
		{
			throw new SyntaxErrorException("Missing variable assignment : Line " + currentLine());
		}
		assignment = new AssignmentNode(targetVariable, targetValue);
		expectEndsOfLine();
		
		return assignment;
	}
	
	/**
	 * Checks if a given expression could be a boolean expression by searching for a boolean operator.
	 * Used for when an expression must be a boolean expression.
	 * @return True if a boolean operator is found, false otherwise
	 */
	private boolean isBoolExpression() throws SyntaxErrorException
	{
		for (int i = 0; i < tokenList.size(); i++)
		{
			switch (peek(i).getTokenType())
			{
				case GREATERTHAN:
				case GREATERTHANOREQUALS:
				case LESSTHAN:
				case LESSTHANOREQUALS:
				case EQUALS:
				case NOTEQUALS:
					return true;
					
				default:
					break;
			}
		}
		
		return false;
	}
	
	/**
	 * Creates and returns an IfNode if IF, ELSIF, or ELSE keywords are found.
	 * Returns null otherwise.
	 * @return IfNode if a keyword is found, null if not
	 * @throws SyntaxErrorException On missing boolean conditions
	 */
	private IfNode parseIf() throws SyntaxErrorException
	{
		IfNode ifBlock;
		BooleanCompareNode condition;
		
		if (matchAndRemove(Token.TokenType.IF) != null || matchAndRemove(Token.TokenType.ELSIF) != null) // If and Elsif blocks require conditions
		{
			if (!isBoolExpression()) // If boolean expression not found after keyword, throw an error
			{
				throw new SyntaxErrorException("Expected boolean expression after " + tokenInfo.getTokenType() + " : Line " + currentLine());
			}
			
			condition = (BooleanCompareNode) boolCompare();
			
			ifBlock = new IfNode(condition);
			
			if (matchAndRemove(Token.TokenType.THEN) == null)
			{
				throw new SyntaxErrorException("Expected THEN after condition : Line " + currentLine());
			}
		}
		else if (matchAndRemove(Token.TokenType.ELSE) != null) // No condition
		{
			ifBlock = new IfNode(null);
		}
		else // No keywords
		{
			return null;
		}
		
		expectEndsOfLine(); // Condition statement read, now parse body
		ifBlock.addStatements(statements());
		
		if (peek(0).getTokenType() == Token.TokenType.ELSIF || peek(0).getTokenType() == Token.TokenType.ELSE)
		{
			ifBlock.setElse(parseIf());
		}
		else
		{
			ifBlock.setElse(null);
		}
		
		return ifBlock;
	}
	
	/**
	 * Creates and returns a WhileNode if WHILE keyword is found.
	 * Returns null otherwise.
	 * @return WhileNode if the keyword is found, null if not
	 * @throws SyntaxErrorException On missing boolean condition
	 */
	private WhileNode parseWhile() throws SyntaxErrorException
	{
		WhileNode whileBlock;
		BooleanCompareNode condition;
		
		if (matchAndRemove(Token.TokenType.WHILE) != null) // Condition required
		{
			if (!isBoolExpression()) // If boolean expression not found after keyword, throw an error
			{
				throw new SyntaxErrorException("Expected boolean expression after WHILE : Line " + currentLine());
			}
			
			condition = (BooleanCompareNode) boolCompare();
			
			whileBlock = new WhileNode(condition);
		}
		else // No keywords
		{
			return null;
		}
		
		expectEndsOfLine(); // Condition statement read, now parse body
		whileBlock.addStatements(statements());
		
		return whileBlock;
	}
	
	/**
	 * Creates and returns a RepeatNode if REPEATUNTIL keyword is found.
	 * Returns null otherwise.
	 * @return RepeatNode if the keyword is found, null if not
	 * @throws SyntaxErrorException On missing boolean condition
	 */
	private RepeatNode parseRepeat() throws SyntaxErrorException
	{
		RepeatNode repeatBlock;
		BooleanCompareNode condition;
		
		if (matchAndRemove(Token.TokenType.REPEATUNTIL) != null) // Condition required
		{
			if (!isBoolExpression()) // If boolean expression not found after keyword, throw an error
			{
				throw new SyntaxErrorException("Expected boolean expression after REPEATUNTIL : Line " + currentLine());
			}
			
			condition = (BooleanCompareNode) boolCompare();
			
			repeatBlock = new RepeatNode(condition);
		}
		else // No keywords
		{
			return null;
		}
		
		expectEndsOfLine(); // Condition statement read, now parse body
		repeatBlock.addStatements(statements());
		
		return repeatBlock;
	}
	
	/**
	 * Creates and returns a ForNode if FOR keyword is found.
	 * Returns null otherwise.
	 * @return ForNode if the keyword is found, null if not
	 * @throws SyntaxErrorException If iterator or bounds are invalid or missing
	 */
	private ForNode parseFor() throws SyntaxErrorException
	{
		ForNode forBlock;
		VariableReferenceNode iterator;
		Node from;
		Node to;
		
		if (matchAndRemove(Token.TokenType.FOR) == null) // Not a for loop
		{	
			return null;
		}
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
		{
			iterator = new VariableReferenceNode(tokenInfo.getTokenValue());
		}
		else
		{
			throw new SyntaxErrorException("Expected IDENTIFIER after FOR : Line " + currentLine());
		}
		if (matchAndRemove(Token.TokenType.FROM) != null)
		{
			from = expression();
		}
		else
		{
			throw new SyntaxErrorException("Expected FROM after IDENTIFIER : Line " + currentLine());
		}
		if (matchAndRemove(Token.TokenType.TO) != null)
		{
			to = expression();
		}
		else
		{
			throw new SyntaxErrorException("Expected TO after expression : Line " + currentLine());
		}
		
		forBlock = new ForNode(iterator, from, to);
		expectEndsOfLine(); // Condition statement read, now parse body
		forBlock.addStatements(statements());
		
		return forBlock;
	}
	
	/**
	 * Creates and returns a FunctionCallNode if IDENTIFIER is found.
	 * Returns null otherwise.
	 * @return FunctionCallNode if IDENTIFIER is found, null if not
	 * @throws SyntaxErrorException If parameters are invalid
	 */
	private FunctionCallNode parseFunctionCalls() throws SyntaxErrorException
	{
		FunctionCallNode functionCall;
		Node parameter;
		
		if (peek(0).getTokenType() != Token.TokenType.IDENTIFIER)
		{
			return null;
		}
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
		{
			functionCall = new FunctionCallNode(tokenInfo.getTokenValue());
		}
		else
		{
			throw new SyntaxErrorException("Unexpectedly missing IDENTIFIER for function call name : Line " + currentLine());
		}
		
		do
		{
			if (matchAndRemove(Token.TokenType.VAR) != null)
			{
				if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
				{
					parameter = new ParameterNode(new VariableReferenceNode(tokenInfo.getTokenValue()));
					functionCall.addParameter(new ParameterNode(parameter));
				}
				else
				{
					throw new SyntaxErrorException("Expected IDENTIFIER after VAR : Line " + currentLine());
				}
			}
			else
			{
				parameter = boolCompare();
				if (parameter != null)
				{
					functionCall.addParameter(new ParameterNode(parameter));
				}
			}
		} while (matchAndRemove(Token.TokenType.COMMA) != null);
		
		expectEndsOfLine();
		return functionCall;
	}
	
	/**
	 * Parses boolean expressions and mathematical expressions.
	 * Calls expression() first.
	 * @return A BooleanCompareNode containing a boolean expression
	 * @return MathOpsNode or number Node if no boolean operators are found
	 * @throws SyntaxErrorException If no expression found
	 */
 	private Node boolCompare() throws SyntaxErrorException
	{
		Node expression1;
		BooleanCompareNode compareNode = null;
		boolean not = false;
		
		if (matchAndRemove(Token.TokenType.NOT) != null)
		{
			not = true;
		}
		
		expression1 = expression();
		
		if (expression1 == null)
		{
			throw new SyntaxErrorException("Expression not found : Line " + currentLine());
		}
		
		if (not == false)
		{
			switch (peek(0).getTokenType())
			{
				case GREATERTHAN:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.GreaterThan);
					break;
					
				case GREATERTHANOREQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.GreaterThanOrEquals);
					break;
					
				case LESSTHAN:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.LessThan);
					break;
					
				case LESSTHANOREQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.LessThanOrEquals);
					break;
					
				case EQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.Equals);
					break;
					
				case NOTEQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.NotEquals);
					break;
					
				default:
					break;
			}		
		}
		else // Reverse the boolean operator if NOT keyword is present
		{
			switch (peek(0).getTokenType())
			{
				case GREATERTHAN:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.LessThanOrEquals);
					break;
					
				case GREATERTHANOREQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.LessThan);
					break;
					
				case LESSTHAN:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.GreaterThanOrEquals);
					break;
					
				case LESSTHANOREQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.GreaterThan);
					break;
					
				case EQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.NotEquals);
					break;
					
				case NOTEQUALS:
					compareNode = new BooleanCompareNode(BooleanCompareNode.BooleanOps.Equals);
					break;
					
				default:
					break;
			}
		}
		
		if (compareNode != null)
		{
			matchAndRemove(peek(0).getTokenType()); // Remove boolean operator
			Node expression2 = expression();
			compareNode.setLeft(expression1);
			compareNode.setRight(expression2);
			return compareNode;
		}
		
		return expression1;
	}
	
 	/**
 	 * Parses math expressions involving addition and subtraction.
 	 * Calls functions relating to higher order operations first.
 	 * @return A MathOpsNode or a singular number Node
 	 */
 	private Node expression() throws SyntaxErrorException
	{
		Node term1;
		MathOpNode mathNode = null;

		term1 = term();
		
		// Initial check is done, then mathNode can call itself to add on afterwards
		if (matchAndRemove(Token.TokenType.PLUS) != null)
		{
			mathNode = new MathOpNode(MathOpNode.MathOps.add, term1, term());
		}
		else if (matchAndRemove(Token.TokenType.MINUS) != null)
		{
			mathNode = new MathOpNode(MathOpNode.MathOps.subtract, term1, term());
		}
		
		while (peek(0).getTokenType() == Token.TokenType.PLUS || peek(0).getTokenType() == Token.TokenType.MINUS)
		{
			while (matchAndRemove(Token.TokenType.PLUS) != null)
			{
				mathNode = new MathOpNode(MathOpNode.MathOps.add, mathNode, term());
			}
			while (matchAndRemove(Token.TokenType.MINUS) != null)
			{
				mathNode = new MathOpNode(MathOpNode.MathOps.subtract, mathNode, term());
			}
		}
		
		if (mathNode != null)
		{
			return mathNode;
		}
		else
		{
			return term1;
		}
	}

 	/**
 	 * Parses math expressions involving multiplication, division, and modulo.
 	 * Calls factor() first to identify numbers and variables, or expressions within parentheses.
 	 * @return A MathOpsNode or a singular number Node
 	 */
	private Node term() throws SyntaxErrorException
	{
		Node factor1;
		MathOpNode mathNode = null;
		
		factor1 = factor();
		
		if (matchAndRemove(Token.TokenType.TIMES) != null)
		{
			mathNode = new MathOpNode(MathOpNode.MathOps.multiply, factor1, factor());
		}
		else if (matchAndRemove(Token.TokenType.DIVIDES) != null)
		{
			mathNode = new MathOpNode(MathOpNode.MathOps.divide, factor1, factor());
		}
		else if (matchAndRemove(Token.TokenType.MODULO) != null)
		{
			mathNode = new MathOpNode(MathOpNode.MathOps.mod, factor1, factor());
		}
		
		while (peek(0).getTokenType() == Token.TokenType.TIMES || peek(0).getTokenType() == Token.TokenType.DIVIDES || peek(0).getTokenType() == Token.TokenType.MODULO)
		{
			while (matchAndRemove(Token.TokenType.TIMES) != null)
			{
				mathNode = new MathOpNode(MathOpNode.MathOps.multiply, mathNode, factor());
			}
			while (matchAndRemove(Token.TokenType.DIVIDES) != null)
			{
				mathNode = new MathOpNode(MathOpNode.MathOps.divide, mathNode, factor());
			}
			while (matchAndRemove(Token.TokenType.MODULO) != null)
			{
				mathNode = new MathOpNode(MathOpNode.MathOps.mod, mathNode, factor());
			}
		}
		
		if (mathNode != null)
		{
			return mathNode;
		}
		else
		{
			return factor1;
		}
	}
	
	/**
 	 * Parses singular numbers and variables, or an expression contained within parentheses.
 	 * Calls factor() first to identify numbers or expressions within parentheses.
 	 * @return A number Node, VariableReferenceNode, or an expression Node
 	 * @throws SyntaxErrorException Upon unclosed parenthesis, unclosed bracket, or unexpected token
 	 */
	private Node factor() throws SyntaxErrorException
	{
		String storedNumber;
		boolean negativeMode = false;
		
		while (matchAndRemove(Token.TokenType.MINUS) != null)
		{
			// Multiple negatives just flips the sign
			negativeMode = !negativeMode;
		}
		
		if (matchAndRemove(Token.TokenType.NUMBER) != null)
		{
			storedNumber = tokenInfo.getTokenValue();
			if (negativeMode)
			{
				storedNumber = "-" + storedNumber;
			}
			
			if (storedNumber.contains("."))
			{
				return new FloatNode(Float.parseFloat(storedNumber));
			}
			else
			{
				return new IntegerNode(Integer.parseInt(storedNumber));
			}
		}
		
		if (matchAndRemove(Token.TokenType.LPARENTHESIS) != null)
		{
			Node expressionNode = expression();
			if (matchAndRemove(Token.TokenType.RPARENTHESIS) != null)
			{
				return expressionNode;
			}
			throw new SyntaxErrorException("Unclosed parenthesis : Line " + currentLine());
		}
		
		if (matchAndRemove(Token.TokenType.IDENTIFIER) != null)
		{
			VariableReferenceNode referenceNode = new VariableReferenceNode(tokenInfo.getTokenValue());
			
			if (matchAndRemove(Token.TokenType.LBRACKET) != null)
			{
				referenceNode.setArrayIndex(expression());
				
				if (matchAndRemove(Token.TokenType.RBRACKET) == null)
				{
					throw new SyntaxErrorException("Unclosed bracket : Line " + currentLine());
				}
			}
			
			return referenceNode;
		}
		
		throw new SyntaxErrorException("Unexpected " + peek(0).getTokenType() + " token found in expression : Line " + currentLine());
	}

}
