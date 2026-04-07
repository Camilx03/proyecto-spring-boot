package com.taller_mecanico.service;


import com.taller_mecanico.dto.OrdenDTO;
import com.taller_mecanico.dto.OrdenServicioDTO;
import com.taller_mecanico.exceptions.BadRequestException;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.*;
import com.taller_mecanico.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    // Inyectamos todos los repositorios necesarios para interactuar con la DB
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private PuestoRepository puestoRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private OrdenServicioRepository ordenServicioRepository;

    // --- MÉTODOS CRUD BÁSICOS ---

    // 1. LISTAR TODAS
    public List<OrdenDTO> listar(EstadoOrden estado) {
        List<Orden> ordenes;
        if (estado != null) {
            ordenes = ordenRepository.findByEstadoOrderByFechaEntradaAsc(estado);
        } else {
            ordenes = ordenRepository.findAllByOrderByFechaEntradaAsc();
        }
        return ordenes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // 2. BUSCAR POR ID
    public OrdenDTO buscarPorId(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo", id));
        return toDTO(orden);
    }

    // 3. BUSCAR POR CÓDIGO: Requerido para que el cliente consulte su estado
    public OrdenDTO buscarPorCodigo(String codigo) {
        Orden orden = ordenRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden con código " + codigo + " no encontrada"));
        return toDTO(orden);
    }

    // Genera el código público.
    // count() cuenta cuántas órdenes hay ya y suma 1.
    // String.format("%04d", 42) produce "0042" rellenando con ceros.
    private String generarCodigo(String tipoPuesto) {
        long count = ordenRepository.count() + 1;
        String prefijo = "ITV".equalsIgnoreCase(tipoPuesto) ? "ITV" : "T";
        return prefijo + "-" + String.format("%04d", count);
    }

    // 4. CREAR ORDEN: Genera el código y establece estado inicial
    public OrdenDTO crear(OrdenDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehículo", dto.getVehiculoId()));

        Puesto puesto = puestoRepository.findById(dto.getPuestoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Puesto", dto.getPuestoId()));

        Orden orden = new Orden();
        orden.setVehiculo(vehiculo);
        orden.setPuesto(puesto);
        orden.setEstado(EstadoOrden.RECIBIDO);
        orden.setFechaEntrada(LocalDateTime.now());
        orden.setObservaciones(dto.getObservaciones());
        // El código se genera automáticamente según el tipo de puesto
        orden.setCodigo(generarCodigo(puesto.getTipo()));

        return toDTO(ordenRepository.save(orden));
    }

    // 5. ACTUALIZAR ESTADO: Para mover la orden por el flujo (RECIBIDO -> REVISIÓN...)
    public OrdenDTO cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo", id));

        // Obtenemos la posición del estado actual y del nuevo
        // en la lista de valores del enum.
        // Si el nuevo está antes que el actual, es un retroceso.
        List<EstadoOrden> estados = List.of(EstadoOrden.values());
        int posActual = estados.indexOf(orden.getEstado());
        int posNuevo = estados.indexOf(nuevoEstado);

        if (posNuevo <= posActual) {
            throw new BadRequestException(
                    "No se puede cambiar de " + orden.getEstado()
                            + " a " + nuevoEstado + ". Los estados solo avanzan.");
        }
        orden.setEstado(nuevoEstado);

        return toDTO(ordenRepository.save(orden));
    }

    //6. ACTUALIZAR OBSERVACIONES:
    public OrdenDTO actualizarObservaciones(Long id, String observaciones) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo", id));
        orden.setObservaciones(observaciones);
        return toDTO(ordenRepository.save(orden));
    }

    /* 6. ELIMINAR: Borrado físico de la orden
    public void eliminar(Long id) {
        if (!ordenRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Orden no encontrada");
        }
        ordenRepository.deleteById(id); // Gracias al cascade=ALL se borrarán sus servicios
    }*/

    // --- GESTIÓN DE LA TABLA INTERMEDIA (SERVICIOS) ---

    // AÑADIR SERVICIO: Crea la relación en la tabla intermedia 'orden_servicios'
    public OrdenDTO añadirServicio(Long ordenId, OrdenServicioDTO dto) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo", ordenId));

        if (orden.getEstado() == EstadoOrden.ENTREGADO) {
            throw new BadRequestException(
                    "No se pueden añadir servicios a una orden ya entregada.");
        }

        Servicio servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Servicio", dto.getServicioId()));

        if (!servicio.getActivo()) {
            throw new BadRequestException(
                    "El servicio '" + servicio.getNombre() + "' está desactivado.");
        }
        // Buscamos si ya existe esa línea de servicio en la orden
        OrdenServicio lineaExistente = ordenServicioRepository
                .findByOrdenIdAndServicioId(ordenId, dto.getServicioId())
                .orElse(null);

        if (lineaExistente != null) {
            // Ya existe: solo sumamos la cantidad nueva a la existente
            lineaExistente.setCantidad(lineaExistente.getCantidad()
                    + dto.getCantidad());
            ordenServicioRepository.save(lineaExistente);
        } else {
            // No existe: creamos una línea nueva
            OrdenServicio nueva = new OrdenServicio();
            nueva.setOrden(orden);
            nueva.setServicio(servicio);
            nueva.setCantidad(dto.getCantidad());
            // Guardamos el precio actual del servicio en la línea
            nueva.setPrecioUnitario(servicio.getPrecio());
            ordenServicioRepository.save(nueva);
        }

        // Recargamos la orden completa para devolver el DTO actualizado
        return toDTO(ordenRepository.findById(ordenId).get());
    }
    // QUITAR SERVICIO:
    public OrdenDTO quitarServicio(Long ordenId, Long servicioId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo", ordenId));

        if (orden.getEstado() == EstadoOrden.ENTREGADO) {
            throw new BadRequestException(
                    "No se pueden modificar servicios de una orden ya entregada.");
        }

        OrdenServicio linea = ordenServicioRepository
                .findByOrdenIdAndServicioId(ordenId, servicioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El servicio indicado no pertenece a esta orden"));

        ordenServicioRepository.delete(linea);
        return toDTO(ordenRepository.findById(ordenId).get());
    }

    // --- MAPPER (CONVERSOR DE ENTIDAD A DTO) ---

    private OrdenDTO toDTO(Orden orden) {

        // Primero convertimos cada línea de servicio en OrdenServicioDTO
        List<OrdenServicioDTO> serviciosDTO = new ArrayList<>();
        for (OrdenServicio ordenServicio : orden.getServicios()) {
            OrdenServicioDTO osDTO = new OrdenServicioDTO();
            osDTO.setServicioId(ordenServicio.getServicio().getId());
            osDTO.setServicioNombre(ordenServicio.getServicio().getNombre());
            osDTO.setCategoriaNombre(ordenServicio.getServicio().getCategoria().getNombre());
            osDTO.setCantidad(ordenServicio.getCantidad());
            osDTO.setPrecioUnitario(ordenServicio.getPrecioUnitario());
            osDTO.setSubTotal(ordenServicio.getCantidad() * ordenServicio.getPrecioUnitario());
            serviciosDTO.add(osDTO);
        }
        // Calculamos el total sumando todos los subtotales con streams
        // mapToDouble convierte cada línea a un double (su subtotal)
        // sum() los suma todos
        double total = orden.getServicios()
                .stream()
                .mapToDouble(os -> os.getCantidad() * os.getPrecioUnitario())
                .sum();

        OrdenDTO dto = new OrdenDTO();
        dto.setId(orden.getId());
        dto.setCodigo(orden.getCodigo());
        dto.setEstado(orden.getEstado());
        dto.setFechaEntrada(orden.getFechaEntrada());
        dto.setFechaEntrega(orden.getFechaEntrada());
        dto.setObservaciones(orden.getObservaciones());
        dto.setVehiculoId(orden.getVehiculo().getId());
        dto.setMatricula(orden.getVehiculo().getMatricula());
        dto.setMarcaModelo(orden.getVehiculo().getMarca() + " "
                + orden.getVehiculo().getModelo()
                + " (" + orden.getVehiculo().getAnio() + ")");
        dto.setClienteId(orden.getVehiculo().getCliente().getId());
        dto.setClienteNombre(orden.getVehiculo().getCliente().getNombre());
        dto.setClienteTelefono(orden.getVehiculo().getCliente().getTelefono());
        dto.setPuestoId(orden.getPuesto().getId());
        dto.setPuestoNombre(orden.getPuesto().getNombre());
        dto.setServicios(serviciosDTO);
        dto.setTotal(total);
        dto.setTotalLineas(serviciosDTO.size());
        return dto;
    }
}


