package com.taller_mecanico.exceptions;

// Esta excepción se usa cuando el usuario envía datos incorrectos.
// Ej:
// Cantidad negativa
// Campos obligatorios vacíos
// Datos mal formateados
// Sirve para indicar que el problema NO es del servidor,
// sino de la petición del cliente (Postman, frontend, etc.)

public class BadRequestException extends RuntimeException {

    // Constructor que recibe el mensaje del error
    public BadRequestException(String message) {

        super(message);
    }
}
