package com.dss.carrito.servicios;

import com.dss.carrito.entidades.Pedido;
import com.dss.carrito.repositorios.PedidoRepoCustomImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepoCustomImpl pedidoRepo;

    @Transactional
    public void savePedido(int tenant, Pedido pedido) {
        pedidoRepo.save(tenant, pedido);
    }

    public List<Pedido> getAllPedidos(int tenant) {
        return pedidoRepo.findAllByTenant(tenant);
    }

    public Optional<Pedido> getPedidoById(int tenant, Long id) {
        List<Pedido> pedidos = pedidoRepo.findAllByTenant(tenant);
        return pedidos.stream().filter(p -> p.getId().equals(id)).findFirst();    
    }
}
