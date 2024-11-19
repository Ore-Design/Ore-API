package design.ore.integrateore.interpreter.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FailPoint
{
	int failIndex;
	String failReason;
}
