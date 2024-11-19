package design.ore.core.datatypes.SQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cORE_USER_METADATA")
@JsonIgnoreType // Ignore this type for serialization, as all user data should be returned in the User class, appended with correct auth info.
public class UserMetadata
{
	public UserMetadata(UUID id) { this.id = id; }
	
	@Id
	@Column(nullable = false)
	UUID id;
	@ManyToMany(cascade = CascadeType.ALL)
	List<OrganizationalRole> userRoles;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	List<UserProductArgument> userProductArguments;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	List<OrganizationInvite> invites;
	
	public Map<String, String> getUserProductArgumentsAsMap()
	{
		Map<String, String> map = new HashMap<>();
		if(userProductArguments != null) userProductArguments.forEach(opa -> map.put(opa.key, opa.value));
		return map;
	}
}
