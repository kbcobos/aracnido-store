# Arácnido Store

Proyecto de e-commerce con temática del universo arácnido, desarrollado en el curso de Back-End Java. El repositorio reúne las dos etapas del trabajo:

| Carpeta | Qué es | Tecnología |
|---|---|---|
| [`preentrega-consola/`](./preentrega-consola) | **Preentrega**: sistema de gestión de productos y pedidos por consola | Java puro (sin frameworks) |
| [`api/`](./api) | **Entrega final**: API REST del e-commerce | Spring Boot + MySQL |
| `frontend/` *(próximamente)* | Interfaz web que consume la API | Template personalizado |

## Entrega final: API REST (resumen)

Gestiona **productos, categorías, usuarios y pedidos**: CRUD completo, validaciones, manejo centralizado de errores (`@RestControllerAdvice`), control de stock al crear pedidos, estados de pedido e historial por usuario.

### Cómo ejecutarla

Necesitás **JDK 21**, **MySQL** corriendo en `localhost:3306` y **Maven**.

```bash
cd api
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
# editá application-local.properties y poné la contraseña de tu MySQL
mvn spring-boot:run
```

En PowerShell, la copia es: `Copy-Item src\main\resources\application-local.properties.example src\main\resources\application-local.properties`

La base `aracnido_store` y las tablas se crean solas, y se cargan datos de ejemplo. La API queda en **http://localhost:8080** (probá http://localhost:8080/api/productos).

### Endpoints principales

| Recurso | Endpoints |
|---|---|
| **Productos** | `GET/POST /api/productos` · `GET/PUT/DELETE /api/productos/{id}` · `GET /api/productos/stock-bajo` · `GET /api/productos?nombre=` `?min=&max=` `?orden=precio` · `GET /api/productos/categoria/{nombre}` · `GET /api/productos/valor-inventario` · `GET /api/productos/con-categoria` |
| **Categorías** | `GET/POST /api/categorias` · `GET/PUT/DELETE /api/categorias/{id}` · `GET /api/categorias/{id}/productos` |
| **Usuarios** | `GET/POST /api/usuarios` · `GET/PUT/DELETE /api/usuarios/{id}` · `GET /api/usuarios/{id}/pedidos` *(historial)* |
| **Pedidos** | `POST /api/pedidos` · `GET /api/pedidos` · `GET /api/pedidos/{id}` · `PATCH /api/pedidos/{id}/estado` |

**Detalle completo** (formato de las peticiones, reglas de negocio, códigos de error, estructura y tests): [`api/README.md`](./api/README.md)

La carpeta [`api/requests/`](./api/requests) tiene peticiones de ejemplo para probar cada endpoint con la extensión REST Client de VS Code.

## Autora

**Katherine Cobos**