package com.taller_mecanico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
Entidades principales.
puesto: Punto de recepción/atención (recepción, elevador 1, elevador ITV...)
cliente: Propietario del vehículo
vehiculo: Vehículo que entra al taller (matrícula, marca, modelo, año)
categoria: Categoría de servicio (mecánica, electricidad, ITV, chapa...)
servicio: Servicio ofrecido con precio (cambio aceite, revisión ITV, frenos...)
orden_trabajo: La orden generada al recibir el vehículo. Tiene código único, estado y total
orden_servicio: Tabla intermedia: qué servicios se hacen en una orden y con qué cantidad

Relaciones.
Un puesto tiene muchas orden_trabajo
Un cliente tiene muchos vehiculo
Un vehiculo tiene muchas orden_trabajo
Una orden_trabajo contiene muchos servicio a través de orden_servicio
Un servicio pertenece a una categoria

Estados de la orden de trabajo.
RECIBIDO → EN_REVISION → EN_REPARACION → LISTO → ENTREGADO

Endpoints requeridos
Puestos.
GET /api/puestos — listar puestos
POST /api/puestos — crear puesto payload: nombre, tipo

Clientes.
GET /api/clientes — listar clientes
POST /api/clientes — crear cliente
GET /api/clientes/{id} — buscar cliente

Vehículos.
GET /api/vehiculos — listar vehículos
POST /api/vehiculos — registrar vehículo incluye clienteId
GET /api/vehiculos/{id} — buscar por id
GET /api/vehiculos/matricula/{matricula} — buscar por matrícula

Categorías.
GET /api/categorias
POST /api/categorias

Servicios.
GET /api/servicios — listar filtros valorados: ?activo=true, ?categoria=1, ?orden=precio
POST /api/servicios — crear servicio
PUT /api/servicios/{id} — actualizar
PATCH /api/servicios/{id}/desactivar — desactivar servicio

Órdenes de trabajo.
POST /api/ordenes — registrar nueva orden, genera código automáticamente, payload: vehiculoId, puestoId
POST /api/ordenes/{ordenId}/servicios — añadir servicio a una orden payload: servicioId, cantidad
DELETE /api/ordenes/{ordenId}/servicios/{servicioId} — quitar servicio de una orden
PATCH /api/ordenes/{ordenId}/estado — cambiar estado
GET /api/ordenes/codigo/{codigo} — buscar orden por código para el cliente
GET /api/ordenes?estado=LISTO — listar órdenes filtrar por estado, ordenadas por fecha entrada ASC
GET /api/ordenes/{id} — detalle completo de una orden
*/
@Entity
@Table(name = "puestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Puesto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nombre del puesto: Recepción, Elevador 1, Elevador ITV.
    @Column(nullable = false)
    private String nombre;

    //tipo: Taller o ITV
    @Column(nullable = false)
    private String tipo;
}
