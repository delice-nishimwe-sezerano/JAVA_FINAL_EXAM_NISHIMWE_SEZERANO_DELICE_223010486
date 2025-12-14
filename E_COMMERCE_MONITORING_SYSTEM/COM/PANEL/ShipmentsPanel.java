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


public class ShipmentsPanel extends JPanel implements ActionListener {
	JTextField shipidTxt = new JTextField(),
			payidTxt = new JTextField(),
			useridTxt = new JTextField(),
			arb2Txt = new JTextField(),
			createdatTxt = new JTextField();
	JComboBox<String> statusCmb = new JComboBox<>(new String[]{"IN Transit", "Delivered"});
	JComboBox<String> arb1Cmb = new JComboBox<>(new String[]{"FedEx", "DHL Express"});

	// Add Button
	JButton addBtn = new JButton("Add"),
			updateBtn = new JButton("Update"),
			deleteBtn = new JButton("Delete"),
			loadBtn = new JButton("Load");

	// Add Table
	JTable table;
	DefaultTableModel model;

	//Constructor
	public ShipmentsPanel(){
		setLayout(null);
		String[] labels = {"Shipment_Id","Payment_Id","User_Id","Carrier_Name","Tracking_Number","Status","Created_At"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(5,430,800,300);
		setVisible(true);

		//
		int y = 20;
		addField("Shipment_Id",shipidTxt,y); y +=30;
		addField("Payment_Id",payidTxt,y); y +=30;
		addField("User_Id",useridTxt,y); y +=30;
		addField("Carrier_Name",arb1Cmb,y); y +=30;
		addField("Tracking_Number",arb2Txt,y); y +=30;
		addField("Status", statusCmb, y); y += 30;
		addField("Created_At",createdatTxt,y); y +=30;

		shipidTxt.setEditable(false);//auto increment

		addButtons();
		add(sp);
		//Load users automatically on startup
		loadshipments();


		//Fill Form when a row is clicked
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				int row = table.getSelectedRow();
				if (row >=0){
					shipidTxt.setText(model.getValueAt(row, 0).toString());
					payidTxt.setText(model.getValueAt(row, 1).toString());
					useridTxt.setText(model.getValueAt(row, 2).toString());
					arb1Cmb.setSelectedItem(model.getValueAt(row, 3).toString());
					arb2Txt.setText(model.getValueAt(row, 4).toString());
					statusCmb.setSelectedItem(model.getValueAt(row, 5).toString());
					createdatTxt.setText(model.getValueAt(row, 6).toString());

				}
			}
		});
	}
	private void addButtons() {
		addBtn.setBounds(300,20,100,30);
		updateBtn.setBounds(300,60,100,30);
		deleteBtn.setBounds(300,100,100,30);
		loadBtn.setBounds(300,140,100,30);
		add(addBtn);add(updateBtn);add(deleteBtn);add(loadBtn);
		addBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		loadBtn.addActionListener(this);

	}


	private void addField(String lbl, JComponent txt, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20,y,80,25);
		txt.setBounds(100,y,150,25);
		add(l); add(txt);
	}


	public void actionPerformed(ActionEvent e) {
		try(Connection con = DB.getConnection()){
			if(e.getSource()==addBtn){
				PreparedStatement ps = con.prepareStatement("INSERT INTO shipments(payment_id,user_id,carrier_name,tracking_number,status,created_at) VALUES(?,?,?,?,?,?)");
				ps.setString(1, payidTxt.getText());
				ps.setString(2, useridTxt.getText());
				ps.setString(5, arb1Cmb.getSelectedItem().toString());
				ps.setString(4, arb2Txt.getText());
				ps.setString(5, statusCmb.getSelectedItem().toString());
				ps.setString(6, createdatTxt.getText());
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this,"shipments Added!");
				loadshipments();
			}
			else if (e.getSource()==updateBtn){
				PreparedStatement ps = con.prepareStatement("UPDATE shipments SET payment_id=?,user_id=?,carrier_name=?,tracking_number=?,status=?,created_at=? WHERE shipment_id=?");
				ps.setString(1, payidTxt.getText());
				ps.setString(2, useridTxt.getText());
				ps.setString(5, arb1Cmb.getSelectedItem().toString());
				ps.setString(4, arb2Txt.getText());
				ps.setString(5, statusCmb.getSelectedItem().toString());
				ps.setString(6, createdatTxt.getText());
				ps.setInt(7, Integer.parseInt(shipidTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this,"shipments Updated!");
				loadshipments();
			}
			else if (e.getSource()==deleteBtn){
				PreparedStatement ps = con.prepareStatement("DELETE FROM shipments WHERE shipment_id=?");
				ps.setInt(1, Integer.parseInt(shipidTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this,"shipments Deleted");
				loadshipments();
			}
			else if(e.getSource() ==loadBtn){
				loadshipments();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}



	}
	//Utility to refresh table
	private void loadshipments() {
		try(Connection con = DB.getConnection()){
			model.setRowCount(0);
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM shipments");
			while (rs.next()){
				model.addRow(new Object[]{
						rs.getInt("shipment_id"),
						rs.getString("payment_id"),
						rs.getString("user_id"),
						rs.getString("carrier_name"),
						rs.getString("tracking_number"),
						rs.getString("status"),
						rs.getString("created_at"),
				});
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
}
