package controller;

import java.util.Collection;

import javax.swing.event.TableModelEvent;

import data.BillWriter;
import model.MenuItem;
import model.Order;
import model.interfaces.IRestaurantProcessing;
import view.OrderCreationDialog;
import view.WaiterView;
import view.transfer.OrderDTO;

public class WaiterController {
	private WaiterView view;
	private IRestaurantProcessing restaurant;
	
	public WaiterController(IRestaurantProcessing restaurant) {
		this.restaurant = restaurant;
		
		view = new WaiterView(restaurant.getOrders(), restaurant.getItemsPerOrder());
		attachViewListeners();
		view.setVisible(true);
	}
	
	/**
	 * Closes this WaiterController together with its associated view.
	 */
	public void close() {
		view.dispose();
	}

	private void attachViewListeners() {
		view.attachTableChangeListener(e -> {
			int row = e.getFirstRow(); // Only one cell can be edited at once, so this is all
			
			// We only treat updates to the table here, since insertion
			// and deletion are treated in the button listeners
			if(e.getType() == TableModelEvent.UPDATE) {
				Order oldOrder = view.getOrderBeforeUpdate();
				if(null != oldOrder) {
					Order newOrder = view.getRowOrder(row);
					restaurant.editOrder(oldOrder, newOrder);
				}
			}
		});
		view.attachAddListener(e -> {
			OrderCreationDialog dialog = new OrderCreationDialog(restaurant.getMenu());
			OrderDTO completeOrder = dialog.getOrder();
			
			if(null != completeOrder) {
				view.addDataRow(completeOrder.getOrder(), completeOrder.getProducts());
				restaurant.addOrder(completeOrder.getOrder(), completeOrder.getProducts());
			}
		});
		view.attachBillListener(e -> {
			Order ord = view.getSelectedRowOrder();
			Collection<MenuItem> products = view.getSelectedRowProducts();
			
			new BillWriter().writeReceipt(ord, products);
		});
		view.attachEditListener(e -> {
			int row = view.getSelectedRow();
			Order oldOrder = view.getSelectedRowOrder();
			Collection<MenuItem> oldProducts = view.getSelectedRowProducts();
			
			OrderCreationDialog dialog = new OrderCreationDialog(restaurant.getMenu(), oldOrder, oldProducts);
			OrderDTO completeOrder = dialog.getOrder();
			
			if(null != completeOrder) {
				view.deleteDataRow(row);
				view.addDataRow(completeOrder.getOrder(), completeOrder.getProducts());
				
				restaurant.editOrder(oldOrder, completeOrder.getOrder());
				restaurant.editOrderContents(completeOrder.getOrder(), completeOrder.getProducts());
			}
		});
		view.attachDeleteListener(e -> {
			Order orderToDelete = view.getSelectedRowOrder();
			
			view.deleteDataRow(view.getSelectedRow());
			restaurant.removeOrder(orderToDelete);
		});
	}
}
