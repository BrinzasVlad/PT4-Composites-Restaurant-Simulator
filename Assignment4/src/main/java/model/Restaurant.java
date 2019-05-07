package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	private Set<MenuItem> menu = new HashSet<MenuItem>(); // Set because identical entries don't make sense
	
	@Getter
	private List<Order> orders = new ArrayList<Order>(); // List because identical-ish entries might make sense
	
	@Getter
	private Map<Order, Collection<MenuItem>> itemsPerOrder = new HashMap<Order, Collection<MenuItem>>();
	
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

		// Only notifying when adding items to an order
		//observableSupport.firePropertyChange("menuItem", null, item);
	}
	
	public void removeFromMenu(MenuItem item) {
		menu.remove(item);
		
		List<MenuItem> elemToDeleteList = new ArrayList<MenuItem>();
		elemToDeleteList.add(item);
		
		List<MenuItem> newData = removeElementsContaining(menu, elemToDeleteList);
		
		// Remove all the no-longer-there elements from the menu
		for(Iterator<MenuItem> iter = menu.iterator(); iter.hasNext(); ) {
			MenuItem i = iter.next();
			if(!newData.contains(i)) iter.remove();
		}
		
		// Only notifying when adding items to an order
		//observableSupport.firePropertyChange("menuItem", item, null);
	}
	
	public void editInMenu(MenuItem oldI, MenuItem newI) {
		menu.remove(oldI);
		menu.add(newI);

		replaceReferences(menu, oldI, newI);

		// Only notifying when adding items to an order
		//observableSupport.firePropertyChange("menuItem", oldI, newI);
	}
	
	public void addOrder(Order ord, Collection<MenuItem> items) {
		orders.add(ord);
		itemsPerOrder.put(ord, items);
		
		// Only notifying when adding items to an order - such as here
		observableSupport.firePropertyChange("orderProducts", null, items);
	}
	
	public void removeOrder(Order ord) {
		Collection<MenuItem> oldProducts = itemsPerOrder.get(ord);
		orders.remove(ord);
		itemsPerOrder.remove(ord);
		
		// Notifying here, too, although it's *not* technically required
		observableSupport.firePropertyChange("orderProducts", oldProducts, null);
	}
	
	public void editOrder(Order oldOrd, Order newOrd) {
		orders.remove(oldOrd);
		orders.add(newOrd);
		
		itemsPerOrder.put(newOrd, itemsPerOrder.get(oldOrd));
		itemsPerOrder.remove(oldOrd);
		
		// Only notifying when adding items to an order; here,
		// only details like the table number change
		//observableSupport.firePropertyChange("order", oldOrd, newOrd);
	}
	
	public void editOrderContents(Order ord, Collection<MenuItem> newItems) {
		Collection<MenuItem> oldItems = itemsPerOrder.get(ord);
		itemsPerOrder.replace(ord, newItems);
		
		observableSupport.firePropertyChange("orderProducts", oldItems, newItems);
	}
	
	/**
	 * Replaces in <b>list</b> all items that contain <b>oldValue</b> so that
	 * they contain <b>newValue</b> instead.
	 * @param list - a collection of MenuItems from which some must be altered
	 * @param oldValue - the MenuItem to search for in the component lists of 
	 * 				   CompositeProducts in <b>list</b>
	 * @param newValue - the new MenuItem to replace occurrences of <b>oldValue</b> with
	 */
	private void replaceReferences(Collection<MenuItem> list, MenuItem oldValue, MenuItem newValue) {
		for(MenuItem i : list) {
			
			if(i instanceof CompositeProduct) {
				CompositeProduct prod = (CompositeProduct)i;
				
				if(prod.getSubproducts().remove(oldValue)) {
					prod.addSubproduct(newValue);
				}
			}
		}
	}

	/**
	 * Removes from <b>list</b> all CompositeProduct elements that contain
	 * an element in <b>itemsNotToContain</b>.
	 * <br>
	 * Then, recursively removes the CompositeProduct elements that contained those
	 * and so on, until all products that used to depend directly or indirectly on
	 * an item in <b>itemsNotToContain</b> are removed.
	 * 
	 * @param list - a collection of MenuItems from which some items must be removed
	 * @param itemsToNotContain - a collection of items that no resulting elements may
	 * 							depend on, directly or indirectly
	 * @return A list that contains those elements from the original list that do not
	 * 		   depend on any of the members of <b>itemsToNotContain</b>
	 */
	private List<MenuItem> removeElementsContaining(Collection<MenuItem> list, Collection<MenuItem> itemsToNotContain) {
		List<MenuItem> moreToRemove = new LinkedList<MenuItem>();
		
		List<MenuItem> results = new ArrayList<MenuItem>();
		for(MenuItem e : list) {
			if(e instanceof BaseProduct) {
				results.add(e);
				continue;
			}
			
			CompositeProduct prod = (CompositeProduct)e;
			boolean wasRemoved = false;
			for(MenuItem item : itemsToNotContain) {
				if(prod.contains(item)) {
					moreToRemove.add(prod); // Cascade the removal
					// Discard CompositeProducts that contain not-to-contain items
					wasRemoved = true;
					break;
				}
			}
			
			if(!wasRemoved) {
				results.add(e);
			}
		}
		
		if(moreToRemove.isEmpty()) return results;
		else return removeElementsContaining(results, moreToRemove);
	}
}
