package com.taller_mecanico.repository;

import com.taller_mecanico.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {
    //Todos los servicios de una orden
    List<OrdenServicio> findByOrdenId(Long ordenId);

    //Buscar si ya existe ese servicio en ese orden para no duplicar
    Optional<OrdenServicio> findByOrdenIdAndServicioId(Long ordenId, Long servicioId);
}
