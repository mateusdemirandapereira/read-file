package spc_serasa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TestSerasa {
	
	public static String RESOURCE = "serasa.csv";
	
	public static void main(String[] args) throws SQLException {
		
		Arquivo arquivo = new Arquivo();
		
		List<String[]> listaClientesSerasa = arquivo.ler(RESOURCE);
		ConnectionFactory connectionFactory = new ConnectionFactory();
		
		Connection conn = connectionFactory.getConnection();
		
		NegativadoSerasa negativado = new NegativadoSerasa();
		
		List<ClienteSerasa> cliente = negativado.adiciona(listaClientesSerasa);
		
		SerasaDao serasa = new SerasaDao(conn);
		
		for (ClienteSerasa cl : cliente) {
			serasa.adiciona(cliente);
			
		}
		conn.close();
		
		
			
		
		
	}

}
