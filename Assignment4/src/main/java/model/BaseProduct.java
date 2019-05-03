package model;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseProduct extends MenuItem {
	
	private static final long serialVersionUID = -5747622277188617506L;
	
	@Setter
	private BigDecimal price;
	
	// We need this, because Lombok doesn't know how to call super like this
	public BaseProduct(String name, BigDecimal price) {
		super(name);
		this.price = price;
	}

	@Override
	public BigDecimal computePrice() {
		return price;
	}

	@Override
	public String toString() {
		return "BaseProduct [price=" + price + ", name=" + name + "]";
	}

}
