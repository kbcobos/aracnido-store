package com.aracnidostore.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * JSON que entra al crear un pedido (igual al ejemplo de la consigna):
 *
 *   { "usuarioId": 5, "itemsPedido": [ { "productoId": 10, "cantidad": 2 } ] }
 *
 */
public record PedidoRequest(
        @NotNull(message = "El usuarioId es obligatorio")
        Long usuarioId,

        @NotEmpty(message = "El pedido debe tener al menos un producto")
        @Valid
        List<ItemPedidoRequest> itemsPedido
) {
}
