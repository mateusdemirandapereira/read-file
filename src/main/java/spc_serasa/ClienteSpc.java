package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClienteSpc extends Cliente {	
	
	private final String contrato;
	private final LocalDate dataExclusao;
	private final long codigoNotificacao;
	private final int codigoAssociado;

	public ClienteSpc(long codigoSpc, String cpf, String nome, String contrato, LocalDate dataVencimento,
			double valor, LocalDate dataInclusao, LocalTime horaInclusao, LocalDate dataExclusao,
			String tipoNotificacao,	long codigoNotificacao, int codigoAssociado) {
		
		super(codigoSpc, nome, cpf, dataInclusao, horaInclusao, valor, dataVencimento,tipoNotificacao);		
		
		this.contrato = contrato;
		this.dataExclusao = dataExclusao;
		this.codigoNotificacao = codigoNotificacao;
		this.codigoAssociado = codigoAssociado;

	}
	
	public ClienteSpc(String cpf, String contrato) {
		super(cpf);
		this.contrato = contrato;
		this.dataExclusao = null;
		this.codigoNotificacao = 0;
		this.codigoAssociado = 0;
	}

	
	public long getCodigoSpc() {
		return super.getCodigo();
	}
	
	public String getCpfCnpj () {
		return super.getCpf();
	}
	
	public String getConsumidor() {
		return super.getNome();
	}

	public String getContrato() {
		return contrato;
	}
	
	public double getValorDebito() {
		return super.getValor();
	}
	
	public LocalTime getHoraInclusao() {
		return super.getHora();
	}
	
	public LocalDate getDataExclusao() {
		return dataExclusao;
	}
	
	public String getTipoNotificacao() {
		return super.getTipo();
	}

	public long getCodigoNotificacao() {
		return codigoNotificacao;
	}

	public int getCodigoAssociado() {
		return codigoAssociado;
	}

	@Override
	public String toString() {		
		
		return String.format("%d %s %s %s %s %.2f %s %s %s %s %d %d",
				getCodigoSpc(), getCpfCnpj(), getConsumidor(), getContrato(), getDataVencimento(),
				getValorDebito(), getDataInclusao(), getHoraInclusao(), getDataExclusao(),
				getTipoNotificacao(), getCodigoNotificacao(), getCodigoAssociado());
		
	}
		
}	
		