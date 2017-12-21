package br.com.rponte.hibernateefetivo.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaUtils {
	
	private static final EntityManagerFactory factory = new EntityManagerFactoryProvider()
																	.newEntityManagerFactory();

	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
}
