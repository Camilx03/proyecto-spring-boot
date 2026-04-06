package com.taller_mecanico.dto;

import com.taller_mecanico.model.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    private Long id;
    private String codigo;
    private EstadoOrden estado;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaEntrega;
    private String observaciones;

    private Long vehiculoId;
    private Long puestoId;
    private String matricula;
    private String marcaModelo;

    private Long clienteId;
    private String clienteNombre;
    private String clienteTelefono;

    private String puestoNombre;
    private List<OrdenServicioDTO> servicios;
    private Double total;
    private Integer totalLineas;
}
