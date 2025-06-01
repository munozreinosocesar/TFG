package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Carrito;
import com.dss.carrito.entidades.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarritoRepoCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void setSchema(int tenant) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }

    @Transactional
    public void addProductToCart(int tenant, Long usuarioId, Producto product) {
        setSchema(tenant);

        Carrito carrito = entityManager.createQuery(
            "SELECT c FROM Carrito c WHERE c.usuarioId = :usuarioId", Carrito.class)
            .setParameter("usuarioId", usuarioId)
            .getResultStream()
            .findFirst()
            .orElse(null);


        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuarioId(usuarioId);
            entityManager.persist(carrito);
        }

        Producto existingProduct = entityManager.find(Producto.class, product.getId());
        if (existingProduct == null) {
            entityManager.persist(product);
            existingProduct = product;
        }

        carrito.getProductos().add(existingProduct);
        entityManager.merge(carrito);
    }

    @Transactional
    public void addProductToCart(int tenant, Producto product) {
        setSchema(tenant);

        Carrito carrito = entityManager.find(Carrito.class, 1L);
        if (carrito == null) {
            carrito = new Carrito();
            entityManager.persist(carrito);
        }

        Producto existingProduct = entityManager.find(Producto.class, product.getId());
        if (existingProduct == null) {
            entityManager.persist(product);
            existingProduct = product;
        }

        carrito.getProductos().add(existingProduct);
        entityManager.merge(carrito);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Producto> findAllByUser(int tenant, Long usuarioId) {
        setSchema(tenant);
        return entityManager.createNativeQuery(
            "SELECT p.* FROM carrito_productos cp " +
            "JOIN producto p ON cp.producto_id = p.id " +
            "JOIN carrito c ON cp.carrito_id = c.id " +
            "WHERE c.usuario_id = :usuarioId", Producto.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Producto> findAllByTenant(int tenant) {
        setSchema(tenant);
        return entityManager.createNativeQuery(
            "SELECT p.* FROM carrito_productos cp " +
            "JOIN producto p ON cp.producto_id = p.id " +
            "JOIN carrito c ON cp.carrito_id = c.id", Producto.class)
            .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Producto> findByNombreAndTenant(int tenant, String nombre) {
        setSchema(tenant);
        return entityManager.createNativeQuery(
            "SELECT p.* FROM carrito_productos cp " +
            "JOIN producto p ON cp.producto_id = p.id " +
            "JOIN carrito c ON cp.carrito_id = c.id " +
            "WHERE p.name LIKE :nombre", Producto.class)
            .setParameter("nombre", "%" + nombre + "%")
            .getResultList();
    }

    @Transactional
    public void removeProductFromCart(int tenant, Long usuarioId, Long productId) {
        setSchema(tenant);

        Carrito carrito = entityManager.createQuery(
            "SELECT c FROM Carrito c WHERE c.usuarioId = :usuarioId", Carrito.class)
            .setParameter("usuarioId", usuarioId)
            .getResultStream()
            .findFirst()
            .orElse(null);

        if (carrito != null) {
            carrito.getProductos().removeIf(p -> p.getId().equals(productId));
            entityManager.merge(carrito);
        }
    }

    @Transactional
    public void clear(int tenant, Long usuarioId) {
        setSchema(tenant);

        Carrito carrito = entityManager.createQuery(
            "SELECT c FROM Carrito c WHERE c.usuarioId = :usuarioId", Carrito.class)
            .setParameter("usuarioId", usuarioId)
            .getResultStream()
            .findFirst()
            .orElse(null);

        if (carrito != null) {
            carrito.getProductos().clear();
            entityManager.merge(carrito);
        }
    }
}
