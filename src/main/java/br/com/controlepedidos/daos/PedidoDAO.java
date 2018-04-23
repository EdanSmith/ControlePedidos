package br.com.controlepedidos.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.controlepedidos.models.Pedido;

public class PedidoDAO { // Criar e utilizar o JPAUtil

	public Pedido getPedido(int numeroControle) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControlePedidos");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		String jsql = "select p from Pedido where Id = :pId";

		Query query = em.createQuery(jsql);
		query.setParameter("pId", numeroControle);

		Pedido pedido = (Pedido) query.getSingleResult();

		em.close();
		
		return pedido;
	}

	public void gravar(Pedido pedido) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControlePedidos");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		em.persist(pedido);
		em.getTransaction().commit();
		em.close();
	}
}
