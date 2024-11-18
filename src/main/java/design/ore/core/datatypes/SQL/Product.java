package design.ore.core.datatypes.SQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "cORE_PRODUCTS")
@JsonInclude(Include.NON_NULL)
public class Product
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	long id;
	@Column(columnDefinition = "nvarchar(256)")
	String name;
	@Column(columnDefinition = "nvarchar(256)")
	String windowsBlobName;
	@Column(columnDefinition = "nvarchar(256)")
	String macBlobName;
	@Column(columnDefinition = "nvarchar(256)")
	String logoPath;
	@Column(columnDefinition = "nvarchar(MAX)")
	String description;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "associatedProduct")
	List<ProductArgument> productArguments;
	@JsonIgnore
	@ManyToMany
	List<Organization> ACCESS_ORGANIZATION_MAPPING;
	
	/////////// This is used as sort of a pseudo-wrapper for end users to access ///////////
	@Transient
	@JsonProperty
	protected Map<String, String> getProductArguments()
	{
		if(productArguments == null || productArguments.size() <= 0)
		{
			return readOnlyProductArguments;
		}
		else
		{
			Map<String, String> map = new HashMap<>();
			productArguments.forEach(opa -> map.put(opa.key, opa.value));
			return map;
		}
	}
	
	@Transient
	@JsonProperty
	public void setProductArguments(Map<String, String> args)
	{ readOnlyProductArguments = args; }
	
	@Transient
	@JsonIgnore
	private Map<String, String> readOnlyProductArguments = new HashMap<>();
}
