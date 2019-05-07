package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import model.MenuItem;
import model.Order;
import view.transfer.OrderDTO;

public class OrderCreationDialog {
	private JPanel mainPane;
		private JPanel staticDataPanel;
			private JTextField id = new JTextField();
			private JTextField tableNumber = new JTextField();
		private JPanel listsPanel;
			private JPanel currentOrderPanel;
				private DefaultListModel<MenuItem> currentListModel;
				private JList<MenuItem> currentList;
				private JButton deleteButton;
			private JPanel menuPanel;
				private JList<MenuItem> menuList;
				private JButton addButton;
	
	/**
	 * Create a new OrderCreationDialog aimed at generating an Order from scratch.
	 * <br>
	 * All of the fields will be uninitialized.
	 * <br>
	 * Use the {@link #getOrder()} method to display the dialog and wait for user input.
	 * @param menu - a list of MenuItem objects from which order contents might be chosen
	 */
	public OrderCreationDialog(Collection<MenuItem> menu) {
		initialize(menu);
	}
	
	/**
	 * Create a new OrderCreationDialog aimed at editing an existing Order.
	 * <br>
	 * The name and price fields will be initialized to their current values in the given objects.
	 * <br>
	 * Use the {@link #getOrder()} method to display the dialog and wait for user input.
	 * @param menu - a list of MenuItem objects from which order contents might be chosen
	 * @param ord - the Order to be edited
	 * @param products - the list of products currently associated with <b>ord</b>
	 */
	public OrderCreationDialog(Collection<MenuItem> menu, Order ord, Collection<MenuItem> products) {
		initialize(menu);
		
		id.setText("" + ord.getId());
		id.setEnabled(false); // Id may not be edited, once set
		
		tableNumber.setText("" + ord.getTableNumber());
		
		for(MenuItem i : products) {
			currentListModel.addElement(i);
		}
	}
			
	private void initialize(Collection<MenuItem> menu) {
		mainPane = new JPanel(new BorderLayout());
			staticDataPanel = new JPanel(new GridLayout(0, 1));
				JLabel idLabel = new JLabel("Id");
				idLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
				staticDataPanel.add(idLabel);
				staticDataPanel.add(id);
				
				JLabel numberLabel = new JLabel("Table Number");
				numberLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
				staticDataPanel.add(numberLabel);
				staticDataPanel.add(tableNumber);
			mainPane.add(staticDataPanel, BorderLayout.NORTH);
				
			listsPanel = new JPanel(new GridLayout(0, 1));
				currentOrderPanel = new JPanel(new BorderLayout());
					JLabel currentLabel = new JLabel("Current order:");
					currentLabel.setFont(new Font(null, Font.ITALIC, 12));
					currentOrderPanel.add(currentLabel, BorderLayout.NORTH);
					
					currentListModel = new DefaultListModel<MenuItem>();
					currentList = new JList<MenuItem>(currentListModel);
					currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					currentList.setCellRenderer(new OrderItemCellRenderer());
					JScrollPane currentListScrollPane = new JScrollPane(currentList);
					currentOrderPanel.add(currentListScrollPane, BorderLayout.CENTER);
					
					deleteButton = new JButton("Remove from order");
					deleteButton.addActionListener(e -> {
						int index = currentList.getSelectedIndex();
						if(-1 != index) currentListModel.remove(index);
					});
					currentOrderPanel.add(deleteButton, BorderLayout.SOUTH);
				listsPanel.add(currentOrderPanel);
				
				menuPanel = new JPanel(new BorderLayout());
					JLabel menuLabel = new JLabel("Select an item to add");
					menuLabel.setFont(new Font(null, Font.ITALIC, 12));
					menuPanel.add(menuLabel, BorderLayout.NORTH);
					
					menuList = new JList<MenuItem>(menu.toArray(new MenuItem[0]));
					menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					menuList.setCellRenderer(new OrderItemCellRenderer());
					JScrollPane menuListScrollPane = new JScrollPane(menuList);
					menuPanel.add(menuListScrollPane, BorderLayout.CENTER);
					
					addButton = new JButton("Add to order");
					addButton.addActionListener(e -> {
						MenuItem item = menuList.getSelectedValue();
						if(null != item) currentListModel.addElement(item);
					});
					menuPanel.add(addButton, BorderLayout.SOUTH);
				listsPanel.add(menuPanel);
			mainPane.add(listsPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Shows the user an OK / CANCEL dialog, then waits until
	 * the user clicks either OK or CANCEL.
	 * <br>
	 * If the user clicks CANCEL, <b>null</b> is returned.
	 * <br>
	 * If the user clicks OK, their input data is validated. If it is invalid,
	 * <b>null</b> is returned and the user receives an error message.
	 * <br>
	 * If the data is valid, it is parsed and assembled into an object
	 * which is then returned.
	 * @return An Order object, as per the user's input, or <b>null</b>
	 */
	public OrderDTO getOrder() {
		int option = JOptionPane.showConfirmDialog(null, mainPane, "Cutomise menu item",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(option == JOptionPane.OK_OPTION) {
			int id, tableNumber;
			
			try {
				id = Integer.parseInt(this.id.getText());
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error - id must be an integer!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			} // TODO: check the id for uniqueness, too!
			
			try {
				tableNumber = Integer.parseInt(this.tableNumber.getText());
			} catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error - table number must be an integer!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			int size = currentList.getModel().getSize();
			if(0 == size) {
				JOptionPane.showMessageDialog(null, "Error - order cannot be empty!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			
			Order ord = new Order(id, LocalDateTime.now(), tableNumber);
			List<MenuItem> products = new LinkedList<MenuItem>();
			for(int i = 0; i < size; ++i) {
				products.add( currentList.getModel().getElementAt(i) );
			}
			
			return new OrderDTO(ord, products);
		}
		
		return null;
	}
			
			
			
	private class OrderItemCellRenderer extends JLabel implements ListCellRenderer<MenuItem> {
		private static final long serialVersionUID = 5761831091399190739L;

		public OrderItemCellRenderer() {
	         setOpaque(true);
	     }

	     public Component getListCellRendererComponent(JList<? extends MenuItem> list,
	                                                   MenuItem value,
	                                                   int index,
	                                                   boolean isSelected,
	                                                   boolean cellHasFocus) {
	         setText(value.getName());

	         Color background;
	         Color foreground;

	         // check if this cell is selected
	         if (isSelected) {
	             background = Color.BLUE;
	             foreground = Color.WHITE;

	         // unselected, and not the DnD drop location
	         } else {
	             background = Color.WHITE;
	             foreground = Color.BLACK;
	         };

	         setBackground(background);
	         setForeground(foreground);

	         return this;
	     }
	 }
}
