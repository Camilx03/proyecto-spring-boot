package com.taller_mecanico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioDTO {
    private Long servicioId;
    private Integer cantidad;
    private String servicioNombre;
    private String categoriaNombre;
    private Double precioUnitario;
    private Double subTotal;
}
