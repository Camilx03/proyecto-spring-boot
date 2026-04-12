package com.taller_mecanico.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String dni;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // El cliente no debe enviar esto
    private Integer totalVehiculos;
}
