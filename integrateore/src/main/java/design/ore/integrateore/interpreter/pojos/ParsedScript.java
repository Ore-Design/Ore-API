package design.ore.integrateore.interpreter.pojos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParsedScript
{
	List<Token> tokens = new ArrayList<>();
	FailPoint failPoint;
	
	public void addTokens(List<Token> tokens)
	{ this.tokens.addAll(tokens); }
}
