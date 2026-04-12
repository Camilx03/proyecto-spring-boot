package com.taller_mecanico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //La matrícula es única: no puede haber dos vehículos con la misma
    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    private Integer anio;

    //Muchos vehículos pertenecen a un cliente.JoinColumn define la columna de clave foránea en la tabla "vehiculos".
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
