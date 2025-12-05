package spc_serasa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	
	private String url = "jdbc:mysql://localhost:3306/conciliacao?useSSl=false&serverTimezone=America/Sao_Paulo";
	private String user = "root";
	private String password = "dreamcast";
	
	public Connection getConnection() {
		
		try {
			return DriverManager.getConnection(url, user, password);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}				
		
	}
	
	

}
