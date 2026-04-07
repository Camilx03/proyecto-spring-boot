package com.taller_mecanico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


//Esta clase captura TODAS las excepciones de la aplicación.
//@RestControllerAdvice:
//Se ejecuta automáticamente cuando ocurre un error en cualquier controller
//Evita que Spring devuelva errores largos y difíciles de leer
//Permite devolver respuestas limpias y controladas en formato JSON

@RestControllerAdvice
public class GlobalExceptionHandler {

     //Maneja errores de tipo ResourceNotFoundException (404)
     //Se usa cuando el recurso no existe:
     //Ej: cliente no encontrado, orden inexistente etc...
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


     //Maneja peticiones inválidas (400)
     //Se usa cuando la petición del cliente es incorrecta
     //Ej: datos inválidos, campos incorrectos etc..
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


     //Maneja cualquier otro error no controlado (500)
     //Ej: errores internos del servidor etc..
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Metodo auxiliar que construye la respuesta en formato JSON
    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {

        Map<String, Object> body = new HashMap<>();

        body.put("mensaje", message);
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(status).body(body);
    }
}