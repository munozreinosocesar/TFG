package com.dss.carrito.servicios;


import com.dss.carrito.entidades.Producto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseExportService {
    ProductService productService;

    public byte[] export() {
        List<Producto> productos = productService.getAllProductPublic();
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE productos (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), price DOUBLE);\n");

        for (Producto producto : productos) {
            sb.append("INSERT INTO productos (name, price) VALUES ('")
                    .append(producto.getName())
                    .append("', ")
                    .append(producto.getPrice())
                    .append(");\n");
        }

        return sb.toString().getBytes();
    }
}

