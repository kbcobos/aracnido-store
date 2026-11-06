package com.aracnidostore.model;

/**
 * Estados posibles de un pedido. Flujo permitido:
 *
 *   PENDIENTE -> CONFIRMADO -> ENVIADO -> ENTREGADO
 *       \            \
 *        +------------+--> CANCELADO
 *
 * ENTREGADO y CANCELADO son finales: de ahí no se puede pasar a nada.
 */
public enum EstadoPedido {
    PENDIENTE,
    CONFIRMADO,
    ENVIADO,
    ENTREGADO,
    CANCELADO;

    /** Indica si un pedido en este estado puede pasar al estado "nuevo". */
    public boolean puedePasarA(EstadoPedido nuevo) {
        return switch (this) {
            case PENDIENTE -> nuevo == CONFIRMADO || nuevo == CANCELADO;
            case CONFIRMADO -> nuevo == ENVIADO || nuevo == CANCELADO;
            case ENVIADO -> nuevo == ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }
}
