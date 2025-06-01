package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Producto;
import com.dss.carrito.servicios.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProductRestController {
    private ProductService productService;

    @GetMapping("/products")
    @ResponseBody
    public List<Producto> getAllProduct(){
        List<Producto> productos = productService.getAllProduct(1);
        System.out.println("getAll");

        return productos;
    }

    @GetMapping("/products-by-id")
    @ResponseBody
    public List<Producto> getProductsById(@RequestParam String ids){
        String[] idsArray = ids.split("_");
        List<Producto> productos = new ArrayList<>();

        for (String id : idsArray) {
            productos.add(productService.getProductById(1,Long.parseLong(id)).get());
        }

        System.out.println("getProductsById");

        return productos;
    }

    @GetMapping("/products/add")
    @ResponseBody
    public Integer Product(@RequestParam String name, @RequestParam String price) {
        System.out.println("productAdd");

        Producto product = new Producto();
        product.setName(name);
        product.setPrice(Double.parseDouble(price));
        productService.saveProduct(1,product);
        System.out.println("Peticion add");

        return 0;
    }

    @GetMapping("/products/edit")
    @ResponseBody
    public Integer editProduct(@RequestParam Long id, @RequestParam String name, @RequestParam double price) {
        System.out.println("editProduct");
        Producto product = productService.getProductById(1,id).get();
        System.out.println(name);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            productService.saveProduct(1,product);
            return 0;
        }
        return 1;
    }


    @GetMapping("/products/delete")
    @ResponseBody
    public Integer deleteProduct(@RequestParam Long id, @RequestParam String token) {
        System.out.println("deleteproduct");
        Producto product = productService.getProductById(1,id).get();
        if (product != null) {
            productService.deleteProduct(1,id);
            return 0;
        }
        return 1;
    }

}
