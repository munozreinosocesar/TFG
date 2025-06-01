package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagosRepo extends JpaRepository<Pagos, Long> {
}
