package br.com.rponte.hibernateefetivo.batch;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.lang3.time.DurationFormatUtils;

import br.com.rponte.hibernateefetivo.model.Produto;
import br.com.rponte.hibernateefetivo.util.JpaUtils;

public class ImportadorDeProdutos {

	public static void main(String[] args) {

		int entityCount = 10;
		int batchSize = 100;
		
		long inicio = System.currentTimeMillis();

		EntityManager manager = JpaUtils.getEntityManager();
		
		EntityTransaction tx = manager.getTransaction();
		tx.begin();

		for (int i = 0; i < entityCount; i++) {
			
//			if (i > 0 && i % batchSize == 0) {
//				manager.flush();
//				manager.clear();
//			}
			
			Produto produto = new Produto("Produto #" + i);
			manager.persist(produto);
			
			System.out.println("Produto #" + i);
		}

		tx.commit();
		manager.close();

		long fim = System.currentTimeMillis();
		long duracao = (fim - inicio);

		System.out.println("[com Session] duração: " + DurationFormatUtils.formatDuration(duracao, "mm'm'ss's'"));

	}
}
