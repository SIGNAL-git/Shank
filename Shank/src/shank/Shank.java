package shank;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class Shank
{

	public static void main(String[] args) throws Exception
	{
		
		if (args.length == 0)
		{
			throw new Exception("No argument given");
		}
		if (args.length > 1)
		{
			throw new Exception("Too many arguments");
		}
		
		File fileInput = new File(args[0]);
		List<String> shankInput;
		
		try
		{
			shankInput = Files.readAllLines(fileInput.toPath());
		}
		catch (java.nio.file.NoSuchFileException e)
		{
			throw new Exception("File not found");
		}
		
		shankInput.add(" "); // For final dedenting
		
		Lexer lexer = new Lexer();
		for (int i = 0; i < shankInput.size(); i++)
		{
			lexer.lex(shankInput.get(i));
		}
		lexer.printTokens();
		System.out.println(); // Separate lexer text from parser
		
		Parser parser = new Parser(lexer.getTokens());
		parser.parse();
		
		Interpreter interpreter = new Interpreter(parser.getProgram());
		interpreter.interpretProgram();
	}

}
