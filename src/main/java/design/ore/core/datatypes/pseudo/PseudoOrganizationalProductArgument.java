package design.ore.core.datatypes.pseudo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class PseudoOrganizationalProductArgument
{
	Long id;
	String organizationId;
	String associatedProductId;
	String key;
	String value;
	Boolean delete;
}
