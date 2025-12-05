package spc_serasa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Cliente {
	
	private final long codigo;
	private final String nome;
	private final String cpf;
	private final LocalDate dataInclusao;
	private final  LocalTime hora;
	private final double valor;
	private final LocalDate dataVencimento;
	private final String tipo;
	
	public Cliente(long codigo, String nome, String cpf, LocalDate dataInclusao,LocalTime hora, double valor,
			LocalDate dataVencimento, String tipo) {
		
		this.codigo = codigo;
		this.nome = Objects.requireNonNull(nome, "Nome não pode ser null");
		this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser null");
		this.dataInclusao = dataInclusao;
		this.hora =  hora != null ? hora : LocalTime.MIDNIGHT;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.tipo = tipo;
	}

	public long getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	public String getCpf() {
		return cpf;
	}

	public LocalDate getDataInclusao() {
		return dataInclusao;
	}

	public LocalTime getHora() {
		return hora;
	}

	public double getValor() {
		return valor;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public String getTipo() {
		return tipo;
	}
	
	@Override
	public String toString() {
		return String.format("%d %s %s %.2f %s %s %s",
				codigo, nome, cpf, valor, dataInclusao, hora, dataVencimento);
	}
	
	

}
