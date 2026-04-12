package com.taller_mecanico.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuestoDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private Long id;
    private String nombre;
    private String tipo;
}
