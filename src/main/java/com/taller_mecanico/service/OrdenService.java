package com.taller_mecanico.service;


import com.taller_mecanico.dto.OrdenDTO;
import com.taller_mecanico.dto.OrdenServicioDTO;
import com.taller_mecanico.model.*;
import com.taller_mecanico.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    // 1. LISTAR TODAS: Útil para el panel de administración
    public List<OrdenDTO> listarTodas() {
        return ordenRepository.findAll().stream()
                .map(this::toDTO) // Convertimos cada entidad de la lista a DTO
                .collect(Collectors.toList());
    }

    // 2. BUSCAR POR ID: El método estándar que siempre pides
    public OrdenDTO buscarPorId(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        return toDTO(orden);
    }

    // 3. BUSCAR POR CÓDIGO: Requerido para que el cliente consulte su estado
    public OrdenDTO buscarPorCodigo(String codigo) {
        Orden orden = ordenRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("No existe ninguna orden con el código: " + codigo));
        return toDTO(orden);
    }

    // 4. CREAR ORDEN: Genera el código y establece estado inicial
    @Transactional
    public OrdenDTO crear(OrdenDTO dto) {
        // Validamos que el vehículo y el puesto existan antes de crear la orden
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Puesto puesto = puestoRepository.findById(dto.getPuestoId())
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado"));

        Orden orden = new Orden();
        orden.setVehiculo(vehiculo);
        orden.setPuesto(puesto);
        orden.setObservaciones(dto.getObservaciones());

        // Lógica de negocio: Código único y estado inicial
        orden.setCodigo("ITV-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        orden.setEstado(EstadoOrden.RECIBIDO); // Empieza como RECIBIDO
        orden.setFechaEntrada(LocalDateTime.now()); // Marcamos la hora de entrada

        return toDTO(ordenRepository.save(orden));
    }

    // 5. ACTUALIZAR ESTADO: Para mover la orden por el flujo (RECIBIDO -> REVISIÓN...)
    @Transactional
    public OrdenDTO cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setEstado(nuevoEstado);
        return toDTO(ordenRepository.save(orden));
    }

    // 6. ELIMINAR: Borrado físico de la orden
    @Transactional
    public void eliminar(Long id) {
        if (!ordenRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Orden no encontrada");
        }
        ordenRepository.deleteById(id); // Gracias al cascade=ALL se borrarán sus servicios
    }

    // --- GESTIÓN DE LA TABLA INTERMEDIA (SERVICIOS) ---

    // AÑADIR SERVICIO: Crea la relación en la tabla intermedia 'orden_servicios'
    @Transactional
    public OrdenDTO añadirServicio(Long ordenId, Long servicioId, Integer cantidad) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        OrdenServicio linea = new OrdenServicio();
        linea.setOrden(orden);
        linea.setServicio(servicio);
        linea.setCantidad(cantidad != null ? cantidad : 1);
        // "Congelamos" el precio unitario del catálogo en el momento de la inserción
        linea.setPrecioUnitario(servicio.getPrecio());

        ordenServicioRepository.save(linea);

        // Forzamos el refresco de la entidad para ver los cambios en el DTO
        return toDTO(ordenRepository.findById(ordenId).get());
    }

    // --- MAPPER (CONVERSOR DE ENTIDAD A DTO) ---

    private OrdenDTO toDTO(Orden o) {
        OrdenDTO dto = new OrdenDTO();
        // Mapeo de campos simples
        dto.setId(o.getId());
        dto.setCodigo(o.getCodigo());
        dto.setEstado(o.getEstado());
        dto.setFechaEntrada(o.getFechaEntrada());
        dto.setObservaciones(o.getObservaciones());

        // Mapeo de datos del Vehículo y Cliente (Navegando por las relaciones)
        dto.setVehiculoId(o.getVehiculo().getId());
        dto.setMatricula(o.getVehiculo().getMatricula());
        dto.setMarcaModelo(o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo());
        dto.setClienteNombre(o.getVehiculo().getCliente().getNombre());
        dto.setClienteTelefono(o.getVehiculo().getCliente().getTelefono());

        // Mapeo del Puesto
        dto.setPuestoId(o.getPuesto().getId());
        dto.setPuestoNombre(o.getPuesto().getNombre());

        // TRANSFORMACIÓN DE SERVICIOS (Streams)
        // Convertimos la lista de OrdenServicio a OrdenServicioDTO
        dto.setServicios(o.getServicios().stream().map(linea -> {
            OrdenServicioDTO osDto = new OrdenServicioDTO();
            osDto.setServicioId(linea.getServicio().getId());
            osDto.setServicioNombre(linea.getServicio().getNombre());
            osDto.setCantidad(linea.getCantidad());
            osDto.setPrecioUnitario(linea.getPrecioUnitario());
            // Calculamos el subtotal (Precio * Cantidad)
            osDto.setSubTotal(linea.getPrecioUnitario() * linea.getCantidad());
            return osDto;
        }).collect(Collectors.toList()));

        // CÁLCULO DEL TOTAL GENERAL (Streams)
        // Sumamos todos los subtotales de la lista anterior
        double total = dto.getServicios().stream()
                .mapToDouble(OrdenServicioDTO::getSubTotal)
                .sum();

        dto.setTotal(total);
        dto.setTotalLineas(dto.getServicios().size());

        return dto;
    }
}


