package design.ore.core.datatypes.SQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cORE_ORGANIZATIONS")
@JsonInclude(Include.NON_NULL)
public class Organization
{
	// This UUID should come from the matching Keycloak Group
	@Id
	@Column(name="id", nullable=false)
	UUID id;
	@Column(nullable = false, columnDefinition = "nvarchar(256)")
	String name;
	@Column(columnDefinition = "nvarchar(256)")
	String address1;
	@Column(columnDefinition = "nvarchar(256)")
	String address2;
	@Column(columnDefinition = "nvarchar(256)")
	String stateProvince;
	@Column(columnDefinition = "nvarchar(256)")
	String country;
	@Column(columnDefinition = "nvarchar(256)")
	String city;
	Integer postalCode;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
	List<OrganizationalProductArgument> organizationProductArguments;
	@JsonIgnoreProperties({"organization"})
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
	List<OrganizationalRole> organizationRoles;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
	List<OrganizationInvite> invites;
	@JsonIgnoreProperties({"associatedOrganization", "associatedProduct"})
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "associatedOrganization")
	List<ProductPlugin> plugins;
	@JsonIgnoreProperties({"ACCESS_ORGANIZATION_MAPPING", "description", "windowsBlobName", "macBlobName", "logoPath", "fileName"})
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "ACCESS_ORGANIZATION_MAPPING")
	List<Product> products;
	
	@Transient @JsonIgnore public List<OrganizationalProductArgument> getOrganizationProductArgumentsUnmapped() { return organizationProductArguments; }
	
	/////////// Argument wrapper for end users to access ///////////
	@Transient
	public Map<String, String> getOrganizationProductArguments()
	{
		if(organizationProductArguments == null || organizationProductArguments.size() <= 0)
		{
			return readOnlyProductArguments;
		}
		else
		{
			Map<String, String> map = new HashMap<>();
			organizationProductArguments.forEach(opa -> map.put(opa.key, opa.value));
			return map;
		}
	}
	
	@Transient
	public void setOrganizationProductArguments(Map<String, String> args)
	{ readOnlyProductArguments = args; }
	
	@Transient
	@JsonIgnore
	private Map<String, String> readOnlyProductArguments = new HashMap<>();
}
