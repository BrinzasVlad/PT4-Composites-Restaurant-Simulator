package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import model.MenuItem;

public class ChefView extends JFrame {
	private static final long serialVersionUID = -5195921094739633698L;
	
	private JPanel mainPane;
		private DefaultListModel<MenuItem> taskModel;
		private JList<MenuItem> tasks;
		private JButton completedButton;
	
	public ChefView() {
		mainPane = new JPanel(new BorderLayout());
			JLabel taskLabel = new JLabel("Current tasks");
			taskLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 17));
			mainPane.add(taskLabel, BorderLayout.NORTH);
			
			taskModel = new DefaultListModel<MenuItem>();
			tasks = new JList<MenuItem>(taskModel);
			tasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tasks.setCellRenderer(new ItemCellRenderer());
			JScrollPane taskScrollPane = new JScrollPane(tasks);
			mainPane.add(taskScrollPane, BorderLayout.CENTER);
			
			completedButton = new JButton("Mark Completed");
			completedButton.addActionListener(e -> {
				taskModel.removeElement(tasks.getSelectedValue());
			});
			mainPane.add(completedButton, BorderLayout.SOUTH);
		add(mainPane);
		setDefaultCloseOperation(HIDE_ON_CLOSE); // Never actually close the chef window
		
		setSize(500, 400);
		setTitle("Chef view");
	}
	
	public void addTask(MenuItem task) {
		taskModel.addElement(task);
	}
	
	public void removeTask(MenuItem task) {
		taskModel.removeElement(task);
	}
	
	private class ItemCellRenderer extends JLabel implements ListCellRenderer<MenuItem> {
		private static final long serialVersionUID = 6270211488755801771L;

		public ItemCellRenderer() {
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
