package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Pagos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public class PagosRepoCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void setSchema(int tenant) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }

    @Transactional
    public void save(int tenant, Pagos pago) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
        entityManager.persist(pago);
    }

    @Transactional
    public  List<Pagos> findAllByTenant(int tenant) {
        setSchema(tenant);
        return entityManager.createQuery("SELECT p FROM Pagos p", Pagos.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Pagos> findByUsuarioId(int tenant, Long usuarioId) {
        setSchema(tenant);
        return entityManager.createNativeQuery(
            "SELECT p.* FROM pagos p " +
            "JOIN pedido pd ON p.pedido_id = pd.id " +
            "JOIN usuario u ON pd.usuario_id = u.id " +
            "WHERE u.id = :usuarioId", Pagos.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }
}
