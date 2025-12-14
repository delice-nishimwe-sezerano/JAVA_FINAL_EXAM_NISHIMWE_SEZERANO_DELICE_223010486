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

public class OrdersPanel extends JPanel implements ActionListener {
	// ADD FIELD
	JTextField orderidTxt = new JTextField(),
			useridTxt = new JTextField(),
			ordernoTxt = new JTextField(),
			dateTxt = new JTextField(),
			totalamountTxt = new JTextField();
	JComboBox<String> statusCmb = new JComboBox<>(new String[]{"Pending", "Paid"});
	JComboBox<String> paymethodCmb = new JComboBox<>(new String[]{"Card", "MOMO"});
	JComboBox<String> notesCmb = new JComboBox<>(new String[]{"Awaiting payment Confirmation", "Delivered Successfully"});

	// Add Button
	JButton addBtn = new JButton("Add"),
			updateBtn = new JButton("Update"),
			deleteBtn = new JButton("Delete"),
			loadBtn = new JButton("Load");

	// Add Table
	JTable table;
	DefaultTableModel model;

	public OrdersPanel() {
		setLayout(null);
		String[] labels = {"Order_ID", "User_Id", "Order_Number", "Date", "Status", "Total_Amount", "Payment_Method", "Notes"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(5, 430, 800, 300);
		setVisible(true);

		int y = 20;
		addField("Order_ID", orderidTxt, y);
		y += 30;
		addField("User_Id", useridTxt, y);
		y += 30;
		addField("Order_Number", ordernoTxt, y);
		y += 30;
		addField("Date", dateTxt, y);
		y += 30;
		addField("Total_Amount", totalamountTxt, y);
		y += 30;
		addComoField("Status", statusCmb, y);
		y += 30;
		addComoField("Payment_Method", paymethodCmb, y);
		y += 30;
		addComoField("Notes", notesCmb, y);

		orderidTxt.setEditable(false); // auto increment

		addButtons();
		add(sp);
		loadorders();

		// Fill form when row clicked
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					orderidTxt.setText(model.getValueAt(row, 0).toString());
					useridTxt.setText(model.getValueAt(row, 1).toString());
					ordernoTxt.setText(model.getValueAt(row, 2).toString());
					dateTxt.setText(model.getValueAt(row, 3).toString());
					statusCmb.setSelectedItem(model.getValueAt(row, 4).toString());
					totalamountTxt.setText(model.getValueAt(row, 5).toString());
					paymethodCmb.setSelectedItem(model.getValueAt(row, 6).toString());
					notesCmb.setSelectedItem(model.getValueAt(row, 7).toString());
				}
			}
		});
	}

	private void addButtons() {
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
		deleteBtn.addActionListener(this);
		loadBtn.addActionListener(this);
	}

	private void addComoField(String lbl, JComboBox<String> cmb, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20, y, 120, 25);
		cmb.setBounds(140, y, 150, 25);
		add(l);
		add(cmb);
	}

	private void addField(String lbl, JComponent txt, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20, y, 120, 25);
		txt.setBounds(140, y, 150, 25);
		add(l);
		add(txt);
	}

	public void actionPerformed(ActionEvent e) {
		try (Connection con = DB.getConnection()) {
			if (e.getSource() == addBtn) {
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO orders(user_id, order_number, date, status, total_amount, payment_method, notes) VALUES(?,?,?,?,?,?,?)");
				ps.setString(1, useridTxt.getText());
				ps.setString(2, ordernoTxt.getText());
				ps.setString(3, dateTxt.getText());
				ps.setString(4, statusCmb.getSelectedItem().toString());
				ps.setString(5, totalamountTxt.getText());
				ps.setString(6, paymethodCmb.getSelectedItem().toString());
				ps.setString(7, notesCmb.getSelectedItem().toString());
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Order Added Successfully!");
				loadorders();
				clearFields();
			} else if (e.getSource() == updateBtn) {
				if (orderidTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Select an Order to Update!");
					return;
				}
				PreparedStatement ps = con.prepareStatement(
						"UPDATE orders SET user_id=?, order_number=?, date=?, status=?, total_amount=?, payment_method=?, notes=? WHERE order_id=?");
				ps.setString(1, useridTxt.getText());
				ps.setString(2, ordernoTxt.getText());
				ps.setString(3, dateTxt.getText());
				ps.setString(4, statusCmb.getSelectedItem().toString());
				ps.setString(5, totalamountTxt.getText());
				ps.setString(6, paymethodCmb.getSelectedItem().toString());
				ps.setString(7, notesCmb.getSelectedItem().toString());
				ps.setInt(8, Integer.parseInt(orderidTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Order Updated Successfully!");
				loadorders();
				clearFields();
			} else if (e.getSource() == deleteBtn) {
				if (orderidTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Select an Order to Delete!");
					return;
				}
				PreparedStatement ps = con.prepareStatement("DELETE FROM orders WHERE order_id=?");
				ps.setInt(1, Integer.parseInt(orderidTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Order Deleted Successfully!");
				loadorders();
				clearFields();
			} else if (e.getSource() == loadBtn) {
				loadorders();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
	}

	// Utility to refresh table
	private void loadorders() {
		try (Connection con = DB.getConnection()) {
			model.setRowCount(0);
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM orders");
			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getInt("order_id"),
						rs.getString("user_id"),
						rs.getString("order_number"),
						rs.getString("date"),
						rs.getString("status"),
						rs.getString("total_amount"),
						rs.getString("payment_method"),
						rs.getString("notes")
				});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error Loading Orders: " + ex.getMessage());
		}
	}

	private void clearFields() {
		orderidTxt.setText("");
		useridTxt.setText("");
		ordernoTxt.setText("");
		dateTxt.setText("");
		statusCmb.setSelectedIndex(0);
		totalamountTxt.setText("");
		paymethodCmb.setSelectedIndex(0);
		notesCmb.setSelectedIndex(0);
	}
}
