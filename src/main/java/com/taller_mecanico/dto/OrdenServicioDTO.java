package com.taller_mecanico.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioDTO {
    private Long servicioId;
    private Integer cantidad;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT, Se obtiene del Servicio por el ID
    private String servicioNombre;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se obtiene del Servicio, por el ID
    private String categoriaNombre;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se obtiene del Servicio, por el ID
    private Double precioUnitario;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se calcula en el Service
    private Double subTotal;
}
