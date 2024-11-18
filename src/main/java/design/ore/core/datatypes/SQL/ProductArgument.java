package design.ore.core.datatypes.SQL;

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
@Table(name = "cORE_PRODUCT_ARGUMENTS")
@JsonInclude(Include.NON_NULL)
public class ProductArgument
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	long id;
	@ManyToOne
	@JsonIgnoreProperties("productArguments")
	@JoinColumn(name = "productId", nullable = false, referencedColumnName = "id", foreignKey=@ForeignKey(name = "FK_ProductToProductArgument"))
	Product associatedProduct;
	@Column(columnDefinition = "nvarchar(64)", name = "argumentKey", nullable = false)
	String key;
	@Column(columnDefinition = "nvarchar(4000)", name = "argumentValue", nullable = false)
	String value;
}
