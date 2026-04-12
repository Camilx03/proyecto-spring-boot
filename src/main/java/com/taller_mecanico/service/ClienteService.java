package com.taller_mecanico.service;

import com.taller_mecanico.dto.ClienteDTO;
import com.taller_mecanico.exceptions.BadRequestException;
import com.taller_mecanico.exceptions.ResourceNotFoundException;
import com.taller_mecanico.model.Cliente;
import com.taller_mecanico.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    //convertimos una entidad cliente en un clienteDTO, deberia ser privado pq solo lo usamos de este service
    private ClienteDTO toDTO(Cliente c){
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setTelefono(c.getTelefono());
        dto.setEmail(c.getEmail());
        dto.setDni(c.getDni());
        //contamos cuantos vehiculos tiene el cliente y si la lista es null devolvemos 0
        dto.setTotalVehiculos(c.getVehiculos() == null ? 0 : c.getVehiculos().size());
        return dto;
    }

    public List<ClienteDTO> listarTodos(){
        return clienteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorId(Long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cliente", id));
        return toDTO(cliente);
    }

    public ClienteDTO crear(ClienteDTO dto) {
        //si viene DNI verificamos que no exista ya en la base de datos
        if (dto.getDni() != null) {
            clienteRepository.findByDni(dto.getDni()).ifPresent(c -> {
                throw new BadRequestException(
                        "Ya existe un cliente con el DNI: " + dto.getDni());
            });
        }
        //creamos la entidad a partir del DTO
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setDni(dto.getDni());
        //guardamos y devolvemos el DTO con el id generado
        return toDTO(clienteRepository.save(cliente));
    }

    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
        //solo actualizamos los campos que no sean null en el DTO así si el cliente manda solo el teléfono no borramos el nombre.
        if (dto.getNombre() != null) cliente.setNombre(dto.getNombre());
        if (dto.getTelefono() != null) cliente.setTelefono(dto.getTelefono());
        if (dto.getEmail() != null) cliente.setEmail(dto.getEmail());
        if (dto.getDni() != null) cliente.setDni(dto.getDni());
        return toDTO(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }
}