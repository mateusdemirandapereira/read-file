package spc_serasa;

import java.sql.Connection;
import java.util.List;

public class TesteSpc {
	
	public static void main(String[] args) {
		Connection conn = new ConnectionFactory().getConnection();
		
		SpcDao spcDao = new SpcDao(conn);
		
		List<ClienteSpc> listaCliente = spcDao.pegarNegativado();
		
		Connection conn2 = new ConnectionFactory().getConnection("db2");
		
		SpcDao spcDao2 = new SpcDao(conn2);
		
		spcDao2.atualizaNegativado(listaCliente);
		
	}

}
