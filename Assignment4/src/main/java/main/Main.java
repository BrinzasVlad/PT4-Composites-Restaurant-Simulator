package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import controller.UserSelectionController;
import data.RestaurantSerializer;
import model.Restaurant;

public class Main {

	private RestaurantSerializer serializer = new RestaurantSerializer();
	
	private Restaurant restaurant;
	
	public void start() {
		restaurant = serializer.deserialize();
		if(null == restaurant) {
			System.out.println("Unable to load restaurant configuration file");
			System.out.println("(Possibly this is the first run on this machine?)");
			System.out.println("The system has loaded an empty configuration");
			
			restaurant = new Restaurant();
		}
		
		UserSelectionController controller = new UserSelectionController(restaurant);
		controller.attachViewWindowListener(new WindowListener() {
			// We don't actually want to do anything on these events, only on the close one
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// Re-serialize all data right before the window closes
				serializer.serialize(restaurant);
			}
		});
	}
	
	public static void main(String[] args) {
		new Main().start();
	}

}
