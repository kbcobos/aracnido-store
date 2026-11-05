package com.aracnidostore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Forma única de todos los errores de la API, así el frontend siempre
 * sabe qué esperar. Ejemplo (producto inexistente):
 *
 *   { "timestamp": "2026-11-03T14:50:00", "status": 404,
 *     "error": "Not Found", "mensaje": "No existe un producto con ID 99" }
 *
 * "errores" solo aparece cuando falló la validación de varios campos a la
 * vez, con un mensaje por campo ({"precio": "El precio debe ser mayor a 0"}).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> errores
) {
}
