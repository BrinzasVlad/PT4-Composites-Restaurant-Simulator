package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompositeProduct extends MenuItem {

	private static final long serialVersionUID = -8243834625560144356L;
	
	@Getter
	private List<MenuItem> subproducts = new ArrayList<MenuItem>();

	// We need this, because Lombok doesn't know how to call super like this
	public CompositeProduct(String name) {
		super(name);
	}
	
	public boolean addSubproduct(MenuItem item) {
		return subproducts.add(item);
	}
	
	public boolean addSubproducts(Collection<MenuItem> items) {
		return subproducts.addAll(items);
	}
	
	public boolean contains(MenuItem item) {
		return subproducts.contains(item);
	}

	@Override
	public BigDecimal computePrice() {
		BigDecimal sum = BigDecimal.ZERO;
		
		for(MenuItem item : subproducts) {
			sum = sum.add(item.computePrice());
		}
		
		return sum;
	}

	@Override
	public String toString() {
		String stringForm = "CompositeProduct [name=" + name + ", subproducts=";
		
		StringJoiner products = new StringJoiner(", ", "{", "}");
		for(MenuItem item : subproducts) {
			products.add(item.toString());
		}
		stringForm += products.toString() + "]";
		
		return stringForm;
	}

}
