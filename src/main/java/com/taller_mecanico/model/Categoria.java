package com.taller_mecanico.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ejem: mecanica, electricidad, itv, chapa
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Servicio> servicios;
}
