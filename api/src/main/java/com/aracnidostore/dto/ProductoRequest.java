package com.aracnidostore.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * JSON que ENTRA al crear o actualizar un producto.
 */
public record ProductoRequest(

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        Integer stock,

        @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
        String imagenUrl,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {
}
