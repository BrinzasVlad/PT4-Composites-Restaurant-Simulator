package model.interfaces;

import java.beans.PropertyChangeListener;
import java.util.Set;

import model.MenuItem;

public interface IRestaurantMenuProcessing {
	public Set<MenuItem> getMenu();
	public void addToMenu(MenuItem item);
	public void removeFromMenu(MenuItem item);
	public void addChangeListener(PropertyChangeListener l);
}
