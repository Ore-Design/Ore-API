package design.ore.integrateore.interpreter.pojos;

import design.ore.integrateore.interpreter.enums.CharType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CharToken
{
	CharType type;
	char tokenChar;
	
	@Override
	public String toString() { return "['" + tokenChar + "' - Type: " + type + "]"; }
}
