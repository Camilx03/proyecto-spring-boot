package com.taller_mecanico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String dni;
    private Integer totalVehiculos;
}
