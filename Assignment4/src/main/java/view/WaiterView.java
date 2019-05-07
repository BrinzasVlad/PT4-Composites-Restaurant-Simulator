package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import lombok.Getter;
import model.MenuItem;
import model.Order;

public class WaiterView extends JFrame {
	private static final long serialVersionUID = 6688989501399821800L;
	
	private JPanel mainPane;
		private JScrollPane tablePane;
			private JTable orderTable;
				private OrderTableModel tableModel;
		private JPanel buttonPanel;
			private JButton newButton = new JButton("New");
			private JButton billButton = new JButton("Print Bill");
			private JButton editButton = new JButton("Edit");
			private JButton deleteButton = new JButton("Delete");
	
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm, d MMM yy");
			
	/**
	 * Creates a new WaiterView that displays the given data
	 * and allows the end user to interactively edit it.
	 * <br>
	 * The view is initially hidden and must be made visible externally.
	 * @param orders - a Collection of Order objects
	 * @param itemsToOrder - a Map of Order objects to collections of MenuItem objects
	 */
	public WaiterView(Collection<Order> orders, Map<Order, Collection<MenuItem>> itemsToOrder) {
		mainPane = new JPanel(new BorderLayout());
				tableModel = new OrderTableModel(orders, itemsToOrder);
				
				orderTable = new JTable(tableModel);
				orderTable.setFillsViewportHeight(true);
				orderTable.setRowSelectionAllowed(true);
				orderTable.setColumnSelectionAllowed(false);
				orderTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablePane = new JScrollPane(orderTable);
			mainPane.add(tablePane, BorderLayout.CENTER);
			
			buttonPanel = new JPanel(new GridLayout(1, 0));
				buttonPanel.add(newButton);
				buttonPanel.add(billButton);
				buttonPanel.add(editButton);
				buttonPanel.add(deleteButton);
			mainPane.add(buttonPanel, BorderLayout.SOUTH);
			
		add(mainPane);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setSize(1000, 500);
		setTitle("Waiter view");
	}
	
	/**
	 * Attaches the given listener to the "Delete" button, so
	 * that it will be activated whenever the user clicks that
	 * button.
	 * @param l - the {@link ActionListener} to attach
	 */
	public void attachDeleteListener(ActionListener l) {
		deleteButton.addActionListener(l);
	}
	
	/**
	 * Attaches the given listener to the "Print Bill" button, so
	 * that it will be activated whenever the user clicks that
	 * button.
	 * @param l - the {@link ActionListener} to attach
	 */
	public void attachBillListener(ActionListener l) {
		billButton.addActionListener(l);
	}
	
	/**
	 * Attaches the given listener to the "Edit" button, so
	 * that it will be activated whenever the user clicks that
	 * button.
	 * @param l - the {@link ActionListener} to attach
	 */
	public void attachEditListener(ActionListener l) {
		editButton.addActionListener(l);
	}
	
	/**
	 * Attaches the given listener to the "New" button, so
	 * that it will be activated whenever the user clicks that
	 * button.
	 * @param l - the {@link ActionListener} to attach
	 */
	public void attachAddListener(ActionListener l) {
		newButton.addActionListener(l);
	}
	
	/**
	 * Attaches the given listener to the menu table, so
	 * that it will be activated whenever the table is changed.
	 * @param l - the {@link TableModelListener} to attach
	 */
	public void attachTableChangeListener(TableModelListener l) {
		tableModel.addTableModelListener(l);
	}
	
	/**
	 * A workaround method.
	 * <br>
	 * Returns the value of the last Order that was changed,
	 * before it was edited.
	 * @return The old value of the last changed element
	 */
	public Order getOrderBeforeUpdate() {
		return tableModel.getOrderBeforeUpdate();
	}
	
	/**
	 * Returns the currently selected row in the view's table.
	 * Returns -1 if no row is selected
	 * @return The number of the selected row or -1
	 */
	public int getSelectedRow() {
		int row = orderTable.getSelectedRow();
		if(row < 0 || row >= orderTable.getRowCount()) return -1; // Garbage data, somehow, or unselected
		else return row;
	}
	
	/**
	 * Returns the Order associated to the currently
	 * selected row in the view's table or null if
	 * there is no currently selected row.
	 * @return The currently selected element or null
	 */
	public Order getSelectedRowOrder() {
		if(-1 == getSelectedRow()) return null;
		else return tableModel.getOrderAtRow(getSelectedRow());
	}
	
	/**
	 * Returns the products associated to the currently
	 * selected row in the view's table or null if
	 * there is no currently selected row.
	 * @return The currently selected element or null
	 */
	public Collection<MenuItem> getSelectedRowProducts() {
		if(-1 == getSelectedRow()) return null;
		else return tableModel.getProductsAtRow(getSelectedRow());
	}
	
	/**
	 * Returns the Order from the given row
	 * @param row - the row in the table from which data will be retrieved
	 * @return The Order associated to the given row
	 */
	public Order getRowOrder(int row) {
		return tableModel.getOrderAtRow(row);
	}
	
	/**
	 * Inserts a new row into the view's table
	 * storing the given element.
	 * @param ord - the order to add
	 * @param products - the products associated to the order
	 */
	public void addDataRow(Order ord, Collection<MenuItem> products) {
		tableModel.addRow(ord, products);
		refresh();
	}
	
	/**
	 * Deletes the specified row from the view's table.
	 * @param row - a row number
	 */
	public void deleteDataRow(int row) {
		tableModel.deleteRow(row);
		refresh();
	}
	
	/**
	 * Returns all the orders in the view's table.
	 * @return A Collection of Order objects
	 */
	public Collection<Order> getOrders() {
		return tableModel.getOrders();
	}
	
	/**
	 * Returns the map between orders and products in the view's table.
	 * @return A Map of Order objects to collections of MenuItem objects
	 */
	public Map<Order, Collection<MenuItem>> getItemsToOrder() {
		return tableModel.getItemsToOrder();
	}
	
	/**
	 * Replaces the data in the view's table with the given
	 * data, then repaints the table.
	 * @param orders - a Collection of Order objects
	 * @param itemsToOrder - a Map of Order objects to collections of MenuItem objects
	 */
	public void setData(Collection<Order> orders, Map<Order, Collection<MenuItem>> itemsToOrder) {
		tableModel.setData(orders, itemsToOrder);
		refresh();
	}
	
	/**
	 * Re-paints the view's table, displaying any
	 * possible modifications to the internal values
	 * that have not yet been displayed.
	 */
	public void refresh() {
		orderTable.repaint();
	}
	
	
				
	private class OrderTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 8581834500493219163L;
		
		private final String columnNames[] = {
				"Id",
				"Date and Time",
				"Table Number",
				"Products"
		};
		@Getter
		private List<Order> orders;
		@Getter
		private Map<Order, Collection<MenuItem>> itemsToOrder;
		
		@Getter
		private Order orderBeforeUpdate; // Used for a workaround when editing table numbers
		
		public void setData(Collection<Order> orders, Map<Order, Collection<MenuItem>> itemsToOrder) {
			this.orders = new ArrayList<Order>(orders); // Clone the data to avoid messing stuff up
			this.itemsToOrder = new HashMap<Order, Collection<MenuItem>>(itemsToOrder);
		}
		
		public OrderTableModel(Collection<Order> orders, Map<Order, Collection<MenuItem>> itemsToOrder) {
			setData(orders, itemsToOrder);
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		
		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return orders.size();
		}
		
		public Order getOrderAtRow(int row) {
			return orders.get(row);
		}
		
		public Collection<MenuItem> getProductsAtRow(int row) {
			return itemsToOrder.get(orders.get(row));
		}
		
		public void addRow(Order ord, Collection<MenuItem> products) {
			if(!orders.contains(ord)) {
				orders.add(ord);
				itemsToOrder.put(ord, products);
				fireTableRowsInserted(orders.size(), orders.size());
			}
		}
		
		public void deleteRow(int row) {
			orders.remove(row);
        	fireTableRowsDeleted(row, row);
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			switch(col) {
				case 0: return false; // Order id cannot be edited
				case 1: return false; // Debatable, but order date is fixed
				case 2: return true; // Table number can be edited
				case 3: return false; // Products cannot be edited inline
				default: return false;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			switch(col) {
				case 0: return orders.get(row).getId();
				case 1: return dateFormatter.format( orders.get(row).getDate() );
				case 2: return orders.get(row).getTableNumber();
				case 3:
					Order ord = orders.get(row);
					return productListToString(itemsToOrder.get(ord));
				default: return null;
			}
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			Order oldValue = orders.get(row);
			orderBeforeUpdate = new Order(oldValue.getId(), oldValue.getDate(), oldValue.getTableNumber());
			Collection<MenuItem> productList = itemsToOrder.get(orders.get(row));
			
			switch(col) {
				case 2:
					itemsToOrder.remove(orders.get(row)); // Not allowed to modify items while they're keys!
					orders.get(row).setTableNumber((Integer)value);
					itemsToOrder.put(orders.get(row), productList); // Move from old order
					break;
			}
		}
		
		@Override
		public Class<?> getColumnClass(int col) {
			switch(col) {
				case 0: return Integer.class; // Id
				case 1: return String.class; // Formatted Date
				case 2: return Integer.class; // Table Number
				case 3: return String.class; // Products
				default: return null;
			}
		}
		
		private String productListToString(Collection<MenuItem> products) {
			StringJoiner joiner = new StringJoiner(", ");
			
			for(MenuItem i : products) {
				joiner.add(i.getName());
			}
			
			return joiner.toString();
		}
		
	}
}
