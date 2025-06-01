package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Pedido;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public class PedidoRepoCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public  void setSchema(int tenant) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }
    @Transactional
    public void save(int tenant, Pedido pedido) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
        entityManager.persist(pedido);
    }


    @Transactional
    public  List<Pedido> findAllByTenant(int tenant) {
        setSchema(tenant);
        return entityManager.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
    }
}
