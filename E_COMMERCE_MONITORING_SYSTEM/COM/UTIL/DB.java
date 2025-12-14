package COM.UTIL;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
	public static Connection getConnection()throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/e_commerce_monitoring_system_db","root","");
	}
	


}
