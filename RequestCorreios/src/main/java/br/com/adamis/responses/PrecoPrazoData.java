package br.com.adamis.responses;

import lombok.Data;

@Data
public class PrecoPrazoData {

	private int codigo;
	private Double valor;
	private int prazoEntrega;
	private double valorMaoPropria;
	private double valorAvisoRecebimento;
	private double valorValorDeclarado;
	private String entregaDomiciliar;
	private String entregaSabado;
	private int erro;
	private Object msgErro;
	private double valorSemAdicionais;
	private Object obsFim;
	
}
