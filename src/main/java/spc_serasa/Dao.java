package spc_serasa;

import java.sql.Connection;
import java.util.List;

public abstract class Dao<T> {
	
	protected final Connection conn;
	
	public Dao(Connection conn) {
		this.conn = conn;
	}
	
	public abstract List<T> listar();
	
	public abstract void inserir(List<T> clientes);
	
	public abstract void deletarTodos();
	
	public abstract List<T> pegarNegativado();

}
