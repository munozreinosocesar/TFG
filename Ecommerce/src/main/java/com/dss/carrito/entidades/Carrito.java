package com.dss.carrito.entidades;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "carrito")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carrito_seq")
    @SequenceGenerator(name = "carrito_seq", sequenceName = "carrito_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "carrito_productos",
        joinColumns = @JoinColumn(name = "carrito_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos = new ArrayList<>();
}
