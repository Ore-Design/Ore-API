package design.ore.integrateore;

import java.util.List;

import org.fxmisc.richtext.StyleClassedTextArea;

import ch.qos.logback.classic.Logger;
import design.ore.integrateore.interpreter.Parser;
import design.ore.integrateore.interpreter.enums.TokenType;
import design.ore.integrateore.interpreter.pojos.ParsedScript;
import design.ore.integrateore.interpreter.pojos.Token;

public final class ScriptOre
{
	Parser parser;
	public static Logger logger;
	
	public ScriptOre(Logger logger)
	{
		parser = new Parser();
		ScriptOre.logger = logger;
	}
	
	public ParsedScript parseScript(String script) { return parser.parseScript(script); }
	
	public ParsedScript parseScriptAndStyleize(StyleClassedTextArea textArea)
	{
		final ParsedScript parsedScript = parser.parseScript(textArea.getText());
		
		textArea.clearStyle(0, textArea.getText().length() - 1);
		
		int startingPoint = 0;
		for(Token token : parsedScript.getTokens())
		{	
			String style = "plain-text";
			switch(token.getType())
			{
				case DECIMAL_DECLARATION, INTEGER_DECLARATION, TEXT_DECLARATION, TRUEFALSE_DECLARATION:
					style = "declaration-text";
					break;
				case VARIABLE:
					style = "variable-text";
					break;
				case NUMBER_OPERATOR, PLUS, BOOLEAN_OPERATOR:
					style = "operator-text";
					break;
				case SPEC:
					style = "spec-text";
					break;
				case IF, ELSEIF, ELSE, WHILE, THEN:
					style = "conditional-text";
					break;
				case RETURN:
					style = "return-text";
					break;
				case TEXT_LITERAL:
					style = "string-text";
					break;
				case UNDEFINED:
					style = "invalid-text";
					break;
				default:
					style = "plain-text";
					break;
			}
			
			if(parsedScript.getFailPoint() != null && startingPoint >= parsedScript.getFailPoint().getFailIndex())
			{
				textArea.setStyle(startingPoint, startingPoint + token.getTokenString().length(), List.of(style, "invalid-text"));
				return parsedScript;
			}
			else textArea.setStyle(startingPoint, startingPoint + token.getTokenString().length(), List.of(style));
			
			if(token.getType() != TokenType.UNDEFINED) startingPoint += token.getTokenString().length();
		}
		
		return parsedScript;
	}
}
