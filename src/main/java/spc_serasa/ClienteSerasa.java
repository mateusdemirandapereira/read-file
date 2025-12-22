package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;


public class ClienteSerasa extends Cliente {
	
	private final String status;
	private final String natureza;
	private final String operacao;
	
	public ClienteSerasa(String cpf) {
		super(cpf);
		this.status = null;
		this.natureza = null;
		this.operacao = null;
	}
	
	public ClienteSerasa(String status, long id, String nome,String tipo, String documento,String natureza,
			double valor,LocalDate dataCadastro,LocalTime hora,LocalDate dataVencimento,String operacao) {
		
		super(id, nome, documento, dataCadastro, hora, valor, dataVencimento, tipo);
		this.status = status;
		this.natureza = natureza;
		this.operacao = operacao;
	}
		
	public String getStatus() {
		return status;
	}
	
	public long getId() {
		return super.getCodigo();
	}
	
	public String getNomeDevedor() {
		return super.getNome();
	}
	public String getTipoPessoa() {
		return super.getTipo();
	}
	
	public String getDocumento() {
		return super.getCpf();
	}

	public String getNatureza() {
		return natureza;
	}
	
	public LocalDate getDataCadastro() {
		return super.getDataInclusao();
	}

	public String getOperacao() {
		return operacao;
	}
	
	@Override
	public String toString() {
		
		return String.format("%s %d %s %s %s %s %.2f %s %s %s %s",
				getStatus(), getId(),getNomeDevedor(), getTipoPessoa(), getDocumento(), getNatureza(),
				getValor(), getDataCadastro(),getHora(), getDataVencimento(), getOperacao());
	}
	
	
		

}
