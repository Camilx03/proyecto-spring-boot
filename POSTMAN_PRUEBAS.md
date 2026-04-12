# Guía de pruebas - Taller Mecánico

Aquí explico paso a paso cómo levantar el proyecto y probar todos los endpoints.
Hay que seguir el orden porque cada cosa depende de lo anterior, no puedes crear
un vehículo si el cliente no existe todavía.

---

## Antes de empezar - Crear la base de datos

Abrimos phpMyAdmin en `http://localhost/phpmyadmin`, vamos a la pestaña SQL
y ejecutamos esto:

```sql
CREATE DATABASE IF NOT EXISTS taller_mecanico;
```

Solo con eso ya vale aunque si da algún error seguramente sea Mysql y su puerto, ya que nosotros tenemos el 3307. Hibernate se encarga de crear todas las tablas solo
cuando arrancamos la app. Una vez creada la base de datos arrancamos desde
IntelliJ con el botón ▶️ y esperamos a ver esto en la consola:

```
Tomcat started on port 8080
Started TallerMecanicoApplication
```

Si sale eso es que todo va bien y podemos empezar a probar.

---

## 1. Puestos

Lo primero es crear los puestos porque las órdenes los necesitan.
Un puesto de tipo TALLER genera códigos como T-0001 y uno de tipo ITV genera ITV-0001.

`POST http://localhost:8080/api/puestos`

```json
{
  "nombre": "Recepción",
  "tipo": "TALLER"
}
```

```json
{
  "nombre": "Box 1 - Mecánica",
  "tipo": "TALLER"
}
```

```json
{
  "nombre": "Box 2 - Electricidad",
  "tipo": "TALLER"
}
```

```json
{
  "nombre": "Línea ITV",
  "tipo": "ITV"
}
```

Para comprobar que se han creado bien:
`GET http://localhost:8080/api/puestos`

---

## 2. Categorías

Necesitamos las categorías antes de crear servicios porque cada servicio
pertenece a una.

`POST http://localhost:8080/api/categorias`

```json
{
  "nombre": "Mecánica general"
}
```

```json
{
  "nombre": "Electricidad"
}
```

```json
{
  "nombre": "ITV"
}
```

```json
{
  "nombre": "Chapa y pintura"
}
```

```json
{
  "nombre": "Neumáticos"
}
```

Verificamos: `GET http://localhost:8080/api/categorias`

---

## 3. Servicios

Aquí creamos el catálogo de servicios del taller. Hay que poner el categoriaId
que corresponde a cada uno según el orden en que los creamos arriba.

`POST http://localhost:8080/api/servicios`

```json
{
  "nombre": "Cambio de aceite y filtro",
  "precio": 49.90,
  "categoriaId": 1
}
```

```json
{
  "nombre": "Revisión de frenos",
  "precio": 35.00,
  "categoriaId": 1
}
```

```json
{
  "nombre": "Cambio de pastillas delanteras",
  "precio": 80.00,
  "categoriaId": 1
}
```

```json
{
  "nombre": "Diagnóstico eléctrico",
  "precio": 40.00,
  "categoriaId": 2
}
```

```json
{
  "nombre": "Cambio de batería",
  "precio": 120.00,
  "categoriaId": 2
}
```

```json
{
  "nombre": "Revisión ITV completa",
  "precio": 55.00,
  "categoriaId": 3
}
```

```json
{
  "nombre": "Preparación pre-ITV",
  "precio": 30.00,
  "categoriaId": 3
}
```

```json
{
  "nombre": "Reparación de abolladuras",
  "precio": 150.00,
  "categoriaId": 4
}
```

```json
{
  "nombre": "Cambio de neumático",
  "precio": 25.00,
  "categoriaId": 5
}
```

```json
{
  "nombre": "Equilibrado de ruedas",
  "precio": 12.00,
  "categoriaId": 5
}
```

Verificamos: `GET http://localhost:8080/api/servicios`

---

## 4. Clientes

`POST http://localhost:8080/api/clientes`

```json
{
  "nombre": "María García López",
  "telefono": "612345678",
  "email": "maria@email.com",
  "dni": "12345678A"
}
```

```json
{
  "nombre": "Carlos Pérez Ruiz",
  "telefono": "698765432",
  "email": "carlos@email.com",
  "dni": "87654321B"
}
```

```json
{
  "nombre": "Ana Martínez Soto",
  "telefono": "655111222",
  "email": "ana@email.com",
  "dni": "11223344C"
}
```

Verificamos: `GET http://localhost:8080/api/clientes`

---

## 5. Vehículos

Cada vehículo necesita el id del cliente al que pertenece.
María tiene id 1, Carlos id 2 y Ana id 3.

`POST http://localhost:8080/api/vehiculos`

```json
{
  "matricula": "1234-ABC",
  "marca": "Seat",
  "modelo": "Ibiza",
  "anio": 2018,
  "clienteId": 1
}
```

```json
{
  "matricula": "5678-DEF",
  "marca": "Volkswagen",
  "modelo": "Golf",
  "anio": 2020,
  "clienteId": 1
}
```

```json
{
  "matricula": "9012-GHI",
  "marca": "Ford",
  "modelo": "Focus",
  "anio": 2015,
  "clienteId": 2
}
```

```json
{
  "matricula": "3456-JKL",
  "marca": "Renault",
  "modelo": "Megane",
  "anio": 2019,
  "clienteId": 3
}
```

Verificamos: `GET http://localhost:8080/api/vehiculos`

---

## 6. Órdenes de trabajo

Cuando un coche entra al taller se crea una orden. El sistema genera el código
automáticamente según el tipo de puesto.

`POST http://localhost:8080/api/ordenes`

```json
{
  "vehiculoId": 1,
  "puestoId": 2,
  "observaciones": "Cliente indica ruido al frenar"
}
```

```json
{
  "vehiculoId": 3,
  "puestoId": 4,
  "observaciones": "Revisión ITV anual"
}
```

```json
{
  "vehiculoId": 4,
  "puestoId": 1,
  "observaciones": "Revisión general"
}
```

Verificamos: `GET http://localhost:8080/api/ordenes`

---

## 7. Añadir servicios a las órdenes

Ahora asociamos los servicios a cada orden. Si añades el mismo servicio
dos veces la cantidad se suma sola, no se duplica.

**Orden 1** → `POST http://localhost:8080/api/ordenes/1/servicios`

```json
{ "servicioId": 1, "cantidad": 1 }
```

```json
{ "servicioId": 2, "cantidad": 1 }
```

```json
{ "servicioId": 3, "cantidad": 1 }
```

**Orden 2** → `POST http://localhost:8080/api/ordenes/2/servicios`

```json
{ "servicioId": 6, "cantidad": 1 }
```

```json
{ "servicioId": 7, "cantidad": 1 }
```

**Orden 3** → `POST http://localhost:8080/api/ordenes/3/servicios`

```json
{ "servicioId": 1, "cantidad": 1 }
```

```json
{ "servicioId": 9, "cantidad": 4 }
```

Para ver el total calculado: `GET http://localhost:8080/api/ordenes/1`

---

## 8. Cambiar estados

Los estados solo pueden avanzar, nunca volver atrás.
El flujo es: RECIBIDO → EN_REVISION → EN_REPARACION → LISTO → ENTREGADO

`PATCH http://localhost:8080/api/ordenes/1/estado`

Los ejecutamos uno a uno en este orden:

```json
{ "estado": "EN_REVISION" }
```

```json
{ "estado": "EN_REPARACION" }
```

```json
{ "estado": "LISTO" }
```

```json
{ "estado": "ENTREGADO" }
```

---

## 9. Actualizar observaciones

`PATCH http://localhost:8080/api/ordenes/2/observaciones`

```json
{
  "observaciones": "También necesita pastillas traseras"
}
```

---

## 10. Filtros disponibles

Aquí probamos los distintos filtros que tiene la API.

`GET http://localhost:8080/api/ordenes?estado=RECIBIDO`
→ Muestra las órdenes recién llegadas que aún no se han atendido.

`GET http://localhost:8080/api/ordenes?estado=EN_REVISION`
→ Muestra las órdenes que el mecánico está inspeccionando ahora mismo.

`GET http://localhost:8080/api/ordenes?estado=EN_REPARACION`
→ Muestra las órdenes que están en proceso de reparación.

`GET http://localhost:8080/api/ordenes?estado=LISTO`
→ Muestra los coches ya terminados esperando a que el cliente los recoja.
Esta sería la pantalla de sala de espera.

`GET http://localhost:8080/api/ordenes?estado=ENTREGADO`
→ Muestra el historial de coches ya entregados a sus propietarios.

`GET http://localhost:8080/api/ordenes/codigo/T-0001`
→ El cliente busca su coche con el código que le dieron en recepción.

`GET http://localhost:8080/api/ordenes/codigo/ITV-0002`
→ Lo mismo pero para una orden de tipo ITV.

`GET http://localhost:8080/api/servicios?activo=true`
→ Muestra solo los servicios que están disponibles actualmente.
Los desactivados no salen.

`GET http://localhost:8080/api/servicios?categoria=1`
→ Filtra los servicios por categoría, en este caso Mecánica general.

`GET http://localhost:8080/api/servicios?activo=true&categoria=1&orden=precio`
→ Combina los tres filtros a la vez: solo activos, solo de mecánica,
ordenados de más barato a más caro.

`GET http://localhost:8080/api/clientes/1/vehiculos`
→ Muestra todos los coches que tiene registrados el cliente con id 1.

`GET http://localhost:8080/api/categorias/1/servicios`
→ Muestra todos los servicios que pertenecen a la categoría 1.

`GET http://localhost:8080/api/vehiculos/matricula/1234-ABC`
→ Busca un vehículo por matrícula. No importa si la escribes en mayúsculas
o minúsculas, el resultado es el mismo.

---

## 11. Pruebas de error

Aquí comprobamos que los errores se gestionan bien y devuelven
un JSON con mensaje, status y timestamp.

**404 - cliente que no existe:**
`GET http://localhost:8080/api/clientes/999`

**404 - orden con código que no existe:**
`GET http://localhost:8080/api/ordenes/codigo/T-9999`

**400 - matrícula duplicada** → `POST http://localhost:8080/api/vehiculos`

```json
{
  "matricula": "1234-ABC",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2022,
  "clienteId": 1
}
```

**400 - DNI duplicado** → `POST http://localhost:8080/api/clientes`

```json
{
  "nombre": "Otro Cliente",
  "telefono": "611111111",
  "dni": "12345678A"
}
```

**400 - intentar retroceder el estado:**

Primero avanzamos la orden 3 a EN_REVISION:

`PATCH http://localhost:8080/api/ordenes/3/estado`
```json
{ "estado": "EN_REVISION" }
```

Luego intentamos volver atrás y tiene que dar error:

`PATCH http://localhost:8080/api/ordenes/3/estado`
```json
{ "estado": "RECIBIDO" }
```

**400 - añadir un servicio desactivado:**

Primero lo desactivamos:
`PATCH http://localhost:8080/api/servicios/10/desactivar`

Luego intentamos añadirlo a una orden → `POST http://localhost:8080/api/ordenes/3/servicios`

```json
{
  "servicioId": 10,
  "cantidad": 1
}
```

**400 - modificar una orden ya entregada:**

Una vez la orden 1 está en ENTREGADO intentamos añadir un servicio:

`POST http://localhost:8080/api/ordenes/1/servicios`

```json
{
  "servicioId": 4,
  "cantidad": 1
}
```
