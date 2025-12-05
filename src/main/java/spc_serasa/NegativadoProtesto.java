package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NegativadoProtesto extends Negativado {

	ArrayList<ClienteProtesto> listaCliente = new ArrayList<>();

	@Override
	public List adiciona(List<String[]> linhas) {
		
		for (int linha = 1; linha < linhas.size(); linha++) {

			String[] cliente = dadosCliente(linhas.get(linha));
			
			long pedido = getLong(cliente, 0);
			String comarcaCartorio = getCampo(cliente, 1);
			LocalDate dataSolicitacao = paraData(getCampo(cliente, 2));
			String comarcaDevedor = getCampo(cliente,3);
			String devedor = getCampo(cliente,4);
			String docDevedor = getCampo(cliente,5);
			String numeroTitulo = getCampo(cliente,6);
			double valorTitulo = paraValor(getCampo(cliente,7));
			double valorProtesto = paraValor(getCampo(cliente,8));
			long protocolo = getLong(cliente,9);
			LocalDate dataProtocolo = paraData(getCampo(cliente,10));
			String especie = getCampo(cliente,11);
			String statusPedido = getCampo(cliente,12)
					;
			String irregularidade = getCampo(cliente, 13); 
			String ocorrenciaTitulo = getCampo(cliente, 14); 
			LocalDate dataOcorrencia = paraData(getCampo(cliente, 15));		
			
		
			ClienteProtesto clienteProtesto = new ClienteProtesto(pedido, comarcaCartorio, dataSolicitacao, comarcaDevedor,
					devedor, docDevedor, numeroTitulo, valorTitulo, valorProtesto, protocolo, dataProtocolo,
					especie, statusPedido, irregularidade, ocorrenciaTitulo, dataOcorrencia);
			
			listaCliente.add(clienteProtesto);

		}

		return listaCliente;
	}
	
	

}
