package com.taller_mecanico.controller;

import com.taller_mecanico.model.Puesto;
import com.taller_mecanico.service.PuestoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Puestos", description = "Gestión de puestos de recepción y trabajo")
@RestController
@RequestMapping("/api/puestos")
public class PuestoController {

    @Autowired
    private PuestoService puestoService;

    @Operation(summary = "Listar todos los puestos")
    @GetMapping
    public ResponseEntity<List<Puesto>> listar() {
        return ResponseEntity.ok(puestoService.listarTodos());
    }

    @Operation(summary = "Buscar puesto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Puesto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(puestoService.buscarPorId(id));
    }

    @Operation(summary = "Crear un nuevo puesto")
    @PostMapping
    public ResponseEntity<Puesto> crear(@RequestBody Puesto puesto) {
        // CREATED devuelve código 201 en lugar de 200
        // 201 significa que se creó un nuevo recurso
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(puestoService.crear(puesto));
    }

    @Operation(summary = "Eliminar un puesto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        puestoService.eliminar(id);
        // noContent() devuelve código 204: éxito pero sin cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}
