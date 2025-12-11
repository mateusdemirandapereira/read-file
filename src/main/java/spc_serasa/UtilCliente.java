package spc_serasa;

public abstract class UtilCliente {

	public static long separaContrato(String contrato) throws NumberFormatException {

		if (contrato == null) {
			throw new NumberFormatException("contrato está nulo.");
		}
		System.out.println(">> valor recebido no separaContrato: [ " + contrato + " ]");
		
		contrato = contrato.replace("/", "-").replaceAll("[\\s\\u00A0]+", "").trim().replaceAll("[^0-9\\-]", "");

		if (contrato.isEmpty() || contrato.equals("-")) {
			throw new NumberFormatException(
					"Falha ao converter contrato para numero. valor recebido: '" + contrato + "'");
		}

		try {

			if (contrato.contains("-")) {
				String[] partes = contrato.split("-", 2);

				if (partes[0].isEmpty()) {
					throw new NumberFormatException(
							"Falha ao converter contrato para número. valor recebido: '" + contrato + "'");
				}

				return Long.parseLong(partes[0]);

			}

			return Long.parseLong(contrato);

		} catch (NumberFormatException e) {
			throw new NumberFormatException(
					"Falha ao converter contrato para número. valor recebido: '" + contrato + "'");
		}

	}

	public static int separaParcela(String contrato) {
		int parcela = 0;
		if (contrato == null) {
			throw new NumberFormatException("contrato está nulo.");
		}

		try {
			contrato = contrato.replace("/", "-").replaceAll("[\\s\\u00A0]+", "").trim().replaceAll("[^0-9\\-]", "");

			if (contrato.contains("-")) {
				String[] partes = contrato.split("-");

				if (partes.length > 1 && !partes[1].isEmpty()) {
					parcela = Integer.parseInt(partes[1].trim());
				}

			}
			return parcela;

		} catch (NumberFormatException ex) {
			throw new NumberFormatException(
					"Falha ao converter parcela para numero. valor recebido: '" + contrato + "'");
		}

	}

}
