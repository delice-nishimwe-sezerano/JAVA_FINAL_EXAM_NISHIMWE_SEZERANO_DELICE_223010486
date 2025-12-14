package COM.PANEL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import COM.UTIL.DB;

public class UsersPanel extends JPanel implements ActionListener {
	JTextField idTxt = new JTextField(),
			nameTxt = new JTextField(),
			emailTxt = new JTextField(),
			fullnameTxt = new JTextField(),
			createdatTxt = new JTextField(),
			lastloginTxt = new JTextField();
	JPasswordField passTxt = new JPasswordField();
	JComboBox<String> roleCmb = new JComboBox<>(new String[]{"Admin", "Seller", "Customer"});

	// Add Button
	JButton addBtn = new JButton("Add"),
			updateBtn = new JButton("Update"),
			deleteBtn = new JButton("Delete"),
			loadBtn = new JButton("Load");

	// Add Table
	JTable table;
	DefaultTableModel model;

	public UsersPanel() {
		setLayout(null);
		String[] labels = {"ID", "Username", "FullName", "Password", "Email", "Role", "Created_At", "Last_Login"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(5, 430, 800, 300);
		setVisible(true);

		int y = 20;
		addField("ID", idTxt, y);
		y += 30;
		addField("Username", nameTxt, y);
		y += 30;
		addField("FullName", fullnameTxt, y);
		y += 30;
		addField("Password", passTxt, y);
		y += 30;
		addField("Email", emailTxt, y);
		y += 30;
		addField("Created_At", createdatTxt, y);
		y += 30;
		addField("Last_Login", lastloginTxt, y);
		y += 30;
		addComoField("Role", roleCmb, y);

		idTxt.setEditable(false); // auto increment

		addButtons();
		add(sp);

		// Load users automatically on startup
		loadUsers();

		// Fill Form when a row is clicked
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					idTxt.setText(model.getValueAt(row, 0).toString());
					nameTxt.setText(model.getValueAt(row, 1).toString());
					fullnameTxt.setText(model.getValueAt(row, 2).toString());
					passTxt.setText(model.getValueAt(row, 3).toString());
					emailTxt.setText(model.getValueAt(row, 4).toString());
					roleCmb.setSelectedItem(model.getValueAt(row, 5).toString());
					createdatTxt.setText(model.getValueAt(row, 6).toString());
					lastloginTxt.setText(model.getValueAt(row, 7).toString());
				}
			}
		});
	}

	private void addField(String lbl, JComponent txt, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20, y, 80, 25);
		txt.setBounds(100, y, 150, 25);
		add(l);
		add(txt);
	}

	private void addComoField(String lbl, JComboBox<String> cmb, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20, y, 80, 25);
		cmb.setBounds(100, y, 150, 25);
		add(l);
		add(cmb);
	}

	public void addButtons() {
		addBtn.setBounds(300, 20, 100, 30);
		updateBtn.setBounds(300, 60, 100, 30);
		deleteBtn.setBounds(300, 100, 100, 30);
		loadBtn.setBounds(300, 140, 100, 30);
		add(addBtn);
		add(updateBtn);
		add(deleteBtn);
		add(loadBtn);

		addBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		deleteBtn.addActionListener(this); // ✅ fixed: now responds
		loadBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		try (Connection con = DB.getConnection()) {
			if (e.getSource() == addBtn) {
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO users(username, full_name, password_hash, email, role, created_at, last_login) VALUES(?,?,?,?,?,?,?)");
				ps.setString(1, nameTxt.getText());
				ps.setString(2, fullnameTxt.getText());
				ps.setString(3, new String(passTxt.getPassword()));
				ps.setString(4, emailTxt.getText());
				ps.setString(5, roleCmb.getSelectedItem().toString());
				ps.setString(6, createdatTxt.getText());
				ps.setString(7, lastloginTxt.getText());
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "User Added!");
				loadUsers();
				clearFields(); // ✅ clear form after adding
			}

			else if (e.getSource() == updateBtn) {
				if (idTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Select a user to update!");
					return;
				}

				PreparedStatement ps = con.prepareStatement(
						"UPDATE users SET username=?, full_name=?, password_hash=?, email=?, role=?, created_at=?, last_login=? WHERE user_id=?");
				ps.setString(1, nameTxt.getText());
				ps.setString(2, fullnameTxt.getText());
				ps.setString(3, new String(passTxt.getPassword()));
				ps.setString(4, emailTxt.getText());
				ps.setString(5, roleCmb.getSelectedItem().toString());
				ps.setString(6, createdatTxt.getText());
				ps.setString(7, lastloginTxt.getText());
				ps.setInt(8, Integer.parseInt(idTxt.getText()));
				ps.executeUpdate();

				JOptionPane.showMessageDialog(this, "User Updated!");
				loadUsers();
				clearFields(); // ✅ clear form after update
			}

			else if (e.getSource() == deleteBtn) {
				if (idTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Select a user to delete!");
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(this,
						"Are you sure you want to delete this user?",
						"Confirm Delete", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE user_id=?");
					ps.setInt(1, Integer.parseInt(idTxt.getText()));
					ps.executeUpdate();
					JOptionPane.showMessageDialog(this, "User Deleted!");
					loadUsers();
					clearFields(); // ✅ clear form after delete
				}
			}

			else if (e.getSource() == loadBtn) {
				loadUsers();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Utility to refresh table
	private void loadUsers() {
		try (Connection con = DB.getConnection()) {
			model.setRowCount(0);
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getInt("user_id"),
						rs.getString("username"),
						rs.getString("full_name"),
						rs.getString("password_hash"),
						rs.getString("email"),
						rs.getString("role"),
						rs.getString("created_at"),
						rs.getString("last_login"),
				});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// ✅ Utility to clear form fields
	private void clearFields() {
		idTxt.setText("");
		nameTxt.setText("");
		fullnameTxt.setText("");
		passTxt.setText("");
		emailTxt.setText("");
		createdatTxt.setText("");
		lastloginTxt.setText("");
		roleCmb.setSelectedIndex(0);
	}
}

