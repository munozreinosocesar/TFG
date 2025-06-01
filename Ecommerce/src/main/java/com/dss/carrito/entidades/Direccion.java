package com.dss.carrito.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "direccion_seq")
    @SequenceGenerator(name = "direccion_seq", sequenceName = "direccion_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    private String calle;
    private String ciudad;
    private String pais;
    private String codigoPostal;
}