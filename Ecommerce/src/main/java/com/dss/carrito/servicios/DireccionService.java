package com.dss.carrito.servicios;

import com.dss.carrito.entidades.Direccion;
import com.dss.carrito.repositorios.DireccionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DireccionService {
    private final DireccionRepo direccionRepo;

    public List<Direccion> getDireccionesPorUsuario(Long usuarioId) {
        return direccionRepo.findByUsuarioId(usuarioId);
    }

    public Direccion saveDireccion(Direccion direccion) {
        return direccionRepo.save(direccion);
    }

    public void deleteDireccion(Long id) {
        direccionRepo.deleteById(id);
    }
}
