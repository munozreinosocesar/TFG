package com.dss.carrito.controladores;

import com.dss.carrito.entidades.Pagos;
import com.dss.carrito.entidades.Producto;
import com.dss.carrito.entidades.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class UriController {
    private final ProductController productController;
    private final CarritoController carritoController;
    private final UsuarioController usuarioController;
    private final PagosController pagosController;

    @GetMapping("/{tenant}/{usuario}/productos")
    public String listProducts(@PathVariable int tenant, @PathVariable Long usuario, Model model) {
        List<Producto> allProducts = productController.getAllProduct(tenant);
        model.addAttribute("allProductos", allProducts);
        model.addAttribute("tenant", tenant);
        model.addAttribute("usuario", usuario);
    
        return "productos";
    }
    
    @GetMapping("/{tenant}/usuarios")
    public String listUsers(@PathVariable int tenant, Model model) {
        List<Usuario> allUsers = usuarioController.getAllUsers(tenant);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("tenant", tenant);
        return "usuarios";
    }

    @RequestMapping("/")
    public String root(Model model) {
            return "login"; 
    }

    @RequestMapping("/{tenant}/form")
    public String formProduct(@PathVariable int tenant, Model model, @RequestParam(value = "edit", required = false) Boolean edit, @ModelAttribute Producto product) {
        model.addAttribute("tenant", tenant);
        return "formulario-producto";
    }

    @RequestMapping("/{tenant}/form_usuarios")
    public String formUser(@PathVariable int tenant, Model model, @RequestParam(value = "edit", required = false) Boolean edit, @ModelAttribute Usuario usuario) {
        model.addAttribute("tenant", tenant);
        return "formulario-usuario";
    }

    @RequestMapping("/{tenant}/{usuario}/cart")
    public String cart(@PathVariable int tenant, @PathVariable Long usuario, Model model) {
        List<Producto> productosCarrito = carritoController.getAllProducts(tenant, usuario);
        model.addAttribute("allProductos", productosCarrito);
        model.addAttribute("tenant", tenant);
        model.addAttribute("usuario", usuario);
        return "carrito";
    }

    @RequestMapping("/{tenant}/{usuario}/pagos")
    public String pagos(@PathVariable int tenant, @PathVariable Long usuario, Model model) {
        List<Pagos> listaPagos = pagosController.getPagosPorUsuario(tenant, usuario);
        model.addAttribute("listaPagos", listaPagos);
        model.addAttribute("tenant", tenant);
        model.addAttribute("usuario", usuario);
        return "pagos";
    }
    

    @PostMapping("/{tenant}/{usuario}/checkout")
    public String checkout(@PathVariable int tenant, @PathVariable Long usuario, Model model) {
        List<Producto> productosCarrito = carritoController.getAllProducts(tenant, usuario);
        double total = productosCarrito.stream().mapToDouble(Producto::getPrice).sum();
        List<Usuario> usuarios = usuarioController.getAllUsers(tenant);

        model.addAttribute("productosCarrito", productosCarrito);
        model.addAttribute("total", total);
        model.addAttribute("tenant", tenant);
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarios);

        if (productosCarrito.isEmpty()) {
            model.addAttribute("error", "El carrito está vacío.");
        }

        return "checkout";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) Boolean error) {
        if (error != null && error) {
            model.addAttribute("error", error);
            return "login"; 
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String username = auth.getName();
            int tenantId = extractTenantId(username);
            Long usuarioId = extractUsuarioId(username);
            return "redirect:/" + tenantId + "/" + usuarioId + "/productos";
        }

        return "login"; 
    }

    private int extractTenantId(String username) {
        if (username.startsWith("tenant")) {
            return Integer.parseInt(username.replace("tenant", ""));
        } else if (username.startsWith("user")) {
            return Integer.parseInt(username.replace("user", ""));
        }
        return 1;
    }

    private Long extractUsuarioId(String username) {
        String regex = "(\\d+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(username);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return 1L;
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
