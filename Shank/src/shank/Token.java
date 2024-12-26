package shank;

public class Token {
	
	public enum TokenType
	{
		/* List of keywords */
		DEFINE,
		CONSTANTS,
		VARIABLES,
		OF,
		IF,
		ELSIF,
		THEN,
		ELSE,
		FOR,
		FROM,
		TO,
		WHILE,
		REPEATUNTIL,
		VAR,
		IDENTIFIER,
		
		/* List of variable types (And boolean assignments) */
		INTEGER,
		REAL,
		BOOLEAN,
		CHARACTER,
		STRING,
		ARRAYOF,
		TRUE,
		FALSE,
		
		/* List of operational tokens */
		NUMBER,
		PLUS,
		MINUS,
		TIMES,
		DIVIDES,
		MODULO,
		LPARENTHESIS,
		RPARENTHESIS,
		NOT,
		AND,
		OR,
		LBRACKET,
		RBRACKET,
		EQUALS,
		NOTEQUALS,
		LESSTHAN,
		LESSTHANOREQUALS,
		GREATERTHAN,
		GREATERTHANOREQUALS,
		TYPEASSIGNMENT,
		VALUEASSIGNMENT,
		COMMA,
		SEMICOLON,
		STRINGLITERAL,
		CHARACTERLITERAL,
		INDENT,
		DEDENT,
		ENDOFLINE
	}
	
	private TokenType tokenType;
	private String tokenValue;
	private int lineNumber;
	
	public Token(TokenType tokenType, String tokenValue, int lineNumber)
	{
		this.tokenType = tokenType;
		this.tokenValue = tokenValue;
		this.lineNumber = lineNumber;
	}
	
	public Token(TokenType tokenType, int lineNumber)
	{
		this.tokenType = tokenType;
		this.tokenValue = null;
		this.lineNumber = lineNumber;
	}
	
	public TokenType getTokenType()
	{
		return tokenType;
	}
	
	public String getTokenValue()
	{
		return tokenValue;
	}
	
	public int getTokenLine()
	{
		return lineNumber;
	}
	
	public String toString()
	{
		if (tokenValue != null)
		{
			return tokenType.toString() + "(" + tokenValue + ")";
		}
		else
		{
			return tokenType.toString();
		}
	}
}
