package br.com.adamis.enuns;

public enum TypeEnvio {

	SEDEX_Varejo(40010),
	SEDEX_a_Cobrar_Varejo(40045),
	SEDEX_10_Varejo(40215),
	SEDEX_Hoje_Varejo(40290),
	PAC_Varejo(41106)
	;

	private int codigo;
	
	TypeEnvio(int valor) {
		this.codigo = valor;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

}