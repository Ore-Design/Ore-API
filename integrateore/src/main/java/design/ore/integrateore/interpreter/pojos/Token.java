package design.ore.integrateore.interpreter.pojos;

import design.ore.integrateore.interpreter.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token
{
	TokenType type;
	String tokenString;
	
	@Override
	public String toString() { return "[\"" + tokenString + "\" - Type: " + type + "]"; }
}
