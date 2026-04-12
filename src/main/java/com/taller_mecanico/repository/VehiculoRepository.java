package com.taller_mecanico.repository;

import com.taller_mecanico.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    //Busca un vehiculo por su matricula
    Optional<Vehiculo> findByMatriculaIgnoreCase(String matricula);

    //Todos los vehiculos de un cliente concreto
    List<Vehiculo> findByClienteId(Long clienteId);
}
