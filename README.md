## Taller Mecánico API

Sistema backend desarrollado con Spring Boot para la gestión integral de un taller mecánico.

## DESCRIPCIÓN

Este proyecto implementa una API RESTful completa para la gestión de órdenes de trabajo en un taller mecánico.
El sistema está inspirado en el modelo de pedidos por terminal, donde:

- Cada solicitud genera un código único
- No existe autenticación de usuarios
- Todo gira alrededor del seguimiento del estado de la orden

## ARQUITECTURA

El proyecto sigue un aarquitectura en capas definida: 
Controller -> Service -> Repository -> Base de datos 

com.talle_mecanico
|_controller  -> API REST
|_Service     -> Lógica de negocio
|_repository  -> Acceso a dato (JPA)
|_model       -> Entidades (ORM)
|_dto         -> Trasferecia de datos
|_exceptions  -> Manejo de errores
|_TallerMecanicoAppplication

## MODELO DE DOMINIO
El sistema presenta entidades reales de un talleer mecanico. 
La principales
Cliente
Vehículo
Orden
Servicio
Categoria
Puesto
OrdenServicio

## RELACIONES DEL SISTEMA

Cliente 1 --- N vehiculo
Vehiculo 1 --- N orden
Orden 1 --- N OrdenServicio ---- 1 Servicio
Servicio N --- 1 categoria
Orden N --- 1 puesto

## FLUJO DE UNA ORDEN
RECIBIDO -> EN_REVISION -> EN_PREPARACION -> LISTO -> ENTREGADO
Este flujo representa el ciclo real dentro de un taller. 

## DTOs 
Se implementa una capa DTO para desacoplar la API del modelo interno. 
Ventajas
Evita exponer entidades JPA
Permite enriquecer respuesta
Reduce acoplamiento
Mejora rendimiento

 Ejemplo
 {
 "codigo": "ITV-AB123",
 "estado": "RECIBIDO",
 "clienteNombre": "Juan Pérez", 
 "matricula": 1234ABC",
 "total": 150.0
}

## SPRING DATA JPA
Se utiliza SPpring Data JPA para acceso a datos sin necesidad de SQL explicito.

Ejemplos:
findByDni(String dni)
findByMatriculaIgnoreCase(String matricula)
findByEstadoOrderByFechaentradasAsc(EstadoOrden estado)
findByActivoTrueAndCategoriaId(Long categoriaId)

## LOGICA DE NEGOCIO
Creacion de orden
- Genera código único automaticamente
- Estado inicial -> RECIBIDO
- Fecha de entrada automática
  
Gestión de servicios
- Añade servicios con cantidad
- Precio congelado en el momento de la inserción

Cáculo de total
total = suma(subtotales)

## MANEJO DE ERRORES
Implementado con @ResControllerAdvice
Tipos
BadRequest ->  400
NotFound -> 404
Error Interno -> 500

Formato: 
{
"mensaje": "Error",
"status": 400,
"timestamp": "2026-04-11t12:00:00"
}

## API REST (Endpoints principales)

CLIENTES

Metodo      Endpoint                         Descripcón

GET         /api/clientes                    Listar clientes
GET         /api/clientes/{id}               Buscar Clientes
GET         /api/clientes/{id}/vehiculos     Vehiculos del cliente
POST        /api/clientes                    Crear cliente
PUT         /api/clientes/{id}               Actualizar 
DELETE      /api/clientes/{id}               Eliminar

VEHICULOS

GET        /api/vehiculos                        Listar
GET        /api/vehiculos/{id}                   Buscar
GET        /api/vehiculo/matricula/{matricula}   Buscar por matricula
POST       /api/vehiculos                        Crear

CATEGORIAS

GET       /api/categorias                       Listar
POST      /api/categorias                       Crear
GET       /api/categorias/{id}/servicios        Servicio de categoria

PUESTOS
GET	      /api/puestos	                        Listar
GET	      /api/puestos/{id}	                    Buscar
POST	    /api/puestos	                        Crear
DELETE	  /api/puestos/{id}	                    Eliminar

ORDENES

GET	      /api/ordenes	                            Listar (filtro por estado)
GET	      /api/ordenes/{id}	                        Buscar por ID
GET	      /api/ordenes/codigo/{codigo}	            Buscar por código
POST	    /api/ordenes                              Crear orden
POST	    /api/ordenes/{id}/servicios	              Añadir servicio
DELETE	  /api/ordenes/{id}/servicios/{servicioId}	Quitar servicio
PATCH	    /api/ordenes/{id}/estado	                Cambiar estado
PATCH	    /api/ordenes/{id}/observaciones	          Actualizar observaciones


## FLUJO COMPLETO DEL SISTEMA

1 - CREAR CLIENTE
2 - REGISTRAR VEHICULO
3 - CREAR ORDEN
4 - AÑADIR SERVICIO
5 - CAMBIAR ESTADO
6 - CONSULTAR POR CODIGO

## EJECUCIÓN 
java 17+
Maven

Ejecutar 
mvn spring-boot: run

## Decisiones de diseño 
- Arquitectura en capas desacoplada
- DTOs para separación de modelos
- Tabla intermedia para relaciones N
- Soft delete en servicios
- Uso de Streams (programación funcional)
- Manejo global de errores


## MEJORAS FUTURAS
- Arquitectura profesional



## CONCLUSION

Este proyecto implementa una API backend completa con:

Diseño de dominio realista
Lógica de negocio sólida
Buenas prácticas REST
Arquitectura profesional
