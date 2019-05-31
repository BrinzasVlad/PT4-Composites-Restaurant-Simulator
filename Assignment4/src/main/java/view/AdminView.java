package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import lombok.Setter;
import model.BaseProduct;
import model.CompositeProduct;
import model.MenuItem;

public class AdminView extends JFrame  {
	private static final long serialVersionUID = 898581997329114336L;
	
	private JPanel mainPane;
		private JScrollPane tablePane;
			private JTable menuTable;
				private MenuTableModel tableModel;
	private JPanel buttonPane;
		private JButton newButton = new JButton("New");
		private JButton editButton = new JButton("Edit");
		private JButton deleteButton = new JButton("Delete");
	
	/**
	 * Creates a new AdminView that displays data from the given menu
	 * and allows the end user to interactively edit it.
	 * <br>
	 * The view is initially hidden and must be made visible externally.
	 * @param menu - a collection of MenuItem objects representing a restaurant's menu
	 */
	public AdminView(Collection<MenuItem> menu) {
		mainPane = new JPanel(new BorderLayout());
				tableModel = new MenuTableModel(menu);
				
				menuTable = new JTable(tableModel);
				menuTable.setFillsViewportHeight(true);
				menuTable.setRowSelectionAllowed(true);
				menuTable.setColumnSelectionAllowed(false);
				menuTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablePane = new JScrollPane(menuTable);
			mainPane.add(tablePane, BorderLayout.CENTER);
		
			buttonPane = new JPanel(new GridLayout(1, 0));
				buttonPane.add(newButton);
				buttonPane.add(editButton);
				buttonPane.add(deleteButton);
			mainPane.add(buttonPane, BorderLayout.SOUTH);
			
		add(mainPane);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setSize(700, 500);
		setTitle("Administrator view");
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
	 * Returns the value of the last element that was changed,
	 * before it was edited or deleted.
	 * @return The old value of the last changed element
	 */
	public MenuItem getElementBeforeUpdate() {
		return tableModel.getElemBeforeUpdate();
	}
	
	/**
	 * Returns the currently selected row in the view's table.
	 * Returns -1 if no row is selected
	 * @return The number of the selected row or -1
	 */
	public int getSelectedRow() {
		int row = menuTable.getSelectedRow();
		if(row < 0 || row >= menuTable.getRowCount()) return -1; // Garbage data, somehow, or unselected
		else return row;
	}
	
	/**
	 * Returns the data associated to the currently
	 * selected row in the view's table or null if
	 * there is no currently selected row.
	 * @return The currently selected element or null
	 */
	public MenuItem getSelectedRowData() {
		if(-1 == getSelectedRow()) return null;
		else return tableModel.getValueAtRow(getSelectedRow());
	}
	
	/**
	 * Returns the table data from the given row
	 * @param row - the row in the table from which data will be retrieved
	 * @return The MenuItem associated to the given row
	 */
	public MenuItem getRowData(int row) {
		return tableModel.getValueAtRow(row);
	}
	
	/**
	 * Inserts a new row into the view's table
	 * storing the given element.
	 * @param data - the element to add
	 */
	public void addDataRow(MenuItem item) {
		tableModel.addRow(item);
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
	 * Returns all the data in the view's table.
	 * @return A Collection of MenuItem objects
	 */
	public Collection<MenuItem> getData() {
		return tableModel.getData();
	}
	
	/**
	 * Replaces the data in the view's table with the given
	 * data, then repaints the table.
	 * @param data - a Collection of MenuItem objects
	 * 			   representing entries of a restaurant menu
	 */
	public void setData(Collection<MenuItem> data) {
		tableModel.setData(new ArrayList<MenuItem>(data));
		refresh();
	}
	
	/**
	 * Re-paints the view's table, displaying any
	 * possible modifications to the internal values
	 * that have not yet been displayed.
	 */
	public void refresh() {
		menuTable.repaint();
	}
	
	
	
	private class MenuTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 3381349039513231196L;
		
		private final String[] columnNames = {
			"Name",
			"Components",
			"Price"
		};
		@Getter @Setter
		private List<MenuItem> data;
		
		@Getter
		private MenuItem elemBeforeUpdate; // Used for a workaround when updating cells
		
		public MenuTableModel(Collection<MenuItem> data) {
			this.data = new ArrayList<MenuItem>(data);
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
			return data.size();
		}
		
		public MenuItem getValueAtRow(int row) {
			return data.get(row);
		}
		
		public void addRow(MenuItem item) {
			if(!data.contains(item)) {
				data.add(item);
				fireTableRowsInserted(data.size(), data.size());
			}
		}
		
		public void deleteRow(int row) {
			elemBeforeUpdate = data.get(row);
			data.remove(row);
        	fireTableRowsDeleted(row, row);
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			switch(col) {
				case 0: return true; // All names can be edited
				case 1: return false; // Components cannot be edited in-line TODO
				case 2: return data.get(row) instanceof BaseProduct; // Prices can only be edited for BaseProducts
				default: return false;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			switch(col) {
				case 0: return data.get(row).getName();
				case 1: return componentsToString(data.get(row));
				case 2: return data.get(row).computePrice();
				default: return null;
			}
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			// Hacky hack to store the old element before it is overwritten; essentially a makeshift clone()
			if(data.get(row) instanceof BaseProduct) {
				BaseProduct oldValue = (BaseProduct)data.get(row);
				elemBeforeUpdate = new BaseProduct(oldValue.getName(), oldValue.computePrice());
			} else {
				CompositeProduct oldValue = (CompositeProduct)data.get(row);
				elemBeforeUpdate = new CompositeProduct(oldValue.getName());
				((CompositeProduct)elemBeforeUpdate).addSubproducts(oldValue.getSubproducts());
			}
			
			
			switch(col) {
				case 0:
					data.get(row).setName((String) value);
					fireTableDataChanged(); // Because component name might cascade, we need to redraw it all
					break;
				case 2:
					MenuItem item = data.get(row);
					if(item instanceof BaseProduct) { // Only the price of basic products can be changed
						((BaseProduct)item).setPrice( (BigDecimal)value );
					}
					fireTableDataChanged(); // Because price might cascade, we need to redraw it all
					break;
			}
		}
		
		@Override
		public Class<?> getColumnClass(int col) {
			switch(col) {
				case 0: return String.class; // Name
				case 1: return String.class; // String list of components
				case 2: return BigDecimal.class; // Computed Quantity
				default: return null;
			}
		}
		
		private String componentsToString(MenuItem menuItem) {
			if(menuItem instanceof CompositeProduct) {
				StringJoiner joiner = new StringJoiner(", "); // Using '\n' is more complicated with Label displays
				for(MenuItem i : ((CompositeProduct)menuItem).getSubproducts()) joiner.add(i.getName());
				return joiner.toString();
			} else return "-";
		}
	}
}
