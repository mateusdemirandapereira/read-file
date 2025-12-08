package spc_serasa;

import java.sql.Connection;
import java.util.List;

public class TesteSpc {
	
	public static void main(String[] args) {
		Connection conn = new ConnectionFactory().getConnection();
		
		SpcDao spcDao = new SpcDao(conn);
		
		List<ClienteSpc> listaCliente = spcDao.pegarNegativado();
		
		for (ClienteSpc cliente: listaCliente) {
			System.out.println(cliente);
		}
		
	}

}
