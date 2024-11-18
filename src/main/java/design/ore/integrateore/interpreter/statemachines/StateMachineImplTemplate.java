package design.ore.integrateore.interpreter.statemachines;

import java.util.Iterator;
import java.util.Set;

import design.ore.integrateore.interpreter.interfaces.IStateMachine;
import design.ore.integrateore.interpreter.pojos.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class StateMachineImplTemplate implements IStateMachine
{
	@Getter @Setter private String failReason = "";
	@Getter @Setter private boolean succeeded = false;
	@Getter @Setter private int validStatesCount = 0;
	
	@Override
	public void validate(Set<String> definedVariables, Iterator<Token> itr)
	{
		
	}
}
