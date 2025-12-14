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

public class PaymentsPanel extends JPanel implements ActionListener {
	// Add Fields
	JTextField paymentTxt = new JTextField(),
			amountTxt = new JTextField(),
			dateTxt = new JTextField(),
			refTxt = new JTextField();
	JComboBox<String> statusCmb = new JComboBox<>(new String[]{"Pending", "Paid"});
	JComboBox<String> typeCmb = new JComboBox<>(new String[]{"Card", "MOMO"});

	// Add Buttons
	JButton addBtn = new JButton("Add"),
			updateBtn = new JButton("Update"),
			deleteBtn = new JButton("Delete"),
			loadBtn = new JButton("Load");

	// Add Table
	JTable table;
	DefaultTableModel model;

	// Constructor
	public PaymentsPanel() {
		setLayout(null);
		String[] labels = {"Payment_Id", "Amount", "Date", "Type", "Reference", "Status"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(5, 430, 800, 300);
		setVisible(true);

		int y = 20;
		addField("Payment_Id", paymentTxt, y);
		y += 30;
		addField("Amount", amountTxt, y);
		y += 30;
		addField("Date", dateTxt, y);
		y += 30;
		addField("Type", typeCmb, y);
		y += 30;
		addField("Reference", refTxt, y);
		y += 30;
		addField("Status", statusCmb, y);
		y += 30;

		paymentTxt.setEditable(false); // auto increment

		addButtons();
		add(sp);
		// Load payments automatically on startup
		loadpayments();

		// Fill form when a row is clicked
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					paymentTxt.setText(model.getValueAt(row, 0).toString());
					amountTxt.setText(model.getValueAt(row, 1).toString());
					dateTxt.setText(model.getValueAt(row, 2).toString());
					typeCmb.setSelectedItem(model.getValueAt(row, 3).toString());
					refTxt.setText(model.getValueAt(row, 4).toString());
					statusCmb.setSelectedItem(model.getValueAt(row, 5).toString());
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

	private void addField(String lbl, JComponent txt, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20, y, 80, 25);
		txt.setBounds(100, y, 150, 25);
		add(l);
		add(txt);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try (Connection con = DB.getConnection()) {
			if (e.getSource() == addBtn) {
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO payments(amount, date, type, reference, status) VALUES(?,?,?,?,?)"
						);
				ps.setString(1, amountTxt.getText());
				ps.setString(2, dateTxt.getText());
				ps.setString(3, typeCmb.getSelectedItem().toString());
				ps.setString(4, refTxt.getText());
				ps.setString(5, statusCmb.getSelectedItem().toString());
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Payment Added!");
				loadpayments();
				clearFields();
			} 
			else if (e.getSource() == updateBtn) {
				if (paymentTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Select a payment to update!");
					return;
				}

				PreparedStatement ps = con.prepareStatement(
						"UPDATE payments SET amount=?, date=?, type=?, reference=?, status=? WHERE payment_id=?"
						);
				ps.setString(1, amountTxt.getText());
				ps.setString(2, dateTxt.getText());
				ps.setString(3, typeCmb.getSelectedItem().toString());
				ps.setString(4, refTxt.getText());
				ps.setString(5, statusCmb.getSelectedItem().toString());
				ps.setInt(6, Integer.parseInt(paymentTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Payment Updated!");
				loadpayments();
				clearFields();
			} 
			else if (e.getSource() == deleteBtn) {
				if (paymentTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Select a payment to delete!");
					return;
				}

				PreparedStatement ps = con.prepareStatement("DELETE FROM payments WHERE payment_id=?");
				ps.setInt(1, Integer.parseInt(paymentTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Payment Deleted!");
				loadpayments();
				clearFields();
			} 
			else if (e.getSource() == loadBtn) {
				loadpayments();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
	}

	private void loadpayments() {
		try (Connection con = DB.getConnection()) {
			model.setRowCount(0);
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM payments");
			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getInt("payment_id"),
						rs.getString("amount"),
						rs.getString("date"),
						rs.getString("type"),
						rs.getString("reference"),
						rs.getString("status")
				});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void clearFields() {
		paymentTxt.setText("");
		amountTxt.setText("");
		dateTxt.setText("");
		typeCmb.setSelectedIndex(0);
		refTxt.setText("");
		statusCmb.setSelectedIndex(0);
	}
}
