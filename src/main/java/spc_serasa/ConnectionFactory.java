package spc_serasa;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	
	private final Properties props = new Properties();
	
	{
		try {
	
		InputStream input = ConnectionFactory.class.getClassLoader()
				.getResourceAsStream("config.properties");
		
		if (input == null) {
			throw new RuntimeException("Arquivo config.properties não encotrado!");
		}
		
		props.load(input);
		
	} catch(Exception ex) {
		throw new RuntimeException("Erro ao carregar config.properties", ex);
	}
	}
	
	
	public Connection getConnection() {
		return getConnection("db1");
	}
	
	public Connection getConnection(String database) {
		
		try {
			String url = props.getProperty(database + ".url");
			String user = props.getProperty(database + ".user");
			String password = props.getProperty(database + ".password");
			
			if (url == null) {
				throw new RuntimeException("Configuração do bando '" + database
						+ "' não encontrada!");
			}
					
			return DriverManager.getConnection(url, user, password);
		} catch(SQLException ex) {
			throw new RuntimeException("Erro ao conectar ao banco " + database, ex);
		}				
		
	}
	
	

}
