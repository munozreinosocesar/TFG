package com.dss.carrito.servicios;

import com.dss.carrito.entidades.Producto;
import com.dss.carrito.repositorios.CarritoRepoCustomImpl;

import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@NoArgsConstructor
public class CarritoService {

    @Autowired
    private CarritoRepoCustomImpl carritoRepoCustom;

    public void addProduct(int tenant, Long usuarioId, Producto product) {
        carritoRepoCustom.setSchema(tenant);
        carritoRepoCustom.addProductToCart(tenant, usuarioId, product);
    }

    public List<Producto> getAllProducts(int tenant){
        return carritoRepoCustom.findAllByTenant(tenant);

    }

    public void deleteProduct(int tenant, Long usuarioId, Long id){
        carritoRepoCustom.removeProductFromCart(tenant, usuarioId, id);
    }

    public void vaciarCarrito(int tenant, Long usuarioId){
        carritoRepoCustom.clear(tenant,usuarioId);
    }
    
    public List<Producto> getAllProducts(int tenant, Long usuarioId) {
        carritoRepoCustom.setSchema(tenant);
        return carritoRepoCustom.findAllByUser(tenant, usuarioId);
    }
}