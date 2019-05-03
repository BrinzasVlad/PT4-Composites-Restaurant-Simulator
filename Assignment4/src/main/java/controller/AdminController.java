package controller;

import javax.swing.event.TableModelEvent;

import model.MenuItem;
import model.interfaces.IRestaurantMenuProcessing;
import view.AdminView;

public class AdminController {
	private AdminView view;
	private IRestaurantMenuProcessing menuRestaurant;
	
	public AdminController(IRestaurantMenuProcessing menuRestaurant) {
		this.menuRestaurant = menuRestaurant;
		
		view = new AdminView(menuRestaurant.getMenu());
		attachViewListeners();
		view.setVisible(true);
	}
	
	/**
	 * Closes this AdminController together with its associated view.
	 */
	public void close() {
		view.dispose();
	}

	private void attachViewListeners() {
		// TODO: add listeners to the view for the "New" and "Delete" buttons
		view.attachTableChangeListener(e -> {
			int row = e.getFirstRow(); // Only one cell can be edited at once, so this is all
			
			// We only treat updates to the table here, since insertion
			// and deletion are treated in the button listeners
			if(e.getType() == TableModelEvent.UPDATE) {
				MenuItem oldItem = view.getElementBeforeUpdate();
				if(null != oldItem) {
					MenuItem changedItem = view.getRowData(row);
					menuRestaurant.removeFromMenu(oldItem);
					menuRestaurant.addToMenu(changedItem);
				}
			}
//			switch (e.getType()) {
//				case TableModelEvent.INSERT:
//					MenuItem newItem = view.getRowData(row);
//					menuRestaurant.addToMenu(newItem);
//					break;
//				case TableModelEvent.UPDATE:
//					MenuItem oldItem = view.getElementBeforeUpdate();
//					MenuItem changedItem = view.getRowData(row);
//					menuRestaurant.removeFromMenu(oldItem);
//					menuRestaurant.addToMenu(changedItem);
//					break;
//				case TableModelEvent.DELETE:
//					MenuItem deletedItem = view.getElementBeforeUpdate();
//					menuRestaurant.removeFromMenu(deletedItem);
//					break;
//			}
		});
		view.attachAddListener(e -> {
			// TODO: open a new window where you can select composite-or-not
			// and where you can set the price / components
		});
		view.attachDeleteListener(e -> {
			// TODO: cascadingly delete the base product and all products containing it
		});
	}
}
