package com.taller_mecanico.service;


import com.taller_mecanico.dto.ClienteDTO;
import com.taller_mecanico.dto.ServicioDTO;
import com.taller_mecanico.exceptions.BadRequestException;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Categoria;
import com.taller_mecanico.model.Cliente;
import com.taller_mecanico.model.Servicio;
import com.taller_mecanico.repository.CategoriaRepository;
import com.taller_mecanico.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
   private CategoriaRepository categoriaRepository;


    private ServicioDTO toDTO(Servicio servicio){
        ServicioDTO servicioDTO = new ServicioDTO();
        servicioDTO.setId(servicio.getId());
        servicioDTO.setNombre(servicio.getNombre());
        servicioDTO.setPrecio(servicio.getPrecio());
        servicioDTO.setActivo(servicio.getActivo());
        servicioDTO.setCategoriaId(servicio.getCategoria().getId());
        servicioDTO.setCategoriaNombre(servicio.getCategoria().getNombre());
        return servicioDTO;
    }


    public List<ServicioDTO> listarActivo(Boolean soloActivos, Long categoriaId,
                                    String ordenarPor) {
        List<Servicio> lista;
        if (soloActivos != null && soloActivos && categoriaId != null) {
            lista = servicioRepository.findByActivoTrueAndCategoriaId(categoriaId);
        } else if (soloActivos != null && soloActivos) {
            lista = servicioRepository.findByActivoTrue();
        } else if (categoriaId != null) {
            lista = servicioRepository.findByCategoriaId(categoriaId);
        } else {
            lista = servicioRepository.findAll();
        }
        if ("precio".equalsIgnoreCase(ordenarPor)) {
            lista = lista.stream()
                    .sorted(Comparator.comparingDouble(Servicio::getPrecio))
                    .collect(Collectors.toList());
        } else if ("nombre".equalsIgnoreCase(ordenarPor)) {
            lista = lista.stream()
                    .sorted(Comparator.comparing(Servicio::getNombre))
                    .collect(Collectors.toList());
        }

        return lista.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ServicioDTO findById(Long id){
        Servicio servicio = servicioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Servicio", id));
        return toDTO(servicio);
    }

    public ServicioDTO crear(ServicioDTO dto) {
        //creamos la entidad a partir del DTO
        Servicio servicio = new Servicio();
        servicio.setNombre(dto.getNombre());
        servicio.setPrecio(dto.getPrecio());
        servicio.setActivo(true);
        servicio.setCategoria(categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.getCategoriaId())));

        return toDTO(servicioRepository.save(servicio));
    }

    public List<ServicioDTO> listarPorCategoria(Long categoriaId) {
        categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría", categoriaId));
        return servicioRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ServicioDTO actualizar(Long id, ServicioDTO dto) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", id));

        if (dto.getNombre() != null) servicio.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) servicio.setPrecio(dto.getPrecio());
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository
                    .findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría", dto.getCategoriaId()));
            servicio.setCategoria(categoria);
        }

        return toDTO(servicioRepository.save(servicio));
    }

    public ServicioDTO desactivar(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", id));
        servicio.setActivo(false);
        return toDTO(servicioRepository.save(servicio));
    }


}
