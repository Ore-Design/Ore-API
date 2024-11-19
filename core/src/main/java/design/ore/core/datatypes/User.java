package design.ore.core.datatypes;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import design.ore.core.datatypes.SQL.Organization;
import design.ore.core.datatypes.SQL.OrganizationalRole;
import design.ore.core.datatypes.SQL.UserMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The User class is used as a wrapper for combining {@link UserMetadata}
 * from the user SQL database and authentication data from KeyCloak into
 * a single readable format that can be returned via the cORE REST API.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class User
{
	UUID id;
	String firstName;
	String lastName;
	String email;
	List<OrganizationalRole> roles;
	Set<String> rawRoles;
	Map<String, String> userProductArguments;
	Organization organization;
	
	@JsonIgnore
	public String getName() { return firstName + " " + lastName; }
	
	public Set<String> getRolesAsStringSet()
	{
		return Stream.concat(roles.stream().map(OrganizationalRole::getName), rawRoles.stream()).collect(Collectors.toSet());
	}
}
