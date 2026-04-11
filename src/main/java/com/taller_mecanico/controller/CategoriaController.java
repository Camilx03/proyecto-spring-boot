package com.taller_mecanico.controller;

import com.taller_mecanico.dto.ServicioDTO;
import com.taller_mecanico.model.Categoria;
import com.taller_mecanico.service.CategoriaService;
import com.taller_mecanico.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorías", description = "Categorías de servicios del taller")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ServicioService servicioService;

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @Operation(summary = "Crear una nueva categoría")
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.create(categoria));
    }

    // Aprovecha la bidireccionalidad de la relación Categoria → Servicio
    // GET /api/categorias/1/servicios
    @Operation(summary = "Listar servicios de una categoría")
    @GetMapping("/{id}/servicios")
    public ResponseEntity<List<ServicioDTO>> serviciosDeCategoria(
            @PathVariable Long id) {
        return ResponseEntity.ok(servicioService.listarPorCategoria(id));
    }
}