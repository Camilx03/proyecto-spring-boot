package com.taller_mecanico.service;

import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Puesto;
import com.taller_mecanico.repository.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuestoService {
    @Autowired
    private PuestoRepository puestoRepository;

    public List<Puesto> listarTodos(){
        return puestoRepository.findAll();
    }

    public Puesto buscarPorId(Long id){
        return puestoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Puesto", id));
    }

    public Puesto crear(Puesto puesto){
        return puestoRepository.save(puesto);
    }

    public void eliminar(Long id){
        buscarPorId(id);
        puestoRepository.deleteById(id);
    }
}
