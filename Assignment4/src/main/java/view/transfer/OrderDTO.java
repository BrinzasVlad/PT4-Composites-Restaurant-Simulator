package view.transfer;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.MenuItem;
import model.Order;

@Data
@AllArgsConstructor
public class OrderDTO {
	Order order;
	Collection<MenuItem> products;
}
