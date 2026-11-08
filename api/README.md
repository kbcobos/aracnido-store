# Arácnido Store — API REST

API REST del e-commerce **Arácnido Store**, desarrollada con **Spring Boot + MySQL** para la entrega final del curso de Back-End Java. Gestiona productos, categorías, usuarios y pedidos, con control de stock, estados de pedido e historial por usuario.

## Tecnologías

- Java 21
- Spring Boot 3.3.4 (Spring Web, Spring Data JPA / Hibernate, Bean Validation)
- MySQL 8
- Maven
- JUnit 5 + Mockito (tests unitarios)

## Requisitos previos

- **JDK 21** o superior
- **MySQL** en ejecución en `localhost:3306` (con MySQL Workbench o el servicio de Windows)
- **Maven** (o la extensión *Extension Pack for Java* de VS Code, que lo incluye)
- Un cliente HTTP para probar; recomendado: la extensión **REST Client** de VS Code

## Cómo ejecutarlo

**1. Clonar el repositorio**
```bash
git clone https://github.com/kbcobos/aracnido-store.git
cd aracnido-store/api
```

**2. Configurar la contraseña de MySQL**

La contraseña real **no está en el repositorio**. Se guarda en un archivo local que Git ignora:

```bash
# desde api/
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```
En Windows (PowerShell):
```powershell
Copy-Item src\main\resources\application-local.properties.example src\main\resources\application-local.properties
```
Abrí `application-local.properties` y reemplazá `TU_CONTRASEÑA_DE_MYSQL` por la contraseña de tu usuario `root`. Si tu usuario no es `root`, cambialo en `application.properties` (`spring.datasource.username`).

**3. No hace falta crear la base ni las tablas**

La URL de conexión incluye `createDatabaseIfNotExist=true`, así que la API crea la base `aracnido_store`, y `spring.jpa.hibernate.ddl-auto=update` crea las tablas a partir de las entidades. La primera vez que arranca, `DataLoader` carga datos de ejemplo (3 categorías y 5 productos).

**4. Levantar la API**
```bash
mvn spring-boot:run
```
O desde VS Code: abrir `AracnidoStoreApiApplication.java` y apretar **Run** sobre el método `main`.

Cuando aparece `Started AracnidoStoreApiApplication`, la API está en **http://localhost:8080**. Probala abriendo http://localhost:8080/api/productos en el navegador.

**5. Probar los endpoints**

En la carpeta [`requests/`](./requests) hay archivos `.http` con una petición de ejemplo para cada endpoint (productos, categorías, usuarios y pedidos). Con la extensión REST Client, se ejecutan con el enlace **Send Request** que aparece sobre cada petición.

**6. Correr los tests** (18 tests unitarios, no necesitan MySQL)
```bash
mvn test
```

## Endpoints

Base: `http://localhost:8080/api`

### Productos — `/api/productos`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/productos` | Lista todos los productos | 200 |
| GET | `/productos?nombre=traje` | Busca por nombre (parcial, sin distinguir mayúsculas) | 200 |
| GET | `/productos?min=5000&max=40000` | Filtra por rango de precio | 200, 400 |
| GET | `/productos?orden=precio` | Ordena por precio, de mayor a menor | 200, 400 |
| GET | `/productos/{id}` | Detalle de un producto | 200, 404 |
| GET | `/productos/stock-bajo` | Productos con menos de 5 unidades (`?limite=10` cambia el umbral) | 200 |
| GET | `/productos/categoria/{nombre}` | Productos de una categoría, por nombre | 200 |
| GET | `/productos/valor-inventario` | Suma de `precio × stock` de todo el inventario | 200 |
| GET | `/productos/con-categoria` | Lista plana con el nombre de la categoría | 200 |
| POST | `/productos` | Crea un producto | 201, 400, 404, 409 |
| PUT | `/productos/{id}` | Actualiza un producto | 200, 400, 404, 409 |
| DELETE | `/productos/{id}` | Elimina un producto | 204, 404, 409 |

### Categorías — `/api/categorias`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/categorias` | Lista las categorías | 200 |
| GET | `/categorias/{id}` | Detalle de una categoría | 200, 404 |
| GET | `/categorias/{id}/productos` | Productos de una categoría, por id | 200, 404 |
| POST | `/categorias` | Crea una categoría | 201, 400, 409 |
| PUT | `/categorias/{id}` | Actualiza una categoría | 200, 400, 404, 409 |
| DELETE | `/categorias/{id}` | Elimina una categoría (409 si tiene productos) | 204, 404, 409 |

### Usuarios — `/api/usuarios`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/usuarios` | Lista los usuarios | 200 |
| GET | `/usuarios/{id}` | Detalle de un usuario | 200, 404 |
| POST | `/usuarios` | Crea un usuario (email único) | 201, 400, 409 |
| PUT | `/usuarios/{id}` | Actualiza un usuario | 200, 400, 404, 409 |
| DELETE | `/usuarios/{id}` | Elimina un usuario (409 si tiene pedidos) | 204, 404, 409 |
| GET | `/usuarios/{id}/pedidos` | **Historial de pedidos** del usuario | 200, 404 |

### Pedidos — `/api/pedidos`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| POST | `/pedidos` | Crea un pedido: valida y descuenta el stock, queda `PENDIENTE` | 201, 400, 404 |
| GET | `/pedidos` | Lista todos los pedidos (los más nuevos primero) | 200 |
| GET | `/pedidos/{id}` | Detalle de un pedido | 200, 404 |
| PATCH | `/pedidos/{id}/estado` | Cambia el estado del pedido | 200, 400, 404, 409 |

## Formato de las peticiones

**Crear / actualizar un producto** (`POST` / `PUT /api/productos`)
```json
{
  "nombre": "Guantes Arácnidos",
  "descripcion": "Guantes con agarre adhesivo",
  "precio": 15000.00,
  "stock": 10,
  "imagenUrl": "http://ejemplo.com/guantes.jpg",
  "categoriaId": 2
}
```

**Crear un pedido** (`POST /api/pedidos`)
```json
{
  "usuarioId": 1,
  "itemsPedido": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 2, "cantidad": 1 }
  ]
}
```

**Cambiar el estado** (`PATCH /api/pedidos/{id}/estado`)
```json
{ "estado": "CONFIRMADO" }
```

**Respuesta de un pedido**
```json
{
  "id": 1,
  "usuarioId": 1,
  "usuarioNombre": "Peter B. Parker",
  "fecha": "2026-11-06T10:35:48.028444",
  "estado": "PENDIENTE",
  "total": 179000.00,
  "items": [
    { "productoId": 1, "productoNombre": "Lanzarredes Mecánico",
      "cantidad": 2, "precioUnitario": 45000.00, "subtotal": 90000.00 },
    { "productoId": 2, "productoNombre": "Traje Arácnido Clásico",
      "cantidad": 1, "precioUnitario": 89000.00, "subtotal": 89000.00 }
  ]
}
```

## Reglas de negocio

- **Stock:** al crear un pedido se valida que haya stock de cada producto. Si no alcanza, responde **400** con `StockInsuficienteException` y no se descuenta nada (la operación es todo-o-nada, con `@Transactional`).
- **Precio congelado:** cada línea del pedido guarda el precio del producto al momento de la compra; cambiarlo después no altera pedidos anteriores.
- **Estados del pedido:**
  ```
  PENDIENTE → CONFIRMADO → ENVIADO → ENTREGADO
      └──────────┴──→ CANCELADO
  ```
  Un cambio no permitido responde **409**. Al **cancelar** un pedido, el stock se devuelve.
- **Alertas de stock:** `GET /api/productos/stock-bajo` lista los productos con menos de 5 unidades (valor configurable con `aracnido.stock.minimo`), y al crear un pedido se muestra una alerta por consola cuando un producto queda por debajo del mínimo.
- **Nombres únicos:** no se permiten dos productos o dos categorías con el mismo nombre, ni dos usuarios con el mismo email (sin distinguir mayúsculas) → **409**.
- **Dinero:** los precios usan `BigDecimal` (`DECIMAL(10,2)` en MySQL), para evitar errores de redondeo de `double`.

## Respuestas de error

Todos los errores tienen el mismo formato, gracias a `GlobalExceptionHandler` (`@RestControllerAdvice`):

```json
{
  "timestamp": "2026-11-06T10:40:12.123",
  "status": 404,
  "error": "Not Found",
  "mensaje": "No existe un producto con ID 99"
}
```

Cuando falla la validación de varios campos, se agrega el detalle por campo:

```json
{
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Hay datos inválidos en la solicitud",
  "errores": {
    "nombre": "El nombre es obligatorio",
    "precio": "El precio debe ser mayor a 0"
  }
}
```

| Código | Cuándo |
|---|---|
| 200 / 201 / 204 | Operación correcta / recurso creado / eliminado sin contenido |
| 400 | Datos inválidos, JSON mal formado, stock insuficiente, parámetro inválido |
| 404 | El producto, categoría, usuario o pedido no existe |
| 409 | Nombre o email repetido, transición de estado no permitida, o el registro está en uso (ej. categoría con productos) |

## CORS

La API acepta peticiones del navegador desde los orígenes listados en `aracnido.cors.origins` (`application.properties`): por defecto `localhost:5500` (Live Server), `3000` y `5173`.

## Autora

**Katherine Cobos** — Entrega final, curso Back-End Java.