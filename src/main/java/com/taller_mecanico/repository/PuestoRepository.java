package com.taller_mecanico.repository;

import com.taller_mecanico.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//findAll(), findById(), save(), deleteById(), existsById(), count()...
@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long> {
}
