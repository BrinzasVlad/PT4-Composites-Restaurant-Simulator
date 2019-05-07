package model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Order implements Serializable {
	private static final long serialVersionUID = -8445679034099576539L;
	
	private int id;
	private LocalDateTime date;
	private int tableNumber;
}
