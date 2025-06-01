package com.dss.carrito.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pagos")
public class Pagos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagos_seq")
    @SequenceGenerator(name = "pagos_seq", sequenceName = "pagos_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    private double monto;
    private String metodoPago;
    private LocalDateTime fecha;

    public Pagos(Usuario usuario, Pedido pedido, double monto, String metodoPago) {
        this.usuario = usuario;
        this.pedido = pedido;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = LocalDateTime.now();
    }
}