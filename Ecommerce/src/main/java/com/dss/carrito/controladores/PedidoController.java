package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Pagos;
import com.dss.carrito.entidades.Pedido;
import com.dss.carrito.entidades.Producto;
import com.dss.carrito.entidades.Usuario;
import com.dss.carrito.servicios.CarritoService;
import com.dss.carrito.servicios.PagosService;
import com.dss.carrito.servicios.PedidoService;
import com.dss.carrito.servicios.UsuarioService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/pedidos/{tenant}/{usuario}")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PagosService pagoService;
    private final UsuarioService usuarioService;
    private final CarritoService carritoService;


    @PostMapping
    public String savePedido(@PathVariable int tenant, 
                             @PathVariable Long usuario, 
                             @RequestParam String metodoPago, 
                             @RequestParam double monto, 
                             Model model) {
        
        Optional<Usuario> usuario1 = usuarioService.getUserById(tenant, usuario);
        
        List<Producto> productos = carritoService.getAllProducts(tenant);
        
        Usuario usuario2 = usuario1.orElse(new Usuario());

        Pedido pedido = new Pedido(usuario2, productos, monto);
        pedidoService.savePedido(tenant, pedido);
        
        Pagos pago = new Pagos(usuario2, pedido, monto, metodoPago);
        pagoService.savePago(tenant, pago);
        
        carritoService.vaciarCarrito(tenant, usuario);
        
        return "redirect:/" + tenant + "/" + usuario + "/productos";
    }
}
