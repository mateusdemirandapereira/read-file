package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NegativadoSerasa extends Negativado {
	ArrayList<ClienteSerasa> listaCliente = new ArrayList<>();
	@Override
	public List adiciona(List<String[]> linhas) {		
		
		for (int linha = 1; linha < linhas.size(); linha++) {
			
			String[] cliente = dadosCliente(linhas.get(linha));
			
			String status = getCampo(cliente,0);
			long id = getLong(cliente,1);
			String nomeDevedor = getCampo(cliente,2);
			String tipoPessoa = getCampo(cliente,3);
			String documento = getCampo(cliente,4);
			String natureza = getCampo(cliente,5);
			double valor = paraValor(getCampo(cliente,6));
			
			
			String dataHora = getCampo(cliente,7);
			LocalDate dataCadastro = null;
			LocalTime horaInclusao = null;
			
			if (!dataHora.isEmpty()) {
				String[] partes = dataHora.split(" ");
				
				dataCadastro = paraData(partes[0]);
				
				if (partes.length > 1) {
					horaInclusao = paraHora(partes[1]);
				}
			}
			
			
			LocalDate dataVencimento = paraData(getCampo(cliente,8));
			String operacao = getCampo(cliente,9);
			
			ClienteSerasa clienteSerasa = new ClienteSerasa(status, id, nomeDevedor, tipoPessoa, documento, natureza, valor, dataCadastro,
					horaInclusao, dataVencimento, operacao);
			
			listaCliente.add(clienteSerasa);			
			
		} 
		
		
		
		return listaCliente;
		
	}
	


}
