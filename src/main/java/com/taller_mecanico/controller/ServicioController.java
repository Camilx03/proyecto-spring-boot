package com.taller_mecanico.controller;

import com.taller_mecanico.dto.ServicioDTO;
import com.taller_mecanico.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Servicios", description = "Catálogo de servicios ofrecidos")
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    // GET /api/servicios
    // GET /api/servicios?activo=true
    // GET /api/servicios?categoria=1
    // GET /api/servicios?activo=true&categoria=1&orden=precio
    // required=false hace que el parámetro sea opcional
    @Operation(summary = "Listar servicios con filtros opcionales")
    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listar(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Long categoria,
            @RequestParam(required = false) String orden) {
        return ResponseEntity.ok(servicioService.listarActivo(activo, categoria, orden));
    }

    @Operation(summary = "Buscar servicio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.findById(id));
    }

    @Operation(summary = "Crear un nuevo servicio")
    @PostMapping
    public ResponseEntity<ServicioDTO> crear(@RequestBody ServicioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicioService.crear(dto));
    }

    @Operation(summary = "Actualizar un servicio")
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> actualizar(@PathVariable Long id,
                                                  @RequestBody ServicioDTO dto) {
        return ResponseEntity.ok(servicioService.actualizar(id, dto));
    }

    // PATCH solo cambia el campo activo a false
    // No borramos servicios, los desactivamos
    @Operation(summary = "Desactivar un servicio")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ServicioDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.desactivar(id));
    }
}
