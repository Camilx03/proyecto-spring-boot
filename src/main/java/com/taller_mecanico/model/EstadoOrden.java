package com.taller_mecanico.model;

public enum EstadoOrden {
    RECIBIDO, EN_REVISION, EN_REPARACION, LISTO, ENTREGADO
    //creado: cliente pide en la maquina
    //recepcionado: cliente confirma y entrega llave
    //en revision: el vehículo está en el elevador
    //listo: terminado, código sale en pantalla para su recogida en la sala de espera
    //entregado: cliente pago y se fue
}
