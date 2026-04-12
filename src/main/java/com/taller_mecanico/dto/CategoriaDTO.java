package com.taller_mecanico.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private Long id;
    private String nombre;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // Oculta el campo en peticiones POST/PUT
    private List<ServicioDTO> servicios; // Aquí incluimos la lista que querías
    }

