package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DireccionRepo extends JpaRepository<Direccion, Long> {
    List<Direccion> findByUsuarioId(Long usuarioId);
}
