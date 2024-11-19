package design.ore.integrateore.interpreter.statemachines;

import java.util.Iterator;
import java.util.Set;

import design.ore.integrateore.interpreter.enums.TokenType;
import design.ore.integrateore.interpreter.interfaces.IStateMachine;
import design.ore.integrateore.interpreter.pojos.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class DecimalDeclarationSM implements IStateMachine
{
	@Getter @Setter private String failReason = "";
	@Getter @Setter private boolean succeeded = false;
	@Getter @Setter private int validStatesCount = 0;
	
	@Override
	public void validate(Set<String> definedVariables, Iterator<Token> itr)
	{
		int unclosedParenthesisCount = 0;
		boolean expectingOperator = false;
		
		while(itr.hasNext())
		{
			Token next = itr.next();
			TokenType nextType = next.getType();
			
			if(validStatesCount == 0 && nextType != TokenType.DECIMAL_DECLARATION)
			{
				fail("Incomplete syntax, expected 'decimal'.");
				break;
			}
			else if(validStatesCount == 1 && nextType != TokenType.VARIABLE)
			{
				fail("Incomplete syntax, expected variable name.");
				break;
			}
			else if(validStatesCount == 2 && nextType != TokenType.EQUALS)
			{
				fail("Incomplete syntax, expected '='");
				break;
			}
			else
			{
				fail("Incomplete syntax, expected spec reference, variable, or decimal literal, instead got duplicate operator.");

				if(nextType == TokenType.OPEN_PARENTHESIS)
				{
					if(expectingOperator)
					{
						fail("Incomplete syntax, expected number operator, instead got parenthesis.");
						unclosedParenthesisCount = 0;
						break;
					}
					else
					{
						unclosedParenthesisCount++;
						expectingOperator = false;
					}
				}
				else if(nextType == TokenType.CLOSE_PARENTHESIS)
				{
					if(!expectingOperator)
					{
						fail("Incomplete syntax, expected spec reference, variable, or decimal literal, instead got parenthesis.");
						unclosedParenthesisCount = 0;
						break;
					}
					else
					{
						unclosedParenthesisCount--;
						expectingOperator = true;
						if(unclosedParenthesisCount == 0) succeed();
					}
				}
				else if(nextType == TokenType.DECIMAL_LITERAL || nextType == TokenType.VARIABLE || nextType == TokenType.SPEC)
				{
					expectingOperator = true;
					succeed();
				}
				else if(nextType == TokenType.INTEGER_LITERAL)
				{
					fail("Incomplete syntax, expected spec reference, variable, or decimal literal, instead got integer literal.");
					unclosedParenthesisCount = 0;
					break;
				}
				else if(nextType == TokenType.TEXT_LITERAL)
				{
					fail("Incomplete syntax, expected spec reference, variable, or decimal literal, instead got text literal.");
					unclosedParenthesisCount = 0;
					break;
				}
				else if(nextType == TokenType.TRUEFALSE_LITERAL)
				{
					fail("Incomplete syntax, expected spec reference, variable, or decimal literal, instead got truefalse literal.");
					unclosedParenthesisCount = 0;
					break;
				}
				else if(nextType == TokenType.NUMBER_OPERATOR)
				{
					if(expectingOperator)
					{
						expectingOperator = false;
						succeed();
					}
					else
					{
						fail("Incomplete syntax, expected spec reference, variable, or decimal literal, instead got duplicate operator.");
						unclosedParenthesisCount = 0;
						break;
					}
				}
			}
			
			validStatesCount++;
		}
		
		if(unclosedParenthesisCount > 0) fail("Unclosed parenthesis, expected ')'");
		else if(unclosedParenthesisCount < 0) fail("Too many parenthesis, delete ')'");
	}
}
