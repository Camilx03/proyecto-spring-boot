package com.taller_mecanico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {
    private Long id;
    private String matricula;
    private String marca;
    private String modelo;
    private Integer anio;
    private Long clienteId;
    private String clienteNombre;
    private String clienteTelefono;
}
