package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
