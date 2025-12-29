package spc_serasa;

import java.time.LocalDate;


public class ClienteProtesto extends Cliente {
	
	private final String comarcaCartorio;
	private final LocalDate dataSolicitacao;
	private final String comarcaDevedor;
	private final String numeroTitulo;
	private final double valorProtesto;
	private final long protocolo;
	private final String statusPedido;
	private final String irregularidade;
	private final String ocorrenciaTitulo;
	
	public ClienteProtesto(String numeroTitulo) {
		this.comarcaCartorio = null;
		this.dataSolicitacao = null;
		this.comarcaDevedor = null;
		this.numeroTitulo = numeroTitulo;
		this.valorProtesto = 0d;
		this.protocolo = 0l;
		this.statusPedido = null;
		this.irregularidade = null;
		this.ocorrenciaTitulo = null;
	}
	
	public ClienteProtesto(long pedido, String comarcaCartorio, LocalDate dataSolicitacao,
			String comarcaDevedor, String nomeDevedor, String docDevedor, String numeroTitulo, double valorTitulo,
			double valorProtesto, long protocolo, LocalDate dataProtocolo, String especie,
			String statusPedido, String irregularidade, String ocorrenciaTitulo, LocalDate dataOcorrencia) {
		
		
		super(pedido,nomeDevedor, docDevedor, dataProtocolo, null, valorTitulo, dataOcorrencia, especie);
		
		
		this.comarcaCartorio = comarcaCartorio;
		this.dataSolicitacao = dataSolicitacao;
		this.comarcaDevedor = comarcaDevedor;
		this.numeroTitulo = numeroTitulo;
		this.valorProtesto = valorProtesto;
		this.protocolo = protocolo;
		this.statusPedido = statusPedido;
		this.irregularidade = irregularidade;
		this.ocorrenciaTitulo = ocorrenciaTitulo;
	}
	public long getPedido() {
		return super.getCodigo();
	}
	public String getComarcaCartorio() {
		return comarcaCartorio;
	}
	public LocalDate getDataSolicitacao() {
		return dataSolicitacao;
	}
	public String getComarcaDevedor() {
		return comarcaDevedor;
	}
	public String getDevedor() {
		return super.getNome();
	}
	public String getDocDevedor() {
		return super.getCpf();
	}
	public String getNumeroTitulo() {
		return numeroTitulo;
	}
	public double getValorTitulo() {
		return super.getValor();
	}
	public double getValorProtesto() {
		return valorProtesto;
	}
	public long getProtocolo() {
		return protocolo;
	}
	public LocalDate getDataProtocolo() {
		return super.getDataInclusao();
	}	
	
	public String getEspecie() {
		return super.getTipo();
	}
	public String getStatusPedido() {
		return statusPedido;
	}
	
	public String getIrregularidade() {
		return irregularidade;
	}
	public String getOcorrenciaTitulo() {
		return ocorrenciaTitulo;
	}
	public LocalDate getDataOcorrencia() {
		return super.getDataVencimento();
	}
	
	@Override
	public String toString() {
		
		return String.format("%d %s %s %s %s %s %s %.2f %.2f %d %s %s %s %s %s %s",
				getPedido(), getComarcaCartorio(), getDataSolicitacao(), getComarcaDevedor(),
				getDevedor(), getDocDevedor(), getNumeroTitulo(), getValorTitulo(), getValorProtesto(),
				getProtocolo(), getDataProtocolo(), getEspecie(), getStatusPedido(), getIrregularidade(),
				getOcorrenciaTitulo(), getDataOcorrencia());
	}

}
