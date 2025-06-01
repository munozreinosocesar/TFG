package com.dss.carrito.servicios;

import com.dss.carrito.entidades.Producto;
import com.dss.carrito.repositorios.ProductRepoCustomImpl;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    
    @Autowired
    private ProductRepoCustomImpl productRepoCustom;

    public List<Producto> getAllProductPublic(){
        return productRepoCustom.findAllByTenant(0);
    }
    public List<Producto> getAllProduct(int tenant){

        return productRepoCustom.findAllByTenant(tenant);
    }

    public Optional<Producto> getProductById(int tenant, Long id){
        List<Producto> productos = productRepoCustom.findAllByTenant(tenant);
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }
    
    public void saveProduct(int tenant, Producto product){
        productRepoCustom.setSchema(tenant);
        productRepoCustom.save(tenant, product);
    }

    public void deleteProduct(int tenant, Long id){
        productRepoCustom.setSchema(tenant);
        productRepoCustom.deleteById(tenant, id);
    }

    public void updateProduct(int tenant, Long id, Producto product){
        productRepoCustom.setSchema(tenant);
        productRepoCustom.save(tenant, product);
    }

    public void updateProduct(int tenant, Producto product){
        productRepoCustom.setSchema(tenant);
        productRepoCustom.save(tenant, product);
    }
}
