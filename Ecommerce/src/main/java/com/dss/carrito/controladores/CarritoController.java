package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Producto;
import com.dss.carrito.servicios.CarritoService;
import com.dss.carrito.servicios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/cart/{tenant}")
public class CarritoController {
    private final CarritoService carritoService;
    private final UsuarioService usuarioService;


    @GetMapping("/{usuario}")
    public List<Producto> getAllProducts(@PathVariable int tenant, @PathVariable Long usuario) {
        return carritoService.getAllProducts(tenant, usuario);
    }

    @RequestMapping
    public List<Producto> getAllProducts(@PathVariable int tenant, Authentication auth) {
        Optional<Long> usuarioId = usuarioService.obtenerUsuarioIdDesdeEmail(auth.getName(), tenant);
        if (usuarioId.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado en el esquema " + tenant);
        }
        return carritoService.getAllProducts(tenant, usuarioId.get());
    }

    @PostMapping("/{usuario}")
    public String saveProduct(@PathVariable int tenant, @PathVariable Long usuario, @ModelAttribute Producto producto) {
        carritoService.addProduct(tenant, usuario, producto);
        return "redirect:/" + tenant + "/" + usuario + "/cart";
    }
    @PostMapping("/{usuario}/delete/{id}")
    public String deleteProduct(@PathVariable int tenant, @PathVariable Long usuario, @PathVariable Long id) {
        carritoService.deleteProduct(tenant, usuario, id);
        return "redirect:/" + tenant + "/" + usuario + "/cart";
    }
}