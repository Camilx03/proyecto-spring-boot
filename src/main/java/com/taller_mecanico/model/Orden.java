package com.taller_mecanico.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //codigo publico que se entrega al cliente: T-0042, ITV-0017
    @Column(nullable = false, unique = true)
    private String codigo;

    //El estado se guarda como texto en la base de datos
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado = EstadoOrden.RECIBIDO;

    //Fecha y hora en que entró el vehículo
    @Column(nullable = false)
    private LocalDateTime fechaEntrada =  LocalDateTime.now();

    //Observaciones del mecánico
    private String observaciones;

    //El vehículo al que pertenece esta orden
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    //El puesto donde se gestionó recepción, box
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puesto puesto;

    //Lista de servicios realizados en esta orden
    //cascade=ALL: si se borra la orden, se borran sus líneas de servicio
    //orphanRemoval=true: si quitamos un servicio de la lista, se borra de la BD
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrdenServicio> servicios = new ArrayList<>();
}
