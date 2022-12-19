package br.com.adamis.responses;
import lombok.Data;

@Data
public class PrazoMaximo {

	private int codigo;
	private int prazoEntrega;
	private String entregaDomiciliar;
	private String entregaSabado;
	private Object erro;
	private Object msgErro;
	private Object obsFim;
	private String dataMaxEntrega;
	
}
