package com.taller_mecanico.dto;

import com.taller_mecanico.model.EstadoOrden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private Long id;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se genera en el Service
    private String codigo;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se genera por defecto en RECIBIDO cuando creamos una orden
    private EstadoOrden estado;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se genera por defecto en RECIBIDO cuando creamos una orden
    private LocalDateTime fechaEntrada;
    private String observaciones;

    private Long vehiculoId;
    private Long puestoId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT, se obtiene en el Service
    private String matricula;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT, se obtiene en el Service
    private String marcaModelo;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se obtiene a partir del Vehiculo asociado
    private Long clienteId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private String clienteNombre;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private String clienteTelefono;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private String puestoNombre;
    private List<OrdenServicioDTO> servicios;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se calcula en el Service
    private Double total;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Se calcula en el Service
    private Integer totalLineas;
}
