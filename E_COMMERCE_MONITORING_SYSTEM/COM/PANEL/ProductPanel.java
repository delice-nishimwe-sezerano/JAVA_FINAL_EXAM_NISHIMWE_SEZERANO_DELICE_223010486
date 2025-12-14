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

public class ProductPanel extends JPanel implements ActionListener {
    // Add Field
    JTextField productTxt = new JTextField(),
            useridTxt = new JTextField(),
            categoryTxt = new JTextField(),
            nameTxt = new JTextField(),
            descrTxt = new JTextField(),
            priceTxt = new JTextField(),
            createdatTxt = new JTextField();
    JComboBox<String> statusCmb = new JComboBox<String>(new String[]{"Active", "Inactive"});

    // Add Button
    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    // Add Table
    JTable table;
    DefaultTableModel model;

    // Constructor
    public ProductPanel() {
        setLayout(null);
        String[] labels = {"Product_Id", "User_Id", "Category_Id", "Name", "Description", "Price", "Status", "Created_At"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(5, 430, 800, 300);
        setVisible(true);

        //
        int y = 20;
        addField("Product_Id", productTxt, y);
        y += 30;
        addField("User_Id", useridTxt, y);
        y += 30;
        addField("Category_Id", categoryTxt, y);
        y += 30;
        addField("Name", nameTxt, y);
        y += 30;
        addField("Description", descrTxt, y);
        y += 30;
        addField("Price", priceTxt, y);
        y += 30;
        addField("Status", statusCmb, y);
        y += 30;
        addField("Created_At", createdatTxt, y);
        y += 30;

        productTxt.setEditable(false);// auto increment

        addButtons();
        add(sp);

        // Load products automatically on startup
        loadproducts();

        // Fill form when a row is clicked
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    productTxt.setText(model.getValueAt(row, 0).toString());
                    useridTxt.setText(model.getValueAt(row, 1).toString());
                    categoryTxt.setText(model.getValueAt(row, 2).toString());
                    nameTxt.setText(model.getValueAt(row, 3).toString());
                    descrTxt.setText(model.getValueAt(row, 4).toString());
                    priceTxt.setText(model.getValueAt(row, 5).toString());
                    statusCmb.setSelectedItem(model.getValueAt(row, 6).toString());
                    createdatTxt.setText(model.getValueAt(row, 7).toString());
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

    public void actionPerformed(ActionEvent e) {
        try {
            Connection con = DB.getConnection();
            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO products(user_id,category_id,name,description,price,status,created_at) VALUES(?,?,?,?,?,?,?)");
                ps.setString(1, useridTxt.getText());
                ps.setString(2, categoryTxt.getText());
                ps.setString(3, nameTxt.getText());
                ps.setString(4, descrTxt.getText());
                ps.setString(5, priceTxt.getText());
                ps.setString(6, statusCmb.getSelectedItem().toString());
                ps.setString(7, createdatTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Products Added!");
                loadproducts();
                clearFields();
                ps.close();
            } else if (e.getSource() == updateBtn) {
                if (productTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select a product to update!");
                    return;
                }
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE products SET user_id=?, category_id=?, name=?, description=?, price=?, status=?, created_at=? " +
                        "WHERE product_id=?");
                ps.setString(1, useridTxt.getText());
                ps.setString(2, categoryTxt.getText());
                ps.setString(3, nameTxt.getText());
                ps.setString(4, descrTxt.getText());
                ps.setString(5, priceTxt.getText());
                ps.setString(6, statusCmb.getSelectedItem().toString());
                ps.setString(7, createdatTxt.getText());
                ps.setInt(8, Integer.parseInt(productTxt.getText())); // Corrected index
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Products Updated!");
                loadproducts();
                clearFields();
                ps.close();
            } else if (e.getSource() == deleteBtn) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE product_id=?");
                ps.setInt(1, Integer.parseInt(productTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Products Deleted");
                loadproducts();
                clearFields();
                ps.close();
            } else if (e.getSource() == loadBtn) {
                loadproducts();
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Utility to refresh table
    private void loadproducts() {
        try {
            Connection con = DB.getConnection();
            model.setRowCount(0);
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM products");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("user_id"),
                        rs.getString("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("price"),
                        rs.getString("status"),
                        rs.getString("created_at")
                });
            }
            rs.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        productTxt.setText("");
        useridTxt.setText("");
        categoryTxt.setText("");
        statusCmb.setSelectedIndex(0);
        nameTxt.setText("");
        descrTxt.setText("");
        priceTxt.setText("");
        createdatTxt.setText("");
    }

}
