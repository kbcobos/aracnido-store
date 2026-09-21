# Arácnido Store — Sistema de Gestión

Aplicación de **Java puro** para gestionar un catálogo de productos y crear pedidos, desarrollada como preentrega de la materia de backend.

## Requisitos cumplidos

- **Tipos de datos**: `int` (ids, cantidades, stock), `double` (precios), `String` (nombres/descripciones), `boolean` (control de flujo del menú).
- **Colecciones**: `ArrayList<Producto>` y `ArrayList<LineaPedido>` / `ArrayList<Pedido>`.
- **POO**: clases `Producto`, `Pedido`, `LineaPedido`, `ProductoService`, `PedidoService` y `Main`, con encapsulamiento (atributos privados + getters/setters).
- **Herencia y polimorfismo**: `Bebida` y `Comida` extienden `Producto`, cada una con su atributo propio (`volumenLitros` y `fechaVencimiento`), y se listan de forma genérica junto a los productos comunes.
- **Excepciones**: `try/catch` para `NumberFormatException` en cada lectura de datos por consola, y la excepción personalizada `StockInsuficienteException` (checked) al crear un pedido sin stock suficiente.
- **Paquetes**: código organizado en `com.aracnidostore.productos`, `com.aracnidostore.pedidos` y `com.aracnidostore.excepciones`.

## Cómo correrlo

**Desde VS Code**: abrir la carpeta del proyecto, abrir `src/Main.java` y usar el botón "Run" que aparece arriba del método `main`.

**Desde la terminal**, parado en la carpeta `src/`:
```bash
javac -d ../out Main.java DatosEjemplo.java com/aracnidostore/productos/*.java com/aracnidostore/pedidos/*.java com/aracnidostore/excepciones/*.java
java -cp ../out Main
```

Requiere Java 18 o superior.

## Catálogo de ejemplo

Al arrancar, `DatosEjemplo.java` precarga automáticamente un catálogo con temática del universo arácnido (héroes y villanos incluidos) y tres pedidos ya confirmados — uno genérico, uno de comida y uno de bebidas —, así que las opciones 2 y 6 del menú ya muestran datos desde el primer momento, sin necesidad de cargar nada a mano.

## Menú

```
========== SISTEMA DE GESTIÓN - ARÁCNIDO STORE ==========

1) Agregar producto
2) Listar productos
3) Buscar/Actualizar producto
4) Eliminar producto
5) Crear un pedido
6) Listar pedidos
7) Salir
```
