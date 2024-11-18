package design.ore.integrateore.interpreter.interfaces;

import java.util.Iterator;
import java.util.Set;

import design.ore.integrateore.interpreter.pojos.Token;

public interface IStateMachine
{
	public void validate(Set<String> definedVariables, Iterator<Token> itr);
	public int getValidStatesCount();
	public void setValidStatesCount(int validStatesCount);
	public boolean isSucceeded();
	public void setSucceeded(boolean succeeded);
	public String getFailReason();
	public void setFailReason(String failReason);
	
	public default void flush()
	{
		setValidStatesCount(0);
		setSucceeded(false);
		setFailReason("");
	}
	
	public default void fail(String reason)
	{
		setSucceeded(false);
		setFailReason(reason);
	}
	
	public default void succeed()
	{
		setSucceeded(true);
		setFailReason("");
	}
}
