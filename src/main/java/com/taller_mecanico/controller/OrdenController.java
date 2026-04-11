package com.taller_mecanico.controller;

import com.taller_mecanico.dto.OrdenDTO;
import com.taller_mecanico.dto.OrdenServicioDTO;
import com.taller_mecanico.model.EstadoOrden;
import com.taller_mecanico.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Órdenes de trabajo",
        description = "Creación y seguimiento de órdenes de trabajo")
@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    // GET /api/ordenes          → todas las órdenes
    // GET /api/ordenes?estado=LISTO → solo las listas para recoger
    @Operation(summary = "Listar órdenes con filtro opcional por estado")
    @GetMapping
    public ResponseEntity<List<OrdenDTO>> listar(
            @RequestParam(required = false) EstadoOrden estado) {
        return ResponseEntity.ok(ordenService.listar(estado));
    }

    @Operation(summary = "Buscar orden por ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    // El cliente usa este endpoint con el código que le dieron en papel
    // GET /api/ordenes/codigo/T-0042
    @Operation(summary = "Buscar orden por código")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<OrdenDTO> buscarPorCodigo(
            @PathVariable String codigo) {
        return ResponseEntity.ok(ordenService.buscarPorCodigo(codigo));
    }

    // POST /api/ordenes
    // Body: { "vehiculoId": 1, "puestoId": 2, "observaciones": "Ruido al frenar" }
    @Operation(summary = "Crear una nueva orden de trabajo")
    @PostMapping
    public ResponseEntity<OrdenDTO> crear(
            @RequestBody OrdenDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenService.crear(dto));
    }

    // POST /api/ordenes/1/servicios
    // Body: { "servicioId": 3, "cantidad": 2 }
    @Operation(summary = "Añadir un servicio a una orden")
    @PostMapping("/{ordenId}/servicios")
    public ResponseEntity<OrdenDTO> añadirServicio(
            @PathVariable Long ordenId,
            @RequestBody OrdenServicioDTO dto) {
        return ResponseEntity.ok(ordenService.añadirServicio(ordenId, dto));
    }

    // DELETE /api/ordenes/1/servicios/3
    @Operation(summary = "Quitar un servicio de una orden")
    @DeleteMapping("/{ordenId}/servicios/{servicioId}")
    public ResponseEntity<OrdenDTO> quitarServicio(
            @PathVariable Long ordenId,
            @PathVariable Long servicioId) {
        return ResponseEntity.ok(ordenService.quitarServicio(ordenId, servicioId));
    }

    // PATCH /api/ordenes/1/estado
    // Body: { "estado": "EN_REPARACION" }
    // Usamos Map<String, String> para recibir solo ese campo sin crear un DTO extra
    @Operation(summary = "Cambiar el estado de una orden")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        // valueOf convierte el String "EN_REPARACION" al enum EstadoOrden.EN_REPARACION
        EstadoOrden nuevoEstado = EstadoOrden.valueOf(body.get("estado"));
        return ResponseEntity.ok(ordenService.cambiarEstado(id, nuevoEstado));
    }

    // PATCH /api/ordenes/1/observaciones
    // Body: { "observaciones": "También necesita pastillas traseras" }
    @Operation(summary = "Actualizar observaciones de una orden")
    @PatchMapping("/{id}/observaciones")
    public ResponseEntity<OrdenDTO> actualizarObservaciones(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ordenService.actualizarObservaciones(
                id, body.get("observaciones")));
    }
}