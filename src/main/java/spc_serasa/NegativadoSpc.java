package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NegativadoSpc extends Negativado {
	ArrayList<ClienteSpc> listCliente = new ArrayList<>();

	public List adiciona(List<String[]> linhas) {
		
		for (int linha = 1; linha < linhas.size(); linha++) {
			
			String[] cliente =  dadosCliente(linhas.get(linha));							
			
			long codigoSpc = getLong(cliente,0);
			String cpfCnpj = getCampo(cliente,1);
			String consumidor = getCampo(cliente,2);
			String contrato = getCampo(cliente,3);
			LocalDate dataVencimento = paraData(getCampo(cliente,4));
			double valorDebito = paraValor(getCampo(cliente,5));
			LocalDate dataInclusao = paraData(getCampo(cliente,6));
			LocalTime horaInclusao = paraHora(getCampo(cliente,7));
			LocalDate dataExclusao = paraData(getCampo(cliente,8));
			String tipoNotificacao = getCampo(cliente,9);
			long codigoNotificacao = getLong(cliente,10);
			int codigoAssociado = getInt(cliente,11);

			listCliente.add(new ClienteSpc(codigoSpc, cpfCnpj, consumidor, contrato, dataVencimento, valorDebito, dataInclusao,
					horaInclusao, dataExclusao, tipoNotificacao, codigoNotificacao, codigoAssociado));

		}
		return listCliente;
	}
	
	private int getInt(String[] dados, int index) {
		String texto = getCampo(dados, index);
		
		if (texto.isEmpty()) return 0;
		
		try {
			return Integer.parseInt(texto);
		} catch(Exception e) {
			return 0;
		}
	}
	
	
}
