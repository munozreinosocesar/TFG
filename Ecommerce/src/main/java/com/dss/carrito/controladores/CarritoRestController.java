package com.dss.carrito.controladores;

import com.dss.carrito.servicios.CarritoService;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@NoArgsConstructor
@RequestMapping("/api")
public class CarritoRestController {
    CarritoService carritoService;
    ProductRestController productResController;
}
