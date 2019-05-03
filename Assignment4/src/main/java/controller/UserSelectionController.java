package controller;

import java.awt.event.WindowListener;

import model.interfaces.IRestaurantProcessing;
import view.LoginView;

public class UserSelectionController {
	private IRestaurantProcessing restaurant;
	
	private LoginView view;
	
	private AdminController admin = null;
	
	public UserSelectionController(IRestaurantProcessing restaurant) {
		this.restaurant = restaurant;
		
		view = new LoginView();
		attachViewButtonListeners();
		view.setVisible(true);
	}
	
	public void attachViewWindowListener(WindowListener l) {
		view.addWindowListener(l);
	}
	
	private void attachViewButtonListeners() {
		// TODO: attach listeners to the buttons for each user: Waiter, Chef
		view.attachAdminListener(e -> {
			if(null != admin) admin.close();
			
			admin = new AdminController(restaurant);
		});
	}
}
