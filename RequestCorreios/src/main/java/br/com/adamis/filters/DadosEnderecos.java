package br.com.adamis.filters;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.adamis.enuns.TypeEnvio;

public class DadosEnderecos {

	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	private String nCdEmpresa;
	private String sDsSenha;
	private TypeEnvio nCdServico;
	private Integer sCepOrigem;
	private Integer sCepDestino;
	private Integer nVlPeso;
	private Integer nCdFormato;
	private Integer nVlComprimento;
	private Integer nVlAltura;
	private Integer nVlLargura;
	private Integer nVlDiametro;
	private String sCdMaoPropria = "n";
	private Integer nVlValorDeclarado;
	private Date sDtCalculo;
	private String sCdAvisoRecebimento = "n";
	private String strVerificaRestricao = "s";
	
	/**
	 * @return the nCdEmpresa
	 */
	public String getnCdEmpresa() {
		return nCdEmpresa;
	}
	/**
	 * @param nCdEmpresa the nCdEmpresa to set
	 */
	public void setnCdEmpresa(String nCdEmpresa) {
		this.nCdEmpresa = nCdEmpresa;
	}
	/**
	 * @return the sDsSenha
	 */
	public String getsDsSenha() {
		return sDsSenha;
	}
	/**
	 * @param sDsSenha the sDsSenha to set
	 */
	public void setsDsSenha(String sDsSenha) {
		this.sDsSenha = sDsSenha;
	}
	/**
	 * @return the nCdServico
	 */
	public Integer getnCdServico() {
		return nCdServico.getCodigo();
	}
	/**
	 * @param nCdServico the nCdServico to set
	 */
	public void setnCdServico(TypeEnvio nCdServico) {
		this.nCdServico = nCdServico;
	}
	/**
	 * @return the sCepOrigem
	 */
	public Integer getsCepOrigem() {
		return sCepOrigem;
	}
	/**
	 * @param sCepOrigem the sCepOrigem to set
	 */
	public void setsCepOrigem(Integer sCepOrigem) {
		this.sCepOrigem = sCepOrigem;
	}
	/**
	 * @return the sCepDestino
	 */
	public Integer getsCepDestino() {
		return sCepDestino;
	}
	/**
	 * @param sCepDestino the sCepDestino to set
	 */
	public void setsCepDestino(Integer sCepDestino) {
		this.sCepDestino = sCepDestino;
	}
	/**
	 * @return the nVlPeso
	 */
	public Integer getnVlPeso() {
		return nVlPeso;
	}
	/**
	 * @param nVlPeso the nVlPeso to set
	 */
	public void setnVlPeso(Integer nVlPeso) {
		this.nVlPeso = nVlPeso;
	}
	/**
	 * @return the nCdFormato
	 */
	public Integer getnCdFormato() {
		return nCdFormato;
	}
	/**
	 * @param nCdFormato the nCdFormato to set
	 */
	public void setnCdFormato(Integer nCdFormato) {
		this.nCdFormato = nCdFormato;
	}
	/**
	 * @return the nVlComprimento
	 */
	public Integer getnVlComprimento() {
		return nVlComprimento;
	}
	/**
	 * @param nVlComprimento the nVlComprimento to set
	 */
	public void setnVlComprimento(Integer nVlComprimento) {
		this.nVlComprimento = nVlComprimento;
	}
	/**
	 * @return the nVlAltura
	 */
	public Integer getnVlAltura() {
		return nVlAltura;
	}
	/**
	 * @param nVlAltura the nVlAltura to set
	 */
	public void setnVlAltura(Integer nVlAltura) {
		this.nVlAltura = nVlAltura;
	}
	/**
	 * @return the nVlLargura
	 */
	public Integer getnVlLargura() {
		return nVlLargura;
	}
	/**
	 * @param nVlLargura the nVlLargura to set
	 */
	public void setnVlLargura(Integer nVlLargura) {
		this.nVlLargura = nVlLargura;
	}
	/**
	 * @return the nVlDiametro
	 */
	public Integer getnVlDiametro() {
		return nVlDiametro;
	}
	/**
	 * @param nVlDiametro the nVlDiametro to set
	 */
	public void setnVlDiametro(Integer nVlDiametro) {
		this.nVlDiametro = nVlDiametro;
	}
	/**
	 * @return the sCdMaoPropria
	 */
	public String getsCdMaoPropria() {
		return sCdMaoPropria;
	}
	/**
	 * @param sCdMaoPropria the sCdMaoPropria to set
	 */
	public void setsCdMaoPropria(String sCdMaoPropria) {
		this.sCdMaoPropria = sCdMaoPropria;
	}
	/**
	 * @return the nVlValorDeclarado
	 */
	public Integer getnVlValorDeclarado() {
		return nVlValorDeclarado;
	}
	/**
	 * @param nVlValorDeclarado the nVlValorDeclarado to set
	 */
	public void setnVlValorDeclarado(Integer nVlValorDeclarado) {
		this.nVlValorDeclarado = nVlValorDeclarado;
	}
	/**
	 * @return the sDtCalculo
	 */
	public String getsDtCalculo() {
		return format.format(sDtCalculo);
	}
	/**
	 * @param sDtCalculo the sDtCalculo to set
	 */
	public void setsDtCalculo(Date sDtCalculo) {
		this.sDtCalculo = sDtCalculo;
	}
	/**
	 * @return the sCdAvisoRecebimento
	 */
	public String getsCdAvisoRecebimento() {
		return sCdAvisoRecebimento;
	}
	/**
	 * @param sCdAvisoRecebimento the sCdAvisoRecebimento to set
	 */
	public void setsCdAvisoRecebimento(String sCdAvisoRecebimento) {
		this.sCdAvisoRecebimento = sCdAvisoRecebimento;
	}
	public String getStrVerificaRestricao() {
		return strVerificaRestricao;
	}
	public void setStrVerificaRestricao(String strVerificaRestricao) {
		this.strVerificaRestricao = strVerificaRestricao;
	}
    
	
}
