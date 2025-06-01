package com.dss.carrito.repositorios;

import com.dss.carrito.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepo extends JpaRepository<Producto, Long> {

}
