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

public class Order_PaymentsPanel extends JPanel implements ActionListener {
    // ADD FIELD
    JTextField orderidTxt = new JTextField(),
            paymentidTxt = new JTextField();

    // Add Button
    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    // Add Table
    JTable table;
    DefaultTableModel model;

    public Order_PaymentsPanel() {
        setLayout(null);
        String[] labels = {"Order_ID", "Payment_ID"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(5, 430, 800, 300);
        setVisible(true);

        int y = 20;
        addField("Order_ID", orderidTxt, y);
        y += 30;
        addField("Payment_ID", paymentidTxt, y);
        y += 30;

        orderidTxt.setEditable(false); // auto increment

        addButtons();
        add(sp);

        // Load automatically on startup
        loadorder_payment();

        // Fill form when row clicked
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    orderidTxt.setText(model.getValueAt(row, 0).toString());
                    paymentidTxt.setText(model.getValueAt(row, 1).toString());
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

    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO order_payment(payment_id) VALUES(?)"
                );
                ps.setString(1, paymentidTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Order Payment Added!");
                loadorder_payment();
                clearFields();
            }

            else if (e.getSource() == updateBtn) {
                if (orderidTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select an order to update!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE order_payment SET payment_id=? WHERE order_id=?"
                );
                ps.setString(1, paymentidTxt.getText());
                ps.setInt(2, Integer.parseInt(orderidTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Order Payment Updated!");
                loadorder_payment();
                clearFields();
            }

            else if (e.getSource() == deleteBtn) {
                if (orderidTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select an order to delete!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM order_payment WHERE order_id=?"
                );
                ps.setInt(1, Integer.parseInt(orderidTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Order Payment Deleted!");
                loadorder_payment();
                clearFields();
            }

            else if (e.getSource() == loadBtn) {
                loadorder_payment();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Utility to refresh table
    private void loadorder_payment() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM order_payment");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("payment_id"),
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Utility to clear input fields
    private void clearFields() {
        orderidTxt.setText("");
        paymentidTxt.setText("");
    }
}
