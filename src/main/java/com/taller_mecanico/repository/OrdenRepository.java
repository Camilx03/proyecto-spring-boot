package com.taller_mecanico.repository;

import com.taller_mecanico.model.EstadoOrden;
import com.taller_mecanico.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    //Buscar por codigo al que se le entrega al cliente
    Optional<Orden> findByCodigo(String id);

    //Filtrar por estado para la pantalla de vehículos listos
    //OrderByFechaEntradaAsc: los más antiguos primero
    List<Orden> findByEstadoOrderByFechaEntradaAsc(EstadoOrden estado);

    //Todas las órdenes sin filtro ordenadas por fecha
    List<Orden> findAllByOrderByFechaEntradaAsc();

    //Órdenes de un vehículo concreto
    List<Orden> findByVehiculoId(Long vehiculoId);
}
