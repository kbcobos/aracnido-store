package com.aracnidostore.dto;

import com.aracnidostore.model.EstadoPedido;

import jakarta.validation.constraints.NotNull;

/** JSON para cambiar el estado: { "estado": "CONFIRMADO" } */
public record CambioEstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoPedido estado
) {
}
