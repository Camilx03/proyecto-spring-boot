package com.taller_mecanico.exceptions;


// Esta excepción la usamos cuando el sistema NO encuentra un recurso.
//Por ejemplo:
//Buscar un cliente que no existe
//Buscar una orden por ID que no está en la base de datos
//Buscar un vehículo inexistente
//En lugar de devolver null o un error genérico se lanza esta excepción para indicar claramente que el recurso no existe.


public class ResourceNotFoundException extends RuntimeException {

    // Constructor que recibe el mensaje del error
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " con id " + id + " no encontrado");
    }

}
