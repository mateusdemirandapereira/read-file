package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public abstract class Negativado {
	private static final DateTimeFormatter[] FORMATOS_DATA = { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
			DateTimeFormatter.ofPattern("d/M/yyyy"), DateTimeFormatter.ofPattern("dd/MM/yy"),
			DateTimeFormatter.ofPattern("d/M/yy"), DateTimeFormatter.ofPattern("yyyy-MM-dd"),
			DateTimeFormatter.ofPattern("yyyyMMdd") };

	public abstract List adiciona(List<String[]> linhas);

	public LocalDate paraData(String texto) {

		if (texto == null || texto.isBlank())
			return null;

		for (DateTimeFormatter f : FORMATOS_DATA) {
			try {
				return LocalDate.parse(texto.trim(), f);
			} catch (Exception ignored) {

			}
		}

		throw new IllegalArgumentException("Formato de data inválido:" + texto);

	}

	public LocalTime paraHora(String texto) {

		if (texto == null || texto.isBlank()) {
			return null;
		}
		
		texto = texto.trim();
		DateTimeFormatter formatter;
		
		switch(texto.length()) {
			case 8:
			case 7:
				formatter = DateTimeFormatter.ofPattern("H:mm:ss");
				break;
			case 5:
			case 4:
				formatter = DateTimeFormatter.ofPattern("H:mm");
				break;
			default:
				throw new IllegalArgumentException("Formato de hora desconhecido: " + texto);
			
		}
		
		return LocalTime.parse(texto, formatter);		

	}

	public double paraValor(String texto) {
		if (texto == null || texto.isBlank()) return 0;
		
		texto = texto.replace("R$", "")
				.replace(".", "")
				.replace(",", ".")
				.replace(" ", "");
		
		return Double.parseDouble(texto);
	}

	public String[] dadosCliente(String[] dados) {
		String novosDados = Arrays.toString(dados);
		novosDados = novosDados.substring(1, novosDados.length() - 1);

		String[] cliente = novosDados.split(";");
		return cliente;
	}
	
	public String getCampo(String[] dados, int index) {
		return index < dados.length ? dados[index].trim() : "";
	}
	
	public long getLong(String[] dados, int index) {
		String valor = getCampo(dados, index);
		
		if (valor.isEmpty()) return 0;
		
		try {
			return Long.parseLong(valor);
		} catch(Exception e) {
			return 0;
		}
	}
	

}
