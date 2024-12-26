package shank;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

	private States state = States.None;
	private String output = "";
	private int indentLevel = 0;
	private ArrayList<Token> tokenList = new ArrayList<Token>();
	
	private int lineNumber = 0; // For debugging purposes in thrown exceptions
	
	private int newIndentLevel = 0;
	private int spaceCounter = 0;
	
	private HashMap<String, Token.TokenType> hash = new HashMap<String, Token.TokenType>();
	
	private enum States
	{
		None,
		Comment,
		Indent,
		Word,
		NumberPreDecimal,
		NumberPostDecimal,
		StringLiteral,
		CharacterLiteral,
		Symbol
	}
	
	public Lexer()
	{
		hash.put("define", Token.TokenType.DEFINE);
		hash.put("constants", Token.TokenType.CONSTANTS);
		hash.put("variables", Token.TokenType.VARIABLES);
		hash.put("integer", Token.TokenType.INTEGER);
		hash.put("real", Token.TokenType.REAL);
		hash.put("boolean", Token.TokenType.BOOLEAN);
		hash.put("character", Token.TokenType.CHARACTER);
		hash.put("string", Token.TokenType.STRING);
		hash.put("array", Token.TokenType.ARRAYOF);
		hash.put("of", Token.TokenType.OF);
		hash.put("if", Token.TokenType.IF);
		hash.put("elsif", Token.TokenType.ELSIF);
		hash.put("then", Token.TokenType.THEN);
		hash.put("else", Token.TokenType.ELSE);
		hash.put("for", Token.TokenType.FOR);
		hash.put("from", Token.TokenType.FROM);
		hash.put("to", Token.TokenType.TO);
		hash.put("while", Token.TokenType.WHILE);
		hash.put("repeat until", Token.TokenType.REPEATUNTIL);
		hash.put("not", Token.TokenType.NOT);
		hash.put("and", Token.TokenType.AND);
		hash.put("or", Token.TokenType.OR);
		hash.put("true", Token.TokenType.TRUE);
		hash.put("false", Token.TokenType.FALSE);
		hash.put("mod", Token.TokenType.MODULO);
		hash.put("var", Token.TokenType.VAR);
	}
	
	public void lex(String fileLine) throws SyntaxErrorException
	{
		// fileLines.readAllLines trims the EndOfLine characters, so this prevents OutOfBounds exceptions
		fileLine = fileLine + " ";
		
		lineNumber++;
		newIndentLevel = 0; // Reset on new line
		spaceCounter = 0;
		
		if (state != States.Comment) // Only check indents when not in a comment
		{
			state = States.Indent; // There should only ever be one session of indenting per line
		}
		
		for (int i = 0; i < fileLine.length(); i++)
		{
			// This selects the state based on the first character it reads
			if (state == States.None)
			{
				output = ""; // If not in a state, there should be nothing read
				
				if (Character.isLetter(fileLine.charAt(i)))
				{
					state = States.Word;
				}
				else if (Character.isDigit(fileLine.charAt(i)))
				{
					state = States.NumberPreDecimal;
				}
				else if (fileLine.charAt(i) == '.')
				{
					state = States.NumberPostDecimal;
				}
				else if (fileLine.charAt(i) != ' ' && fileLine.charAt(i) != '	') // Skip mid-line spaces and tabs
				{
					state = States.Symbol; // Last resort, throws error if unknown character
				}
			}
			
			// This block executes based on state given by the above block
			switch (state)
			{
				case Indent: // Indenting is done first, and only once per line
					
					if (fileLine.charAt(i) == '	') // Tab character
					{
						newIndentLevel++;
					}
					else if (fileLine.charAt(i) == ' ') // Space character
					{
						spaceCounter++;
						
						if (spaceCounter == 4)
						{
							spaceCounter = 0;
							newIndentLevel++;
						}
					}
					else // Other character, output tokens and exit state
					{
						while (indentLevel < newIndentLevel)
						{
							tokenList.add(new Token(Token.TokenType.INDENT, lineNumber));
							indentLevel++;
						}
						while (indentLevel > newIndentLevel)
						{
							tokenList.add(new Token(Token.TokenType.DEDENT, lineNumber));
							indentLevel--;
						}
						
						i--; // Unknown characters result in backsteps so other states can act on it
						state = States.None;
					}
					
					break;
			
				case Word:
					
					// Allows numbers for the creation of variable names
					if (Character.isLetterOrDigit(fileLine.charAt(i)))
					{
						output = output + fileLine.charAt(i);
					}
					else if (!output.equals("repeat")) // Don't make a token yet for potential keyword with space
					{
						if (hash.getOrDefault(output, Token.TokenType.IDENTIFIER) == Token.TokenType.IDENTIFIER)
						{
							tokenList.add(new Token(Token.TokenType.IDENTIFIER, output, lineNumber));
						}
						else
						{
							tokenList.add(new Token(hash.get(output), lineNumber)); // No value if the token is a keyword
						}
						
						i--;
						state = States.None;
					}
					
					break;
				
				case NumberPreDecimal:
					
					if (Character.isDigit(fileLine.charAt(i)))
					{
						output = output + fileLine.charAt(i);
					}
					else if (fileLine.charAt(i) == '.') // If decimal, add to token's value and change state
					{
						output = output + fileLine.charAt(i);
						state = States.NumberPostDecimal;
					}
					else // If not a decimal, make a token
					{
						tokenList.add(new Token(Token.TokenType.NUMBER, output, lineNumber));
						i--;
						state = States.None;
					}
					
					break;
					
				case NumberPostDecimal: // This state is only reached upon finding a decimal point
					
					if (Character.isDigit(fileLine.charAt(i)))
					{
						output = output + fileLine.charAt(i);
					}
					else
					{
						tokenList.add(new Token(Token.TokenType.NUMBER, output, lineNumber));
						i--;
						state = States.None;
					}
					
					break;
				
				case Comment:
					
					if (fileLine.charAt(i) == '}')
					{
						state = States.None;
					}
					else if (fileLine.charAt(i) == '{') // Disallow nested comments
					{
						throw new SyntaxErrorException("Invalid nesting of comments : Line  " + lineNumber);
					}
					
					break;
				
				case StringLiteral:
					
					if (isEndOfLine(fileLine, i)) // True if there is no ending quotation mark
					{
						throw new SyntaxErrorException("Unterminated string literal : Line  " + lineNumber);
					}
					else if (fileLine.charAt(i) != '"')
					{	
						output = output + fileLine.charAt(i);
					}
					else if (fileLine.charAt(i) == '"')
					{
						tokenList.add(new Token(Token.TokenType.STRINGLITERAL, output, lineNumber));
						state = States.None;
					}
					
					break;
				
				case CharacterLiteral:
					
					if (isEndOfLine(fileLine, i)) // True if there is no ending apostrophe
					{
						throw new SyntaxErrorException("Unterminated character literal : Line  " + lineNumber);
					}
					else if (fileLine.charAt(i) != '\'')
					{	
						output = output + fileLine.charAt(i);
					}
					else if (fileLine.charAt(i) == '\'')
					{	
						if (output.length() < 1) // Character literal needs one character
						{
							throw new SyntaxErrorException("No characters in character literal : Line  " + lineNumber);
						}
						if (output.length() > 1) // Character literal has to close after one character
						{
							throw new SyntaxErrorException("Excessive characters in character literal : Line  " + lineNumber);
						}
						
						tokenList.add(new Token(Token.TokenType.CHARACTERLITERAL, output, lineNumber));
						state = States.None;
					}
					
					break;
					
				case Symbol:
					switch (fileLine.charAt(i))
					{
						case '+':
							tokenList.add(new Token(Token.TokenType.PLUS, lineNumber));
							state = States.None;
							break;
							
						case '-':
							tokenList.add(new Token(Token.TokenType.MINUS, lineNumber));
							state = States.None;
							break;
						
						case '*':
							tokenList.add(new Token(Token.TokenType.TIMES, lineNumber));
							state = States.None;
							break;
						
						case '/':
							tokenList.add(new Token(Token.TokenType.DIVIDES, lineNumber));
							state = States.None;
							break;
							
						case '%':
							tokenList.add(new Token(Token.TokenType.MODULO, lineNumber));
							state = States.None;
							break;
							
						case '(':
							tokenList.add(new Token(Token.TokenType.LPARENTHESIS, lineNumber));
							state = States.None;
							break;
						
						case ')':
							tokenList.add(new Token(Token.TokenType.RPARENTHESIS, lineNumber));
							state = States.None;
							break;
						
						case '=':
							tokenList.add(new Token(Token.TokenType.EQUALS, lineNumber));
							state = States.None;
							break;
							
						case '<':
							if (nextChar(fileLine, i) == '=')
							{
								tokenList.add(new Token(Token.TokenType.LESSTHANOREQUALS, lineNumber));
								i++; // Avoids reading the next character again, done for every nextChar() check
							}
							else if (nextChar(fileLine, i) == '>')
							{
								tokenList.add(new Token(Token.TokenType.NOTEQUALS, lineNumber));
								i++;
							}
							else
							{
								tokenList.add(new Token(Token.TokenType.LESSTHAN, lineNumber));
							}
							state = States.None;
							break;
							
						case '>':
							if (nextChar(fileLine, i) == '=')
							{
								tokenList.add(new Token(Token.TokenType.GREATERTHANOREQUALS, lineNumber));
								i++;
							}
							else
							{
								tokenList.add(new Token(Token.TokenType.GREATERTHAN, lineNumber));
							}
							state = States.None;
							break;
						
						case ':':
							if (nextChar(fileLine, i) == '=')
							{
								tokenList.add(new Token(Token.TokenType.VALUEASSIGNMENT, lineNumber));
								i++; // Avoid reading the equals again
							}
							else
							{
								tokenList.add(new Token(Token.TokenType.TYPEASSIGNMENT, lineNumber));
							}
							state = States.None;
							break;
							
						case ';':
							tokenList.add(new Token(Token.TokenType.SEMICOLON, lineNumber));
							state = States.None;
							break;
							
						case '{':
							state = States.Comment;
							break;
							
						case '[':
							tokenList.add(new Token(Token.TokenType.LBRACKET, lineNumber));
							state = States.None;
							break;
							
						case ']':
							tokenList.add(new Token(Token.TokenType.RBRACKET, lineNumber));
							state = States.None;
							break;
						
						case ',':
							tokenList.add(new Token(Token.TokenType.COMMA, lineNumber));
							state = States.None;
							break;
							
						case '"':
							state = States.StringLiteral;
							break;
							
						case '\'':
							state = States.CharacterLiteral;
							break;
							
						default:
							throw new SyntaxErrorException("Unknown character : Line  " + lineNumber
									+ ": " + fileLine.charAt(i));
					}
					
			default:
				break;
				
			}
			
		}
		
		if (state == States.Indent) // End of file indentations
		{
			while (indentLevel < newIndentLevel)
			{
				tokenList.add(new Token(Token.TokenType.INDENT, lineNumber));
				indentLevel++;
			}
			while (indentLevel > newIndentLevel)
			{
				tokenList.add(new Token(Token.TokenType.DEDENT, lineNumber));
				indentLevel--;
			}
		}
		
		/* 
		 * No comment check, for the possibility of a multi-line comment at the end of some code
		 * and at the beginning of the next line of code
		 */
		if (fileLine.length() != 1) // Ignore completely blank lines
		{
			tokenList.add(new Token(Token.TokenType.ENDOFLINE, lineNumber)); // Line should be finished here
		}
		
	}
	
	private boolean isEndOfLine(String input, int position) // Used for avoiding OutOfBounds exceptions in specific cases
	{
		if (position + 1 >= input.length())
		{
			return true;
		}
		return false;
	}
	
	private char nextChar(String input, int position)
	{
		if (!isEndOfLine(input, position))
		{
			return input.charAt(position + 1);
		}
		return ' '; // This is never practically used, and is only to avoid the "must return a char" error
	}
	
	public void printTokens()
	{
		for (int i = 0; i < tokenList.size(); i++)
		{
			System.out.println(tokenList.get(i).toString() + " : Line " + tokenList.get(i).getTokenLine());
		}
	}
	
	public ArrayList<Token> getTokens()
	{
		return tokenList;
	}
	
}
