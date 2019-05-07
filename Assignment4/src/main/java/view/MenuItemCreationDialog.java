package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import model.BaseProduct;
import model.CompositeProduct;
import model.MenuItem;

public class MenuItemCreationDialog {
	private JPanel mainPane;
		private JPanel staticDataPanel;
			private JTextField name = new JTextField();
			private JCheckBox isComposite = new JCheckBox();
		private JPanel moreDataPanel;
			private JPanel basePanel;
				private JTextField price = new JTextField();
			private JPanel compositePanel;
				private JList<MenuItem> components;
	
	/**
	 * Create a new MenuItemCreationDialog aimed at generating a MenuItem from scratch.
	 * <br>
	 * All of the fields will be uninitialized.
	 * <br>
	 * Use the {@link #getItem()} method to display the dialog and wait for user input.
	 * @param componentOptions - a list containing the products out of which a new
	 * 						   CompositeProduct might be made.
	 */
	public MenuItemCreationDialog(Collection<MenuItem> componentOptions) {
		initialize(componentOptions);
	}
	
	/**
	 * Create a new MenuItemCreationDialog aimed at editing an existing MenuItem.
	 * <br>
	 * Use this constructor for BaseProduct items; for CompositeProduct items, please
	 * use {@link #MenuItemCreationDialog(Collection, CompositeProduct)} instead.
	 * <br>
	 * The name and price fields will be initialized to their current values in the given object.
	 * <br>
	 * Use the {@link #getItem()} method to display the dialog and wait for user input.
	 * <br>
	 * Note that it is possible for the dialog to return a CompositeProduct instead of
	 * a BaseProduct if the user changes the "Composite" option.
	 * @param componentOptions - a list containing the products out of which a new
	 * 						   CompositeProduct might be made.
	 * @param prod - the BaseProduct item to edit
	 */
	public MenuItemCreationDialog(Collection<MenuItem> componentOptions, BaseProduct prod) {
		initialize(componentOptions);
		
		name.setText(prod.getName());
		isComposite.setSelected(false);
		price.setText(prod.computePrice().toString());
	}
	
	/**
	 * Create a new MenuItemCreationDialog aimed at editing an existing MenuItem.
	 * <br>
	 * Use this constructor for CompositeProduct items; for BaseProduct items, please
	 * use {@link #MenuItemCreationDialog(Collection, BaseProduct)} instead.
	 * <br>
	 * The name and price fields will be initialized to their current values in the given object.
	 * <br>
	 * Use the {@link #getItem()} method to display the dialog and wait for user input.
	 * <br>
	 * Note that it is possible for the dialog to return a BaseProduct instead of
	 * a CompositeProduct if the user changes the "Composite" option.
	 * @param componentOptions - a list containing the products out of which a new
	 * 						   CompositeProduct might be made.
	 * @param prod - the CompositeProduct item to edit
	 */
	public MenuItemCreationDialog(Collection<MenuItem> componentOptions, CompositeProduct prod) {
		initialize(componentOptions);
		
		name.setText(prod.getName());
		isComposite.setSelected(true);
		
		List<Integer> toSelect = new ArrayList<Integer>();
		for(MenuItem i : prod.getSubproducts()) {
			components.setSelectedValue(i, false);
			toSelect.add(components.getSelectedIndex()); // Hacky! I just need the indices, basically
		}
		
		for(Integer i : toSelect) {
			components.addSelectionInterval(i, i);
		}
	}
	
	private void initialize(Collection<MenuItem> componentOptions) {
		mainPane = new JPanel(new BorderLayout());
		staticDataPanel = new JPanel(new GridLayout(0, 1));
			JLabel nameLabel = new JLabel("Name");
			nameLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
			staticDataPanel.add(nameLabel);
			staticDataPanel.add(name);
		
			JLabel compositeLabel = new JLabel("Composite item");
			compositeLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
			staticDataPanel.add(compositeLabel);
			staticDataPanel.add(isComposite);
			isComposite.addItemListener(e -> {
				if(e.getStateChange() == ItemEvent.SELECTED)
					((CardLayout)moreDataPanel.getLayout()).show(moreDataPanel, "Composite");
				else
					((CardLayout)moreDataPanel.getLayout()).show(moreDataPanel, "Base");
			});
		mainPane.add(staticDataPanel, BorderLayout.NORTH);
		
		moreDataPanel = new JPanel(new CardLayout());
			basePanel = new JPanel(new GridLayout(0, 1));
				JLabel priceLabel = new JLabel("Price");
				priceLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
				basePanel.add(priceLabel);
				basePanel.add(price);
			moreDataPanel.add(basePanel, "Base");
			
			compositePanel = new JPanel(new BorderLayout());
				JLabel instructionLabel = new JLabel("Please select all desired components");
				instructionLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
				JLabel extraInstructionLabel = new JLabel("(Use Ctrl-click to select multiple items)");
				extraInstructionLabel.setFont(new Font(null, Font.ITALIC, 12));
				compositePanel.add(instructionLabel, BorderLayout.NORTH);
				compositePanel.add(extraInstructionLabel, BorderLayout.NORTH);
				
				components = new JList<MenuItem>(componentOptions.toArray(new MenuItem[0]));
				components.setCellRenderer(new MenuItemCellRenderer());
				JScrollPane componentsScrollPane = new JScrollPane(components);
				compositePanel.add(componentsScrollPane, BorderLayout.CENTER);
			moreDataPanel.add(compositePanel, "Composite");
		mainPane.add(moreDataPanel, BorderLayout.CENTER);
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
	 * @return A MenuItem object, as per the user's input, or <b>null</b>
	 */
	public MenuItem getItem() {
		int option = JOptionPane.showConfirmDialog(null, mainPane, "Cutomise menu item",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(option == JOptionPane.OK_OPTION) {
			if(!isComposite.isSelected()) {
				// Base Product
				try {
					BaseProduct result = new BaseProduct(name.getText(), new BigDecimal(price.getText()));
					return result;
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Error - price must be a decimal number!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				// Composite Product
				CompositeProduct result = new CompositeProduct(name.getText());
				result.addSubproducts(components.getSelectedValuesList());
				
				if(result.getSubproducts().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Error - there must be at least one component!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else return result;
			}
		}
		
		return null;
	}
	
	private class MenuItemCellRenderer extends JLabel implements ListCellRenderer<MenuItem> {
		private static final long serialVersionUID = 6376004550022151595L;

		public MenuItemCellRenderer() {
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
