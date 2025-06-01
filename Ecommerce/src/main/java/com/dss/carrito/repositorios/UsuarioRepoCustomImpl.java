package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepoCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void setSchema(int tenant) {
        String schema = "schema" + tenant;
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }

    @Transactional
    public List<Usuario> findAllByTenant(int tenant) {
        setSchema(tenant);
        return entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Transactional
    public Optional<Usuario> findByEmailAndTenant(int tenant, String email) {
        setSchema(tenant);
        return entityManager.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email",
                Usuario.class
        ).setParameter("email", email)
         .setMaxResults(1)
         .getResultStream()
         .findFirst();
    }
    
    

    @Transactional
    public void save(int tenant, Usuario usuario) {
        setSchema(tenant);
        entityManager.persist(usuario);
    }

    @Transactional
    public void deleteById(int tenant, Long id) {
        setSchema(tenant);
        Usuario usuario = entityManager.find(Usuario.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
    }
}
