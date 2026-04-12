package com.taller_mecanico.service;

import com.taller_mecanico.dto.PuestoDTO;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Puesto;
import com.taller_mecanico.repository.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PuestoService {
    @Autowired
    private PuestoRepository puestoRepository;

    public List<PuestoDTO> listarTodos() {
        return puestoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PuestoDTO buscarPorId(Long id) {
        Puesto p = puestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto", id));
        return toDTO(p);
    }

    public PuestoDTO crear(PuestoDTO dto) {
        Puesto puesto = new Puesto();
        puesto.setNombre(dto.getNombre());
        puesto.setTipo(dto.getTipo());
        // El ID no se settea, lo genera la DB
        return toDTO(puestoRepository.save(puesto));

    }

    private PuestoDTO toDTO(Puesto p) {
        PuestoDTO puestoDTO = new PuestoDTO();
        puestoDTO.setId(p.getId());
        puestoDTO.setNombre(p.getNombre());
        puestoDTO.setTipo(p.getTipo());
        return puestoDTO;
    }

    public void eliminar(Long id) {
        if (!puestoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Puesto", id);
        }
        puestoRepository.deleteById(id);
    }
}
