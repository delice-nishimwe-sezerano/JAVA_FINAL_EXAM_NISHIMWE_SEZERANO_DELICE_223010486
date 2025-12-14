package COM.FORM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import COM.UTIL.DB;

public class LOGINFORM extends JFrame implements ActionListener {
	// Welcome screen components
	JLabel welcomeLabel = new JLabel("Welcome to E-Commerce Platform", JLabel.CENTER);
	JButton enterBtn = new JButton("Login");

	// Role selection components
	JPanel rolePanel = new JPanel();
	JLabel roleLabel = new JLabel("Login As:", JLabel.CENTER);
	JButton adminBtn = new JButton("Admin");
	JButton sellerBtn = new JButton("Seller");
	JButton customerBtn = new JButton("Customer");

	// Login components
	JPanel loginPanel = new JPanel();
	JTextField userTxt = new JTextField();
	JPasswordField passTxt = new JPasswordField();
	JButton loginBtn = new JButton("Login");
	JButton backBtn = new JButton("Back");
	JButton cancelBtn = new JButton("Cancel");

	private String selectedRole = "";
	private CardLayout cardLayout;
	private JPanel mainPanel;

	// Constructor
	public LOGINFORM() {
		setTitle("E-Commerce Monitoring System");
		setBounds(100, 100, 600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialize card layout for screen switching
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		createWelcomeScreen();
		createRoleSelectionScreen();
		createLoginScreen();

		add(mainPanel);
		setVisible(true);
	}

	private void createWelcomeScreen() {
		JPanel welcomePanel = new JPanel(new BorderLayout());
		welcomePanel.setBackground(new Color(240, 248, 255));

		// Welcome label styling
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
		welcomeLabel.setForeground(new Color(0, 51, 102));
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(100, 50, 50, 50));

		// Enter button styling
		enterBtn.setFont(new Font("Arial", Font.BOLD, 16));
		enterBtn.setBackground(new Color(0, 102, 204));
		enterBtn.setForeground(Color.WHITE);
		enterBtn.setPreferredSize(new Dimension(150, 40));
		enterBtn.addActionListener(this);

		welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
		welcomePanel.add(enterBtn, BorderLayout.SOUTH);

		mainPanel.add(welcomePanel, "WELCOME");
	}

	private void createRoleSelectionScreen() {
		rolePanel.setLayout(new GridBagLayout());
		rolePanel.setBackground(new Color(245, 245, 245));
		GridBagConstraints gbc = new GridBagConstraints();

		// Role label styling
		roleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		roleLabel.setForeground(new Color(51, 51, 51));
		roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 50, 0);
		rolePanel.add(roleLabel, gbc);

		// Button panel
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15));
		buttonPanel.setBackground(new Color(245, 245, 245));

		// Style buttons
		styleRoleButton(adminBtn, new Color(220, 80, 60));
		styleRoleButton(sellerBtn, new Color(65, 105, 225));
		styleRoleButton(customerBtn, new Color(60, 179, 113));

		// Add action listeners
		adminBtn.addActionListener(this);
		sellerBtn.addActionListener(this);
		customerBtn.addActionListener(this);

		buttonPanel.add(adminBtn);
		buttonPanel.add(sellerBtn);
		buttonPanel.add(customerBtn);

		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		rolePanel.add(buttonPanel, gbc);

		mainPanel.add(rolePanel, "ROLE_SELECTION");
	}

	private void styleRoleButton(JButton button, Color color) {
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setPreferredSize(new Dimension(200, 45));
		button.setFocusPainted(false);
	}

	private void createLoginScreen() {
		loginPanel.setLayout(null);
		loginPanel.setBackground(new Color(255, 255, 255));

		// Role display label
		JLabel roleDisplayLabel = new JLabel("", JLabel.CENTER);
		roleDisplayLabel.setBounds(50, 30, 500, 30);
		roleDisplayLabel.setFont(new Font("Arial", Font.BOLD, 18));
		roleDisplayLabel.setForeground(new Color(0, 51, 102));

		// Username label and field
		JLabel userLabel = new JLabel("Username:");
		userLabel.setBounds(60, 90, 110, 25);
		userTxt.setBounds(160, 80, 210, 50);
		userTxt.setBorder(BorderFactory.createTitledBorder("Enter Username"));

		// Password label and field
		JLabel passLabel = new JLabel("Password:");
		passLabel.setBounds(60, 150, 110, 25);
		passTxt.setBounds(160, 135, 210, 50);
		passTxt.setBorder(BorderFactory.createTitledBorder("Enter Password"));

		// Buttons
		loginBtn.setBounds(50, 250, 100, 35);
		backBtn.setBounds(170, 250, 100, 35);
		cancelBtn.setBounds(290, 250, 100, 35);

		// Style buttons
		styleLoginButton(loginBtn, new Color(60, 179, 113));
		styleLoginButton(backBtn, new Color(65, 105, 225));
		styleLoginButton(cancelBtn, new Color(220, 80, 60));

		// Add action listeners
		loginBtn.addActionListener(this);
		backBtn.addActionListener(this);
		cancelBtn.addActionListener(this);

		// Add components to login panel
		loginPanel.add(roleDisplayLabel);
		loginPanel.add(userLabel);
		loginPanel.add(userTxt);
		loginPanel.add(passLabel);
		loginPanel.add(passTxt);
		loginPanel.add(loginBtn);
		loginPanel.add(backBtn);
		loginPanel.add(cancelBtn);

		// Store reference to role display label for updating
		loginPanel.putClientProperty("roleLabel", roleDisplayLabel);

		mainPanel.add(loginPanel, "LOGIN");
	}

	private void styleLoginButton(JButton button, Color color) {
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setFocusPainted(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == enterBtn) {
			cardLayout.show(mainPanel, "ROLE_SELECTION");
		}
		else if (e.getSource() == adminBtn) {
			selectedRole = "Admin";
			showLoginScreen();
		}
		else if (e.getSource() == sellerBtn) {
			selectedRole = "Seller";
			showLoginScreen();
		}
		else if (e.getSource() == customerBtn) {
			selectedRole = "Customer";
			showLoginScreen();
		}
		else if (e.getSource() == backBtn) {
			cardLayout.show(mainPanel, "ROLE_SELECTION");
			clearLoginFields();
		}
		else if (e.getSource() == cancelBtn) {
			int response = JOptionPane.showConfirmDialog(this, 
					"Are you sure you want to exit?", "Confirm Exit", 
					JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		else if (e.getSource() == loginBtn) {
			performLogin();
		}
	}

	private void showLoginScreen() {
		// Update the role display label
		JLabel roleDisplayLabel = (JLabel) loginPanel.getClientProperty("roleLabel");
		roleDisplayLabel.setText("Login as " + selectedRole);

		// Clear previous fields
		clearLoginFields();

		// Show login screen
		cardLayout.show(mainPanel, "LOGIN");
	}

	private void clearLoginFields() {
		userTxt.setText("");
		passTxt.setText("");
	}

	private void performLogin() {
		String username = userTxt.getText().trim();
		String password = new String(passTxt.getPassword());

		// Basic validation
		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, 
					"Please enter both username and password", 
					"Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try (Connection con = DB.getConnection()) {
			String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ? AND role = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password); // Note: In production, use hashed passwords
			ps.setString(3, selectedRole);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				JOptionPane.showMessageDialog(this, 
						"Login successful! Welcome " + username, 
						"Success", JOptionPane.INFORMATION_MESSAGE);

				dispose();
				new ECMS(selectedRole, rs.getInt("user_id"));
			} else {
				JOptionPane.showMessageDialog(this, 
						"Invalid login credentials or role mismatch", 
						"Login Failed", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) { 
			JOptionPane.showMessageDialog(this, 
					"Database error: " + ex.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Set system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
}