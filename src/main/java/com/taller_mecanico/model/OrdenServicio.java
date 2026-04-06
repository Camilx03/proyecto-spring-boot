package com.taller_mecanico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Tabla intermedia
@Entity
@Table(name = "orden_servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    //Cuántas veces se aplica este servicio ejem: 2 cambios de filtro
    @Column(nullable = false)
    private Integer cantidad = 1;

    //Precio en el momento de añadir el servicio a la orden
    @Column(nullable = false)
    private Double precioUnitario;
}
