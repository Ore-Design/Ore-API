package design.ore.core.datatypes.SQL;

import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "cORE_ORGANIZATION_INVITES")
@JsonInclude(Include.NON_NULL)
public class OrganizationInvite
{
	public OrganizationInvite(Organization organization, String inviteeEmail)
	{
		this.organization = organization;
		this.inviteeEmail = inviteeEmail.toLowerCase();
	}
	
	public OrganizationInvite(Organization organization, String inviteeEmail, UserMetadata user)
	{
		this.organization = organization;
		this.inviteeEmail = inviteeEmail.toLowerCase();
		this.user = user;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	long id;
	@ManyToOne
	@JsonIgnoreProperties("userProductArguments")
	@JoinColumn(name = "userId", referencedColumnName = "id", foreignKey=@ForeignKey(name = "FK_UserToInvite"), nullable = true)
	UserMetadata user;
	@ManyToOne
	@JsonIgnoreProperties("organizationProductArguments")
	@JoinColumn(name = "organizationId", referencedColumnName = "id", foreignKey=@ForeignKey(name = "FK_OrganizationToInvite"), nullable = false)
	Organization organization;
	@Column(columnDefinition = "nvarchar(256)", nullable = false)
	String inviteeEmail;
	@Column(nullable = false)
	Boolean handled = false;
	@Column(nullable = false)
	Long sentTimestamp = Date.from(Instant.now()).getTime();
}
