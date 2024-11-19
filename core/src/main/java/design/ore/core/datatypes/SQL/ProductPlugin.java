package design.ore.core.datatypes.SQL;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cORE_PRODUCT_PLUGINS")
@JsonInclude(Include.NON_NULL)
public class ProductPlugin
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	Long id;
	@Column(columnDefinition = "nvarchar(256)")
	String name;
	@Column(columnDefinition = "nvarchar(256)")
	String fileName;
	Date createdDate;
	int targetMajorVersion;
	int targetMinorVersion;
	int targetBugfixVersion;
	int pluginMajorVersion;
	int pluginMinorVersion;
	int pluginBugfixVersion;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "productId", nullable = false, referencedColumnName = "id", foreignKey=@ForeignKey(name = "FK_ProductPluginToProduct"))
	Product associatedProduct;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "organizationId", nullable = false, referencedColumnName = "id", foreignKey=@ForeignKey(name = "FK_ProductPluginToOrganization"))
	Organization associatedOrganization;
	
	/////////// This is used as sort of a pseudo-wrapper for end users to access ///////////
	@Transient
	@JsonProperty(value="associatedProductId")
	public Long getAssociatedProductId()
	{
		if(associatedProduct != null) return associatedProduct.getId();
		else return associatedProductId;
	}
	
	@JsonProperty(value="associatedProductId")
	public void setAssociatedProductId(Long associatedProductId) { this.associatedProductId = associatedProductId; }
	
	@Transient
	@JsonProperty(value="associatedOrganizationId")
	public UUID getAssociatedOrganizationId()
	{
		if(associatedOrganization != null) return associatedOrganization.getId();
		else return associatedOrganizationId;
	}
	
	@JsonProperty(value="associatedOrganizationId")
	public void setAssociatedOrganizationId(UUID associatedOrganizationId) { this.associatedOrganizationId = associatedOrganizationId; }
	
	@Transient
	@JsonIgnore
	Long associatedProductId;
	@Transient
	@JsonIgnore
	UUID associatedOrganizationId;
}
