package com.taller_mecanico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private Boolean activo;
    private Long categoriaId;
    private String categoriaNombre;
}
