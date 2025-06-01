package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Direccion;
import com.dss.carrito.servicios.DireccionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
@AllArgsConstructor
public class DireccionController {
    private final DireccionService direccionService;

    @GetMapping("/{usuarioId}")
    public List<Direccion> getDireccionesPorUsuario(@PathVariable Long usuarioId) {
        return direccionService.getDireccionesPorUsuario(usuarioId);
    }

    @PostMapping
    public Direccion saveDireccion(@RequestBody Direccion direccion) {
        return direccionService.saveDireccion(direccion);
    }

    @DeleteMapping("/{id}")
    public void deleteDireccion(@PathVariable Long id) {
        direccionService.deleteDireccion(id);
    }
}
