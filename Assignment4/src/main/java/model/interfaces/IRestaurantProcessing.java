package model.interfaces;

import java.util.Collection;
import java.util.Map;

import model.MenuItem;
import model.Order;

public interface IRestaurantProcessing extends IRestaurantMenuProcessing {
	public Collection<Order> getOrders();
	public Map<Order, Collection<MenuItem>> getItemsPerOrder();
	public void addOrder(Order ord, Collection<MenuItem> items);
	public void removeOrder(Order ord);
	public void editOrder(Order oldOrd, Order newOrd);
	public void editOrderContents(Order ord, Collection<MenuItem> newItems);
}
