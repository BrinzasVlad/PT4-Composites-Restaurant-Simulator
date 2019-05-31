package model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode // This one we don't want, in fact, or at least not in Lombok shape
public abstract class MenuItem implements Serializable {
	
	private static final long serialVersionUID = 8003098423871292672L;
	
	@Getter @Setter
	protected String name;
	
	public abstract BigDecimal computePrice();
	public abstract String toString();
}
