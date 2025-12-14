package COM.FORM;

import java.awt.BorderLayout;

import javax.swing.*;

import COM.PANEL.*;


public class ECMS extends JFrame {
	JTabbedPane tabs = new JTabbedPane();
	//constructor
	public ECMS(String role, int userid){
		setTitle("E_COMMERCE MONITORING SYSTEM");
		setSize(1000,700);
		setLayout(new BorderLayout());
		if(role.equalsIgnoreCase("admin")){
			tabs.add("Users", new UsersPanel());
			tabs.add("Category", new CategoryPanel());
			tabs.add("Product", new ProductPanel());
			tabs.add("Orders", new OrdersPanel());
			tabs.add("Payment", new PaymentsPanel());
			tabs.add("Order_Payment", new Order_PaymentsPanel());
			tabs.add("Shipments", new ShipmentsPanel());
			
		}else if (role.equalsIgnoreCase("Seller")){
			tabs.add("Category", new CategoryPanel());
			tabs.add("Product", new ProductPanel());
			tabs.add("Order_Payment", new Order_PaymentsPanel());
			tabs.add("Shipments", new ShipmentsPanel());
		}else if (role.equalsIgnoreCase("Customer")){
			tabs.add("Orders", new OrdersPanel());
			tabs.add("Payment", new PaymentsPanel());
			
		}
		add(tabs,BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	

}
