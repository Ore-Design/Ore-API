package design.ore.integrateore.interpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import design.ore.integrateore.interpreter.interfaces.IStateMachine;
import design.ore.integrateore.interpreter.pojos.FailPoint;
import design.ore.integrateore.interpreter.pojos.ParsedScript;
import design.ore.integrateore.interpreter.pojos.Token;
import design.ore.integrateore.interpreter.statemachines.DecimalDeclarationSM;
import design.ore.integrateore.interpreter.statemachines.IntegerDeclarationSM;

public final class Parser
{
	private final Tokenizer tokenizer;
	
	private final List<IStateMachine> stateMachines = new ArrayList<>();
	
	public Parser()
	{
		tokenizer = new Tokenizer();
		
		stateMachines.add(new DecimalDeclarationSM());
		stateMachines.add(new IntegerDeclarationSM());
	}
	
	public ParsedScript parseScript(String script)
	{
		if(script == null || script.equals("")) return new ParsedScript(List.of(), null);
		
		ParsedScript parsedScript = new ParsedScript();
		Set<String> definedVariables = new HashSet<>();
		
		List<Token> tokens = tokenizer.tokenize(script);
		parsedScript.addTokens(new ArrayList<>(tokens));
		
		int totalCharacterCount = 0;
		
		while(tokens.size() > 0)
		{
			stateMachines.forEach(sm -> sm.flush());
			stateMachines.forEach(sm -> sm.validate(definedVariables, new ArrayList<>(tokens).iterator()));
			
			IStateMachine closestState = stateMachines.getFirst();
			for(IStateMachine sm : stateMachines)
			{
				if(sm.getValidStatesCount() > closestState.getValidStatesCount())
				{
					closestState = sm;
				}
			}
			
			for(int x = 0 ; x < closestState.getValidStatesCount() ; x++)
			{ tokens.remove(0); }
			
			
			if(!closestState.isSucceeded())
			{
				for(Token t : parsedScript.getTokens()) { totalCharacterCount += t.getTokenString().length() + 1; }
				parsedScript.setFailPoint(new FailPoint(totalCharacterCount, closestState.getFailReason()));
				break;
			}
		}
		
		return parsedScript;
	}
}
