package br.com.adamis.orm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@DatabaseTable(tableName = "enderecos")
@Data
public class Enderecos {
	    
		@DatabaseField(generatedId = true)
	    private Long id;
	
	    @DatabaseField
	    private String name;
	    
	    @DatabaseField
	    private String cepOrigem;
	    
	    @DatabaseField
	    private String cep;
	    
	    @DatabaseField
	    private String logradouro;
	    
	    @DatabaseField
	    private String numero;
	    
	    @DatabaseField
	    private String complemento;
	    
	    @DatabaseField
	    private String bairro;
	    
	    @DatabaseField
	    private String cidade;
	    
	    @DatabaseField
	    private String estado;
	    
	    @DatabaseField
	    private String dataEnvio;
	    
	    @DatabaseField
	    private String dataRecebimento;
	    
	    @DatabaseField
	    private String valorEnvio;
	    
}
