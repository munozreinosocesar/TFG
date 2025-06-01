package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Producto;
import com.dss.carrito.servicios.ProductService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/productos/{tenant}")
public class ProductController {
    private ProductService productService;

    @GetMapping
    public List<Producto> getAllProduct(@PathVariable int tenant){
        return productService.getAllProduct(tenant);
    }

    @GetMapping("/{id}")
    public Producto getProductById(@PathVariable int tenant, @PathVariable Long id){
        Optional<Producto> product = productService.getProductById(tenant,id);
        return product.orElse(null);
    }

    @PostMapping
    public String saveProduct(@PathVariable int tenant, @ModelAttribute Producto producto){
        productService.saveProduct(tenant, producto);
        return "redirect:/" + tenant + "/1/productos";
    }

    @PostMapping("/{usuario}/delete/{id}")
    public String deleteProduct(@PathVariable int tenant, @PathVariable Long id){
        productService.deleteProduct(tenant, id);
        return "redirect:/" + tenant + "/1/productos";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable int tenant, @PathVariable Long id, @ModelAttribute Producto updatedProduct){
        Optional<Producto> optionalProduct = productService.getProductById(tenant, id);

        if (optionalProduct.isPresent()) {
            Producto existingProduct = optionalProduct.get();
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());

            productService.saveProduct(tenant, existingProduct);
        }
        return "redirect:/" + tenant + "/1/productos";
    }
}