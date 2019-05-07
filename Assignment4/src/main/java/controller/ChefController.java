package controller;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import model.interfaces.IRestaurantProcessing;
import model.MenuItem;
import view.ChefView;

public class ChefController {
	private ChefView view;
	
	public ChefController(IRestaurantProcessing restaurant) {
		view = new ChefView();
		attachListeners(restaurant);
		
		// Get initial tasks
		restaurant.getItemsPerOrder().forEach((ord, products) -> {
			for(MenuItem i : products) view.addTask(i);
		});
	}
	
	public void setViewVisible() {
		view.setVisible(true);
	}
	
	private void attachListeners(IRestaurantProcessing restaurant) {
		restaurant.addChangeListener(e -> {
			if(e.getPropertyName().equals("orderProducts")) {
				if(null == e.getOldValue()) {
					// Something new was added
					
					@SuppressWarnings("unchecked") // If we got here, it's definitely correct to cast
					Collection<MenuItem> tasks = (Collection<MenuItem>) e.getNewValue();
					
					for(MenuItem i : tasks) view.addTask(i);
				} else if(null == e.getNewValue()) {
					// Something was removed; technically this is optional!
					
					@SuppressWarnings("unchecked") // If we got here, it's definitely correct to cast
					Collection<MenuItem> tasks = (Collection<MenuItem>) e.getOldValue();
					
					for(MenuItem i : tasks) view.removeTask(i);
				} else {
					// Something was edited; technically here we could just add
					// what's new and ignore what was removed!
					
					@SuppressWarnings("unchecked")
					Collection<MenuItem> oldTasks = (Collection<MenuItem>) e.getOldValue();
					@SuppressWarnings("unchecked")
					Collection<MenuItem> newTasks = (Collection<MenuItem>) e.getNewValue();
					
					List<MenuItem> tasksToAdd = new LinkedList<MenuItem>(newTasks);
					tasksToAdd.removeAll(oldTasks); // The tasks to add are the new tasks that weren't in the old ones
					
					List<MenuItem> tasksToRemove = new LinkedList<MenuItem>(oldTasks);
					tasksToRemove.removeAll(newTasks); // Tasks to remove are the old tasks that are no longer in the new
					
					for(MenuItem i : tasksToRemove) view.removeTask(i);
					for(MenuItem i : tasksToAdd) view.addTask(i);
				}
			}
		});
	}
}
