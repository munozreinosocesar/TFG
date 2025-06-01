package com.dss.carrito.servicios;

import com.dss.carrito.entidades.Usuario;
import com.dss.carrito.repositorios.UsuarioRepoCustomImpl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepoCustomImpl usuarioRepo;

    public List<Usuario> getAllUsersPublic() {
        return usuarioRepo.findAllByTenant(0);
    }

    public List<Usuario> getAllUsers(int tenant) {
        return usuarioRepo.findAllByTenant(tenant);
    }

    public Optional<Usuario> getUserById(int tenant, Long id) {
        List<Usuario> usuarios = usuarioRepo.findAllByTenant(tenant);
        return usuarios.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public void saveUser(int tenant, Usuario usuario) {
        usuarioRepo.setSchema(tenant);
        usuarioRepo.save(tenant, usuario);
    }

    public void deleteUser(int tenant, Long id) {
        usuarioRepo.setSchema(tenant);
        usuarioRepo.deleteById(tenant, id);
    }

    public void updateUser(int tenant, Long id, Usuario usuario) {
        usuarioRepo.setSchema(tenant);
        usuarioRepo.save(tenant, usuario);
    }

    public void updateUser(int tenant, Usuario usuario) {
        usuarioRepo.setSchema(tenant);
        usuarioRepo.save(tenant, usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        int tenant = obtenerTenantDesdeEmail(email);
        Optional<Usuario> usuarioOptional = usuarioRepo.findByEmailAndTenant(tenant, email);
    
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    
        String role = usuario.getEmail().startsWith("admin") ? "ADMIN" : "USER";
    
        return User.builder()
                .username(usuario.getEmail())
                .password("{noop}" + usuario.getPassword())
                .roles(role)
                .build();
    }
    
    private int obtenerTenantDesdeEmail(String email) {
        String regex = "(\\d+)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(email);
    
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
    
        throw new UsernameNotFoundException("No se pudo extraer el tenant del email: " + email);
    }
    
    public Optional<Long> obtenerUsuarioIdDesdeEmail(String email, int tenant) {
        return usuarioRepo.findByEmailAndTenant(tenant, email)
                .map(Usuario::getId);
    }
    
}
