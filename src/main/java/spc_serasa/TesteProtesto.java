package spc_serasa;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class TesteProtesto {
	
	public static String RESOURCE = "protesto.csv";
	
	public static void main(String[] args) {
		
		
		List<String[]> listCliente = Arquivo.ler(RESOURCE);
		
		NegativadoProtesto negativadoProtesto = new NegativadoProtesto();
		
		List<ClienteProtesto> listaClienteProtesto =  negativadoProtesto.adiciona(listCliente);
		Connection conn = new ConnectionFactory().getConnection();
		ProtestoDao protestoDao = new ProtestoDao(conn);
		
		protestoDao.adiciona(listaClienteProtesto);
		
		for (ClienteProtesto cliente : listaClienteProtesto) {
			System.out.println(cliente);
		}
		
	
		
		
	}

}
