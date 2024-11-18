package design.ore.core.datatypes.SQL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cORE_ORGANIZATIONAL_ROLE")
@JsonInclude(Include.NON_NULL)
public class OrganizationalRole
{
	public OrganizationalRole(Organization organization, String name)
	{
		this.organization = organization;
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	Long id;
	@ManyToOne
	@JsonIgnoreProperties({"organizationProductArguments", "organizationRoles"})
	@JoinColumn(name = "organizationId", referencedColumnName = "id", foreignKey=@ForeignKey(name = "FK_OrganizationToRole"))
	Organization organization;
	@Column(columnDefinition = "nvarchar(64)", name = "name", nullable = false)
	String name;
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "userRoles")
	List<UserMetadata> assignedUsers;
}
