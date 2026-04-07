package com.taller_mecanico.repository;

import com.taller_mecanico.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    //Solo servicios activos
    List<Servicio> findByActivoTrue();

    //servicios de una categoria concreta
    List<Servicio> findByCategoriaId(Long categoriaId);

    //servicios activos de una categoria concreta
    List<Servicio> findByActivoTrueAndCategoriaId(Long categoriaId);
}
