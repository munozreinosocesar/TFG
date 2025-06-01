package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Pagos;
import com.dss.carrito.servicios.PagosService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pagos/{tenant}/{usuario}")
@AllArgsConstructor
public class PagosController {
    private final PagosService pagoService;

    @GetMapping
    public List<Pagos> getPagosPorUsuario(@PathVariable int tenant, @PathVariable Long usuario) {
        return pagoService.getPagosPorUsuario(tenant, usuario);
    }

    @PostMapping
    public void savePago(@PathVariable int tenant, @RequestBody Pagos pago) {
        pagoService.savePago(tenant, pago);
    }
}