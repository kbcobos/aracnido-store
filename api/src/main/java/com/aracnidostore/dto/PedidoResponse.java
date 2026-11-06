package com.aracnidostore.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.aracnidostore.model.EstadoPedido;
import com.aracnidostore.model.Pedido;

/** Pedido completo: número, fecha, estado, total y productos involucrados. */
public record PedidoResponse(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        LocalDateTime fecha,
        EstadoPedido estado,
        double total,
        List<LineaPedidoResponse> items
) {
    public static PedidoResponse from(Pedido p) {
        return new PedidoResponse(
                p.getId(),
                p.getUsuario().getId(),
                p.getUsuario().getNombre(),
                p.getFecha(),
                p.getEstado(),
                p.calcularTotal(),
                p.getLineas().stream().map(LineaPedidoResponse::from).toList());
    }
}
