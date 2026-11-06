package com.aracnidostore.dto;

import com.aracnidostore.model.LineaPedido;

/** Una línea del pedido que sale: producto, cantidad, precio al momento de la compra y subtotal. */
public record LineaPedidoResponse(
        Long productoId,
        String productoNombre,
        int cantidad,
        double precioUnitario,
        double subtotal
) {
    public static LineaPedidoResponse from(LineaPedido l) {
        return new LineaPedidoResponse(
                l.getProducto().getId(),
                l.getProducto().getNombre(),
                l.getCantidad(),
                l.getPrecioUnitario(),
                l.getSubtotal());
    }
}
