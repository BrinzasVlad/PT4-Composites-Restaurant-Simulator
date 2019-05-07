package controller;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.swing.event.TableModelEvent;

import model.BaseProduct;
import model.CompositeProduct;
import model.MenuItem;
import model.interfaces.IRestaurantMenuProcessing;

import view.AdminView;
import view.MenuItemCreationDialog;

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
		view.attachTableChangeListener(e -> {
			int row = e.getFirstRow(); // Only one cell can be edited at once, so this is all
			
			// We only treat updates to the table here, since insertion
			// and deletion are treated in the button listeners
			if(e.getType() == TableModelEvent.UPDATE) {
				MenuItem oldItem = view.getElementBeforeUpdate();
				if(null != oldItem) {
					MenuItem newItem = view.getRowData(row);
					menuRestaurant.editInMenu(oldItem, newItem);
				}
			}
		});
		view.attachAddListener(e -> {
			MenuItemCreationDialog dialog = new MenuItemCreationDialog(menuRestaurant.getMenu());
			MenuItem newItem = dialog.getItem();
			if(null != newItem) {
				view.addDataRow(newItem);
				menuRestaurant.addToMenu(newItem);
			}
		});
		view.attachEditListener(e -> {
			MenuItem itemToEdit = view.getSelectedRowData();
			Collection<MenuItem> possibleComponents = menuRestaurant.getMenu().stream()
													  .filter(i -> (!isOrContains(i, itemToEdit)))
													  .collect(Collectors.toList());
			
			MenuItem newValue;
			if(itemToEdit instanceof BaseProduct) {
				MenuItemCreationDialog dialog = new MenuItemCreationDialog(possibleComponents,
																		   (BaseProduct)itemToEdit);
				newValue = dialog.getItem();
			} else {
				MenuItemCreationDialog dialog = new MenuItemCreationDialog(possibleComponents,
						   												   (CompositeProduct)itemToEdit);
				newValue = dialog.getItem();
			}
			
			if(null != newValue) {
				menuRestaurant.editInMenu(itemToEdit, newValue);
				
				view.setData(menuRestaurant.getMenu());
			}
		});
		view.attachDeleteListener(e -> {
			MenuItem itemToDelete = view.getSelectedRowData();
			menuRestaurant.removeFromMenu(itemToDelete);
			
			view.setData(menuRestaurant.getMenu());
		});
	}
	
	/**
	 * Tests whether the given item to test either is or contains
	 * in its components, components of components and so on, the
	 * given item to test for.
	 * @param itemToTest - an item that may or may not contain <b>itemToTestFor</b>
	 * @param itemToTestFor - the item that must be detected, if present
	 * @return <b>true</b> if the item to look for is found, <b>false</b> otherwise
	 */
	private boolean isOrContains(MenuItem itemToTest, MenuItem itemToTestFor) {
		if(itemToTest.equals(itemToTestFor)) return true;
		
		if(itemToTest instanceof CompositeProduct) {
			for(MenuItem subItem : ((CompositeProduct)itemToTest).getSubproducts()) {
				if(isOrContains(subItem, itemToTestFor)) return true;
			}
		}
		
		return false;
	}
}
