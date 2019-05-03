package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginView extends JFrame {
	private static final long serialVersionUID = -4972101784603327025L;
	
	private JPanel mainPane;
	
	private JButton adminButton = new JButton("Administrator");
	private JButton waiterButton = new JButton("Waiter");
	private JButton chefButton = new JButton("Chef");
	
	/**
	 * Creates a new LoginView centred on screen.
	 * <br>
	 * The view will be initially invisible and must be made visible externally.
	 */
	public LoginView() {
		mainPane = new JPanel(new GridLayout(0, 1));
		
		JLabel loginLabel = new JLabel("Select User");
		loginLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 17));
		mainPane.add(loginLabel);
		
		mainPane.add(adminButton);
		mainPane.add(waiterButton);
		mainPane.add(chefButton);
		
		add(mainPane);
		
		setLocationRelativeTo(null); // Center on screen
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(300, 300);
		setTitle("User Selection Menu");
	}
	
	public void attachAdminListener(ActionListener l) {
		adminButton.addActionListener(l);
	}
	
	public void attachWaiterListener(ActionListener l) {
		waiterButton.addActionListener(l);
	}
	
	public void attachChefListener(ActionListener l) {
		chefButton.addActionListener(l);
	}
}
