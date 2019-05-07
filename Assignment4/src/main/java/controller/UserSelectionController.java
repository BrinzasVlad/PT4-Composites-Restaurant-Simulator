package controller;

import java.awt.event.WindowListener;

import model.interfaces.IRestaurantProcessing;
import view.LoginView;

public class UserSelectionController {
	private IRestaurantProcessing restaurant;
	
	private LoginView view;
	
	private AdminController admin = null;
	private WaiterController waiter = null;
	private ChefController chef = null;
	
	public UserSelectionController(IRestaurantProcessing restaurant) {
		this.restaurant = restaurant;
		
		this.chef = new ChefController(restaurant); // Chef is actually always "awake".
		
		view = new LoginView();
		attachViewButtonListeners();
		view.setVisible(true);
	}
	
	public void attachViewWindowListener(WindowListener l) {
		view.addWindowListener(l);
	}
	
	private void attachViewButtonListeners() {
		view.attachAdminListener(e -> {
			if(null != admin) admin.close();
			
			admin = new AdminController(restaurant);
		});
		view.attachWaiterListener(e -> {
			if(null != waiter) waiter.close();
			
			waiter = new WaiterController(restaurant);
		});
		view.attachChefListener(e -> {
			chef.setViewVisible();
		});
	}
}
