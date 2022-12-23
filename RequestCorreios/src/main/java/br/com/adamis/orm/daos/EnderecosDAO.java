package br.com.adamis.orm.daos;

import java.sql.SQLException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import br.com.adamis.orm.entity.Enderecos;

public class EnderecosDAO extends BaseDaoImpl<Enderecos, String> {

	public EnderecosDAO(ConnectionSource connectionSource)  throws SQLException {
		        super(connectionSource, Enderecos.class);
    }
	
}
