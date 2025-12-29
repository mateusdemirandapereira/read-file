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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SpcDao extends Dao<ClienteSpc> {

	public SpcDao(Connection conn) {
		super(conn);
	}

	@Override
	public List<ClienteSpc> listar() {
		String sql = "select * from spc";
		List<ClienteSpc> lista = new ArrayList<>();
		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			ResultSet resultSet = pstmp.executeQuery();

			while (resultSet.next()) {
				long codigoSpc = DaoUtil.getLong(resultSet, 1);
				String cpfCnpj = resultSet.getString(2);
				String consumidor = resultSet.getString(3);
				String contrato = resultSet.getString(4);
				LocalDate dataVencimento = DaoUtil.getDate(resultSet, 5);
				double valorDebito = DaoUtil.getDouble(resultSet, 6);
				LocalDate dataInclusao = DaoUtil.getDate(resultSet, 7);
				LocalTime horaInclusao = DaoUtil.getTime(resultSet, 8);
				LocalDate dataExclusao = DaoUtil.getDate(resultSet, 9);
				String tipoNotificacao = resultSet.getString(10);
				long codigoNotificacao = DaoUtil.getLong(resultSet, 11);
				int codigoAssociado = DaoUtil.getInt(resultSet, 12);

				ClienteSpc clienteSpc = new ClienteSpc(codigoSpc, cpfCnpj, consumidor, contrato, dataVencimento,
						valorDebito,	dataInclusao, horaInclusao, dataExclusao, tipoNotificacao, codigoNotificacao, codigoAssociado);
				lista.add(clienteSpc);
			}

			return lista;

		} catch (SQLException e) {

			throw new RuntimeException("Erro ao listar SPC.", e);

		}

	}

	@Override
	public void inserir(List<ClienteSpc> clientes) {

		String sql = "insert into spc(spc_codigo, spc_cpf, spc_cliente_nome,"
				+ "spc_contrato_parcela, spc_data_vencimento, spc_valor_debito,"
				+ "spc_data_inclusao, spc_hora_inclusao, spc_data_exclusao,"
				+ "spc_tipo_notificacao, spc_codigo_notificacao, spc_codigo_associado)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			for (ClienteSpc cliente : clientes) {
				pstmp.setLong(1, cliente.getCodigoSpc());
				pstmp.setString(2, cliente.getCpfCnpj());
				pstmp.setString(3, cliente.getConsumidor());
				pstmp.setString(4, cliente.getContrato());
				pstmp.setDate(5, Date.valueOf(cliente.getDataVencimento()));
				pstmp.setDouble(6, cliente.getValorDebito());
				pstmp.setDate(7, Date.valueOf(cliente.getDataInclusao()));
				pstmp.setTime(8, Time.valueOf(cliente.getHoraInclusao()));

				LocalDate dataExclusao = cliente.getDataExclusao();
				pstmp.setDate(9, dataExclusao != null ? Date.valueOf(dataExclusao) : null);

				pstmp.setString(10, cliente.getTipoNotificacao());
				pstmp.setLong(11, cliente.getCodigoNotificacao());
				pstmp.setInt(12, cliente.getCodigoAssociado());

				pstmp.addBatch();

			}

			pstmp.executeBatch();

		} catch (SQLException e) {

			throw new RuntimeException("Erro ao inserir SPC.", e);

		}

	}

	@Override
	public void deletarTodos() {
		String sql = "delete from spc";

		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {

			int registros = pstmp.executeUpdate();

			System.out.println(registros + " registros excluidos do SPC.");

		} catch (SQLException e) {

			throw new RuntimeException("Erro ao excluir SPC.", e);

		}
	}
	
	@Override 
	public List<ClienteSpc> pegarNegativado() {
		String sql = "select spc.spc_cpf, spc.spc_contrato_parcela from spc where spc.spc_data_exclusao is null";
		
		List<ClienteSpc> lista = new ArrayList<>();
		try (PreparedStatement pstmp = conn.prepareStatement(sql)) {
			
			ResultSet resultSet = pstmp.executeQuery();
			
			while(resultSet.next()) {
				String cpf = resultSet.getString(1);
				String contrato = resultSet.getString(2);
				
				ClienteSpc clienteSpc = new ClienteSpc(cpf, contrato);
				
				lista.add(clienteSpc);
			}
			
			return lista;
			
		} catch(SQLException ex) {
			throw new RuntimeException("Não há clientes negativados no SPC.", ex);
		}
		
	}
	

	public void atualizaNegativado(List<ClienteSpc> clientes) {
		
		String sql1 = "update itxa set itxa.status = 3 where itxa.contrno = ? and itxa.instno = ?";
		
		String sql2 = "update itxa set itxa.status = case "
				+ "when datediff(date_format(current_date(), \"%Y%m%d\"), itxa.duedate) >= 30 then 3 "
				+ "when datediff(date_format(current_date(), \"%Y%m%d\"), itxa.duedate) < 30 and itxa.status = 4 then 4 "
				+ "else 0 "
				+ "end   where itxa.contrno = ? and itxa.status not in(?, ?)";
		
		
		boolean autoComitOriginal = true;
		
		final int  STATUSCANCELADO = 5;
		final int  STATUSQUITADO = 1;
		
		try {
			try(PreparedStatement psm1 = conn.prepareStatement(sql1);
					PreparedStatement psm2 = conn.prepareStatement(sql2);) {
					long contrato;
					int parcela;
					
					for (ClienteSpc cliente : clientes) {
						
						try {
							contrato = UtilCliente.separaContrato(cliente.getContrato());
							 parcela = UtilCliente.separaParcela(cliente.getContrato());
						} catch(NumberFormatException ex1) {
							continue;
						}
						 
						
						if (contrato > 0 && parcela > 0) {
							psm1.setLong(1, contrato);
							psm1.setInt(2, parcela);
							psm1.addBatch();
						} else if (contrato > 0 && parcela <= 0) {
							psm2.setLong(1, contrato);
							psm2.setInt(2, STATUSCANCELADO);
							psm2.setInt(3, STATUSQUITADO);
							psm2.addBatch();
						}
					}
					
					psm1.executeBatch();
					psm2.executeBatch();
					
					conn.commit();
					
				}
			
		} catch(SQLException ex1) {
			try {
				conn.rollback();
			} catch(SQLException ex2) {
				throw new RuntimeException("Erro ao realizar rollback", ex2);
			}
			throw new RuntimeException("Erro ao atualizar clientes negativados do spc", ex1);
		} finally {
			try {
				conn.setAutoCommit(autoComitOriginal);
			} catch(SQLException ex) {
				throw new RuntimeException("Erro ao restaurar autocommit", ex);
			}
		}
		
		
		
	}
	
	

}
