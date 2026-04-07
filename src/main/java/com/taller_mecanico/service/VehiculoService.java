package com.taller_mecanico.service;

import com.taller_mecanico.dto.VehiculoDTO;
import com.taller_mecanico.exceptions.BadRequestException;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Cliente;
import com.taller_mecanico.model.Vehiculo;
import com.taller_mecanico.repository.ClienteRepository;
import com.taller_mecanico.repository.PuestoRepository;
import com.taller_mecanico.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    private VehiculoDTO toDTO(Vehiculo v) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setId(v.getId());
        dto.setMatricula(v.getMatricula());
        dto.setMarca(v.getMarca());
        dto.setModelo(v.getModelo());
        dto.setAnio(v.getAnio());
        dto.setClienteId(v.getCliente().getId());
        dto.setClienteNombre(v.getCliente().getNombre());
        dto.setClienteTelefono(v.getCliente().getTelefono());
        return dto;
    }

    public List<VehiculoDTO> listarTodos() {
        return vehiculoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VehiculoDTO buscarPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", id));
        return toDTO(vehiculo);
    }

    public VehiculoDTO buscarPorMatricula(String matricula) {
        Vehiculo vehiculo = vehiculoRepository
                .findByMatriculaIgnoreCase(matricula)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehículo con matrícula " + matricula + " no encontrado"));
        return toDTO(vehiculo);
    }

    public List<VehiculoDTO> listarPorCliente(Long clienteId) {
        return vehiculoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VehiculoDTO crear(VehiculoDTO dto) {
        //verificar que la matrícula no exista ya
        vehiculoRepository.findByMatriculaIgnoreCase(dto.getMatricula())
                .ifPresent(
                        v -> {throw new BadRequestException("Ya existe un vehículo con la matrícula: " + dto.getMatricula());
                        });
        //buscar el cliente al que pertenece
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cliente", dto.getClienteId()));
        Vehiculo vehiculo = new Vehiculo();
        //toUpperCase para guardar la matrícula siempre en mayúsculas
        vehiculo.setMatricula(dto.getMatricula().toUpperCase());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setCliente(cliente);
        return toDTO(vehiculoRepository.save(vehiculo));
    }
}
