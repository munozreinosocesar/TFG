package com.dss.carrito.controladores;

import com.dss.carrito.entidades.*;
import com.dss.carrito.servicios.UsuarioService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/usuarios/{tenant}")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsers(@PathVariable int tenant){
        return usuarioService.getAllUsers(tenant);
    }

    @GetMapping("/{id}")
    public Usuario getProductById(@PathVariable int tenant, @PathVariable Long id){
        Optional<Usuario> user = usuarioService.getUserById(tenant,id);
        return user.orElse(null);
    }

    @PostMapping
    public String saveUser(@PathVariable int tenant, @ModelAttribute Usuario usuario){
        usuarioService.saveUser(tenant, usuario);
        return "redirect:/" + tenant + "/usuarios";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int tenant, @PathVariable Long id){
        usuarioService.deleteUser(tenant, id);
        return "redirect:/" + tenant + "/usuarios";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int tenant, @PathVariable Long id, @ModelAttribute Usuario updatedUser){
        Optional<Usuario> optionalUser = usuarioService.getUserById(tenant, id);

        if (optionalUser.isPresent()) {
            Usuario existingUser = optionalUser.get();
            existingUser.setNombre(updatedUser.getNombre());
            existingUser.setApellido(updatedUser.getApellido());
            existingUser.setEmail(updatedUser.getEmail());

            if (updatedUser.getDireccion() != null) {
                existingUser.setDireccion(updatedUser.getDireccion());
            }

            usuarioService.saveUser(tenant, existingUser);
        }
        return "redirect:/" + tenant + "/usuarios";
    }
}
