package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepoCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public  void setSchema(int tenant) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }

    @Transactional
    public  List<Producto> findAllByTenant(int tenant) {
        setSchema(tenant);
        return entityManager.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
    }

    @Transactional
    public  List<Producto> findByNombreAndTenant(int tenant, String nombre) {
        setSchema(tenant);
        return entityManager.createQuery("SELECT p FROM Producto p WHERE p.nombre LIKE :nombre", Producto.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }

    @Transactional
    public void save(int tenant, Producto product) {
        setSchema(tenant);
        entityManager.persist(product);
    }

    @Transactional
    public void deleteById(int tenant, Long id) {
        setSchema(tenant);
        Producto product = entityManager.find(Producto.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }
}
