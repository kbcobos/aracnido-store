package com.aracnidostore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * JSON que ENTRA al crear o actualizar un producto. Coincide con el
 * ejemplo de la consigna:
 *
 *   { "nombre": "Café Premium", "descripcion": "Café colombiano tostado",
 *     "precio": 12.50, "categoriaId": 3, "imagenUrl": "http://...", "stock": 100 }
 *
 * No tiene "id" (lo asigna MySQL) ni la categoría completa (solo su id).
 */
public record ProductoRequest(

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        Double precio,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock,

        @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
        String imagenUrl,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {
}
