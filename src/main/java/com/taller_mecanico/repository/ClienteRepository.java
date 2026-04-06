package com.taller_mecanico.repository;

import com.taller_mecanico.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    //busca cliente por dni
    Optional<Cliente> findByDni(String dni);

    //Busca cliente cuyo nombre contenga lo que pasemos ignorando mayusculas
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
