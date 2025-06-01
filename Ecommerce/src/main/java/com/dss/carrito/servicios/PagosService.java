package com.dss.carrito.servicios;

import com.dss.carrito.entidades.Pagos;
import com.dss.carrito.repositorios.PagosRepoCustomImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PagosService {
    private final PagosRepoCustomImpl pagosRepo;

    public List<Pagos> getPagosPorUsuario(int tenant,Long usuarioId) {
        return pagosRepo.findByUsuarioId(tenant,usuarioId);
    }

    public void savePago(int tenant, Pagos pago) {
        pagosRepo.save(tenant, pago);
    }
}
