package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.interfaces.IRestaurantProcessing;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant implements Serializable, IRestaurantProcessing {

	private static final long serialVersionUID = -4926370622648439074L;
	
	@ToString.Exclude
	private transient PropertyChangeSupport observableSupport = new PropertyChangeSupport(this);
	
	@Getter
	private Set<MenuItem> menu = new HashSet<MenuItem>();
	
	// We need the method below, since observableSupport is lost in serialization
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		observableSupport = new PropertyChangeSupport(this);
	}
	
	public void addChangeListener(PropertyChangeListener l) {
		observableSupport.addPropertyChangeListener(l);
	}
	
	public void removeChangeListener(PropertyChangeListener l) {
		observableSupport.removePropertyChangeListener(l);
	}

	public void addToMenu(MenuItem item) {
		menu.add(item);
		
		// This is rather hacky. It says "we modified the menu with a new menu item, here it is!"
		// to the listeners (observers).
		observableSupport.firePropertyChange("menuItem", null, item);
	}
	
	public void removeFromMenu(MenuItem item) {
		menu.remove(item);
		
		// This is rather hacky. It says "we removed an item from the menu, this one!"
		// to the listeners (observers).
		observableSupport.firePropertyChange("menuItem", item, null);
	}
	
	public void addOrder() {
		// TODO
	}
	
	public void removeOrder() {
		// TODO
	}
}
