package com.taller_mecanico.service;


import com.taller_mecanico.exceptions.BadRequestException;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Categoria;
import com.taller_mecanico.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria>  findAll() {
        return categoriaRepository.findAll();
    }


    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Categoria no encontrado", id));
    }


    public Categoria create(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }





}


