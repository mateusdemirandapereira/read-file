package spc_serasa;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SerasaDao extends Dao<ClienteSerasa> {

	public SerasaDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<ClienteSerasa> listar() {
		String sql = "select * from serasa";
		List<ClienteSerasa> lista = new ArrayList<>();

		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			ResultSet resultSet = pstmp.executeQuery();
			
			while(resultSet.next()) {
				String status = resultSet.getString(1);
				long id = DaoUtil.getLong(resultSet, 2);
				String nomeDevedor = resultSet.getString(3);
				String tipoPessoa = resultSet.getString(4);
				String documento = resultSet.getString(5);
				String natureza = resultSet.getString(6);
				double valor = DaoUtil.getDouble(resultSet,7);
				LocalDate dataCadastro = DaoUtil.getDate(resultSet, 8);
				LocalTime horaCadastro = DaoUtil.getTime(resultSet,9);
				LocalDate dataVencimento = DaoUtil.getDate(resultSet,10);
				String operacao = resultSet.getString(11);
				
				ClienteSerasa clienteSerasa = new ClienteSerasa(status, id, nomeDevedor, tipoPessoa,
						documento, natureza, valor, dataCadastro, horaCadastro, dataVencimento, operacao); 
				lista.add(clienteSerasa);
				
			}
			
			return lista;
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar Serasa", e);
			
		}
	}
	
	@Override
	public void inserir(List<ClienteSerasa> clientes) {
		
		String sql = "insert into serasa(serasa_status, serasa_codigo, serasa_nome, serasa_tipo,"
				+ "serasa_cpf, serasa_natureza, serasa_valor, serasa_data_cadastro,serasa_hora_cadastro,"
				+ "serasa_data_vencimento, serasa_operacao)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {
			
			for (ClienteSerasa cliente : clientes) {
				pstmp.setString(1, cliente.getStatus());
				pstmp.setLong(2, cliente.getId());
				pstmp.setString(3, cliente.getNomeDevedor());
				pstmp.setString(4, cliente.getTipoPessoa());
				pstmp.setString(5, cliente.getDocumento());
				pstmp.setString(6, cliente.getNatureza());
				pstmp.setDouble(7, cliente.getValor());
				pstmp.setDate(8, Date.valueOf(cliente.getDataCadastro()));
				pstmp.setTime(9, Time.valueOf(cliente.getHora()));
				pstmp.setDate(10, Date.valueOf(cliente.getDataVencimento()));
				pstmp.setString(11, cliente.getOperacao());
				
				pstmp.addBatch();
			}
			
			pstmp.executeBatch();
			
						
		} catch(SQLException e) {
			
			throw new RuntimeException("Erro ao inserir Serasa", e);
		}
		
	}
	
	@Override
	public void deletarTodos () {
	String sql = "delete from serasa";
		
		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {
		
			int registros = pstmp.executeUpdate();
			
			System.out.println(registros + " registros excluidos do Serasa.");
			
		} catch(SQLException e) {
			
			throw new RuntimeException("Erro ao excluir Serasa", e);
			
		}
		
	}
	
	@Override
	public List<ClienteSerasa> pegarNegativado() {
		
		String sql = "select distinct serasa.serasa_cpf from serasa where serasa.serasa_operacao != \"Baixar\"";
		
		List<ClienteSerasa> listaClientes = new ArrayList<>();
		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {
			
			ResultSet resultSet = pstmp.executeQuery();
			
			while(resultSet.next()) {
				String cpf = resultSet.getString(1);
				ClienteSerasa clienteSerasa = new ClienteSerasa(cpf);
				
				listaClientes.add(clienteSerasa);
			}
			
			return listaClientes;
			
		} catch(SQLException ex) {
			throw new RuntimeException("Não há clientes negativados no SPC.",ex);
		}
		
		
	}
	
	public void atualizaNegativado(List<ClienteSerasa> clientes) {
		
	}
 
}
