package com.aracnidostore.dto;

import com.aracnidostore.model.Producto;

/**
 * JSON que SALE al consultar un producto. Devuelve el id de la categoría
 * y su nombre ya resueltos, en vez de la entidad Categoria completa:
 * así el frontend muestra la categoría sin pedir nada más, y evitamos
 * exponer las entidades JPA directamente (con sus relaciones y proxies
 * lazy, que dan problemas al convertirse a JSON).
 */
public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        double precio,
        int stock,
        String imagenUrl,
        Long categoriaId,
        String categoriaNombre
) {

    /** Convierte una entidad Producto en su versión para la API. */
    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagenUrl(),
                producto.getCategoria() != null ? producto.getCategoria().getId() : null,
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null
        );
    }
}
