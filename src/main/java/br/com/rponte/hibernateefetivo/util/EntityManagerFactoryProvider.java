package br.com.rponte.hibernateefetivo.util;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

import br.com.rponte.hibernateefetivo.model.Produto;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

public class EntityManagerFactoryProvider {

	protected EntityManagerFactory newEntityManagerFactory() {
		PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo(getClass().getSimpleName());
		Map<String, Object> configuration = new HashMap<>();

		return new EntityManagerFactoryBuilderImpl(
				new PersistenceUnitInfoDescriptor(persistenceUnitInfo),
				configuration).build();
	}

	protected PersistenceUnitInfoImpl persistenceUnitInfo(String name) {
		return new PersistenceUnitInfoImpl(name, entityClassNames(), properties());
	}

	private Properties properties() {
		
		Properties properties = new Properties();
		// Configuracoes do DataSource
		properties.put("hibernate.connection.datasource", newDataSource());
		// Configuracoes especificas do Hibernate
	    properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
	    properties.put("hibernate.hbm2ddl.auto", "create-drop");
	    properties.put("hibernate.show_sql", "false");
	    properties.put("hibernate.format_sql", "false");
	    properties.put("hibernate.hbm2ddl.auto", "create-drop");
	    // Configuracoes de Processamento em Lote
	    properties.put("hibernate.jdbc.batch_size", "5");
	    properties.put("hibernate.order_inserts", "true");
	    properties.put("hibernate.order_updates", "true");
	    return properties;
	}

	private DataSource newDataSource() {
		DataSource dataSource = 
			    ProxyDataSourceBuilder
			        .create(actualDataSource())
			        .name("DATASOURCE_PROXY")
			        .logQueryBySlf4j(SLF4JLogLevel.INFO)
			        .build();
		return dataSource;
	}
	
	private DataSource actualDataSource() {
		try {
			oracle.jdbc.pool.OracleDataSource dataSource = new oracle.jdbc.pool.OracleDataSource();
			dataSource.setURL("jdbc:oracle:thin:@localhost:1521:XE");
			dataSource.setDatabaseName("hibernate-efetivo");
			dataSource.setUser("system");
			dataSource.setPassword("manager");
			return dataSource;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	protected List<String> entityClassNames() {
	    return Arrays.asList(Produto.class)
	        .stream()
	        .map(Class::getName)
	        .collect(Collectors.toList());
	}
}
