package spc_serasa;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProtestoDao extends Dao<ClienteProtesto> {

	public ProtestoDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<ClienteProtesto> listar() {
		String sql = "select * from protesto";
		List<ClienteProtesto> lista = new ArrayList<>();

		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			ResultSet resultSet = pstmp.executeQuery();

			while (resultSet.next()) {
				long pedido = resultSet.getLong(1);
				String comarcaCartorio = resultSet.getString(2);
				LocalDate dataSolicitacao = DaoUtil.getDate(resultSet,3);
				String comarcaDevedor = resultSet.getString(4);
				String devedor = resultSet.getString(5);
				String docDevedor = resultSet.getString(6);
				String numeroTitulo = resultSet.getString(7);
				double valorTitulo = DaoUtil.getDouble(resultSet,8);
				double valorProtesto = DaoUtil.getDouble(resultSet,9);
				long protocolo = DaoUtil.getLong(resultSet,10);
				LocalDate dataProtocolo = DaoUtil.getDate(resultSet, 11); 
				String especie = resultSet.getString(12);
				String statusPedido = resultSet.getString(13);
				String irregularidade = resultSet.getString(14);
				String ocorrenciaTitulo = resultSet.getString(15);
				LocalDate dataOcorrencia = DaoUtil.getDate(resultSet,16); 
				 
				
				ClienteProtesto clienteProtesto = new ClienteProtesto(pedido, comarcaCartorio, dataSolicitacao, comarcaDevedor,
						devedor, docDevedor, numeroTitulo, valorTitulo, valorProtesto, protocolo, dataProtocolo,
						especie, statusPedido, irregularidade, ocorrenciaTitulo, dataOcorrencia);
				lista.add(clienteProtesto);
			}

			return lista;

		} catch (SQLException e) {
			
			throw new RuntimeException("Erro ao listar clientes protestados", e);

		}
	}
	@Override
	public void inserir(List<ClienteProtesto> clientes) {

		String sql = " insert into protesto(pedido, comarcaCartorio, dataSolicitacao, comarcadevedor, devedor, docDevedor, numeroTitulo,\r\n"
				+ "		valorTitulo, valorProtesto, protocolo, dataProtocolo, especie, statusPedido, irregularidade, ocorrenciaTitulo,\r\n"
				+ "        dataOcorrencia) \r\n" + "        values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			for (ClienteProtesto cliente : clientes) {
				pstmp.setLong(1, cliente.getPedido());
				pstmp.setString(2, cliente.getComarcaCartorio());
				pstmp.setDate(3, toSqlDate(cliente.getDataSolicitacao()));
				pstmp.setString(4, cliente.getComarcaDevedor());
				pstmp.setString(5, cliente.getDevedor());
				pstmp.setString(6, cliente.getDocDevedor());
				pstmp.setString(7, cliente.getNumeroTitulo());
				pstmp.setDouble(8, cliente.getValorTitulo());
				pstmp.setDouble(9, cliente.getValorProtesto());
				pstmp.setLong(10, cliente.getProtocolo());
				
				pstmp.setDate(11, toSqlDate(cliente.getDataProtocolo()));
				pstmp.setString(12, cliente.getEspecie());
				pstmp.setString(13, cliente.getStatusPedido());
				pstmp.setString(14, cliente.getIrregularidade());
				pstmp.setString(15, cliente.getOcorrenciaTitulo());
				 
				pstmp.setDate(16, toSqlDate(cliente.getDataOcorrencia()));

				pstmp.addBatch();
			}

			pstmp.executeBatch();


		} catch (SQLException e) {
			
			throw new RuntimeException("Erro ao inserir clientes protestados", e);
		}

	}
	@Override
	public void deletarTodos() {
		String sql = "delete from protesto";

		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			int registros = pstmp.executeUpdate();

			System.out.println(registros + " registros deletados");

		} catch (SQLException e) {
			
			throw new RuntimeException("Erro ao deletar registros de protesto", e);

		}

	}
	
	
	public List<ClienteProtesto> pegarNegativado() {
		
		String sql =  "select distinct if(position(\"-\" in protesto.numeroTitulo) > 0,"
				+ 				" substring_index(protesto.numeroTitulo, \"-\", 1),"
				+ 				" protesto.numeroTitulo) as numeroTitulo "
				+               " from protesto "
				+ "where substring_index(protesto.ocorrenciaTitulo, \"-\", 1) = 2";
		List<ClienteProtesto> listaClienteProtesto = new ArrayList<>();
		
		try(PreparedStatement pstmp = conn.prepareStatement(sql)) {
			
			ResultSet resultSet = pstmp.executeQuery();
			
			while (resultSet.next()) {
				String numeroTitulo = resultSet.getString(1);
				
				ClienteProtesto clienteProtesto = new ClienteProtesto(numeroTitulo);
				listaClienteProtesto.add(clienteProtesto);
			}
			
			return listaClienteProtesto;
			
		}catch (SQLException ex) {
			throw new RuntimeException("Não há Clientes negativados no Protesto!", ex);
		}
		
		
	}
	
	public void atualizaNegativado(List<ClienteProtesto> clientes) {
		
		String sql = "update custp set custp.bits3 = ? "
				+ "where custp.no in(select distinct inst.custno from inst where inst.contrno = ?)";
		
		final int SITUACAOCLIENTE = 13;
		
		try(PreparedStatement pstmp = conn.prepareStatement(sql)) {
			
			for (ClienteProtesto cliente : clientes) {
				long numeroTitulo = Long.parseLong(cliente.getNumeroTitulo().trim());
				
				pstmp.setInt(1, SITUACAOCLIENTE);
				pstmp.setLong(2, numeroTitulo);
				
				
				pstmp.addBatch();
			}
			
			pstmp.executeBatch();
			
		} catch(SQLException ex) {
			throw new RuntimeException("Erro ao atualizar clientes Protestado", ex);
		}
		
		
		
		
	}
	
	
	private Date toSqlDate(LocalDate data) {
		return data != null ? Date.valueOf(data) : null;
	}

}
