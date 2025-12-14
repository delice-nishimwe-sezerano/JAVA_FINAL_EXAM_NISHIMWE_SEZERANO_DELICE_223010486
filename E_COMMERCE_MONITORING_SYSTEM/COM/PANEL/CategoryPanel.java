package COM.PANEL;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import COM.UTIL.DB;

public class CategoryPanel extends JPanel implements ActionListener {
    // Fields
    JTextField categoryIdTxt = new JTextField(),
            nameTxt = new JTextField(),
            descriptionTxt = new JTextField(),
            createdAtTxt = new JTextField(),
            updatedAtTxt = new JTextField();
    JComboBox<String> statusCmb = new JComboBox<>(new String[]{"Active", "Inactive"});

    // Buttons
    JButton addBtn = new JButton("Add"),
            updateBtn = new JButton("Update"),
            deleteBtn = new JButton("Delete"),
            loadBtn = new JButton("Load");

    // Table
    JTable table;
    DefaultTableModel model;

    public CategoryPanel() {
        setLayout(null);
        String[] labels = {"Category_ID", "Category_Name", "Description", "Status", "Created_At", "Updated_At"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(5, 430, 800, 300);
        add(sp);
        setVisible(true);

        int y = 20;
        addField("Category_ID", categoryIdTxt, y); y += 30;
        addField("Category_Name", nameTxt, y); y += 30;
        addField("Description", descriptionTxt, y); y += 30;
        addField("Status", statusCmb, y); y += 30;
        addField("Created_At", createdAtTxt, y); y += 30;
        addField("Updated_At", updatedAtTxt, y); y += 30;

        categoryIdTxt.setEditable(false); // Auto increment ID

        addButtons();
        loadCategories();

        // Fill form on table click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    categoryIdTxt.setText(model.getValueAt(row, 0).toString());
                    nameTxt.setText(model.getValueAt(row, 1).toString());
                    descriptionTxt.setText(model.getValueAt(row, 2).toString());
                    statusCmb.setSelectedItem(model.getValueAt(row, 3).toString());
                    createdAtTxt.setText(model.getValueAt(row, 4).toString());
                    updatedAtTxt.setText(model.getValueAt(row, 5).toString());
                }
            }
        });
    }

    private void addField(String lbl, JComponent txt, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 100, 25);
        txt.setBounds(130, y, 180, 25);
        add(l);
        add(txt);
    }

    private void addButtons() {
        addBtn.setBounds(350, 20, 100, 30);
        updateBtn.setBounds(350, 60, 100, 30);
        deleteBtn.setBounds(350, 100, 100, 30);
        loadBtn.setBounds(350, 140, 100, 30);

        add(addBtn);
        add(updateBtn);
        add(deleteBtn);
        add(loadBtn);

        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {

            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO categories (category_name, description, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?)"
                );
                ps.setString(1, nameTxt.getText());
                ps.setString(2, descriptionTxt.getText());
                ps.setString(3, statusCmb.getSelectedItem().toString());
                ps.setString(4, createdAtTxt.getText());
                ps.setString(5, updatedAtTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category added successfully!");
                loadCategories();
                clearFields();
            } 
            else if (e.getSource() == updateBtn) {
                if (categoryIdTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select a category to update!");
                    return;
                }
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE categories SET category_name=?, description=?, status=?, created_at=?, updated_at=? WHERE category_id=?"
                );
                ps.setString(1, nameTxt.getText());
                ps.setString(2, descriptionTxt.getText());
                ps.setString(3, statusCmb.getSelectedItem().toString());
                ps.setString(4, createdAtTxt.getText());
                ps.setString(5, updatedAtTxt.getText());
                ps.setInt(6, Integer.parseInt(categoryIdTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category updated successfully!");
                loadCategories();
                clearFields();
            } 
            else if (e.getSource() == deleteBtn) {
                if (categoryIdTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select a category to delete!");
                    return;
                }
                PreparedStatement ps = con.prepareStatement("DELETE FROM categories WHERE category_id=?");
                ps.setInt(1, Integer.parseInt(categoryIdTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category deleted successfully!");
                loadCategories();
                clearFields();
            } 
            else if (e.getSource() == loadBtn) {
                loadCategories();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadCategories() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM categories");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + ex.getMessage());
        }
    }

    private void clearFields() {
        categoryIdTxt.setText("");
        nameTxt.setText("");
        descriptionTxt.setText("");
        statusCmb.setSelectedIndex(0);
        createdAtTxt.setText("");
        updatedAtTxt.setText("");
    }
}
