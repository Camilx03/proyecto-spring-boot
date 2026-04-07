package com.taller_mecanico.controller;

import com.taller_mecanico.dto.VehiculoDTO;
import com.taller_mecanico.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehículos", description = "Registro y búsqueda de vehículos")
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Operation(summary = "Listar todos los vehículos")
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listar() {
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }

    @Operation(summary = "Buscar vehículo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.buscarPorId(id));
    }

    // GET /api/vehiculos/matricula/1234-ABC
    @Operation(summary = "Buscar vehículo por matrícula")
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<VehiculoDTO> buscarPorMatricula(
            @PathVariable String matricula) {
        return ResponseEntity.ok(vehiculoService.buscarPorMatricula(matricula));
    }

    @Operation(summary = "Registrar un nuevo vehículo")
    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(@RequestBody VehiculoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehiculoService.crear(dto));
    }
}
