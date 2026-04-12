package com.taller_mecanico.service;


import com.taller_mecanico.dto.CategoriaDTO;
import com.taller_mecanico.dto.ServicioDTO;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Categoria;
import com.taller_mecanico.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> findAll() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO findById(Long id) {
        Categoria cat = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada", id));
        return toDTO(cat);
    }

    public CategoriaDTO create(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        // No seteamos la lista de servicios aquí para evitar errores de persistencia en cascada
        return toDTO(categoriaRepository.save(categoria));
    }

    private CategoriaDTO toDTO(Categoria c) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        // Mapeamos la lista de servicios internos a ServicioDTO
        if (c.getServicios() != null) {
            dto.setServicios(c.getServicios().stream().map(s -> {
                ServicioDTO sDto = new ServicioDTO();
                sDto.setId(s.getId());
                sDto.setNombre(s.getNombre());
                sDto.setPrecio(s.getPrecio());
                sDto.setActivo(s.getActivo());
                sDto.setCategoriaId(c.getId());
                sDto.setCategoriaNombre(c.getNombre());
                return sDto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}


