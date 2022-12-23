package br.com.adamis.orm.datasources;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import br.com.adamis.orm.entity.Enderecos;

public class MySQLBuilder{
    
    private ConnectionSource connectionSource;
    private String url = "jdbc:mysql://192.168.1.13:3306/enderecos?createDatabaseIfNotExist=true&serverTimezone=GMT-3";
    private String username = "root";
    private String password = "q1w2e3r4";
    

	public ConnectionSource getConnectionSource() throws SQLException {
		
		if(connectionSource == null) {
			connectionSource = new JdbcConnectionSource(url,username,password);
			createTables();
		}	
		
		return connectionSource;
    }
	    
	
	private void createTables() throws SQLException {
		TableUtils.createTableIfNotExists(connectionSource, Enderecos.class);
	}
}
