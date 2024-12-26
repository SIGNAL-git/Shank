package shank;

public class SyntaxErrorException extends Exception
{
	/**
	 * Don't exactly know what this means
	 * I just know that Eclipse's suggestion silences the error without devastating the output
	 */
	private static final long serialVersionUID = -5769894342423845208L;

	public SyntaxErrorException(String message)
	{
		super(message);
	}
	

}
