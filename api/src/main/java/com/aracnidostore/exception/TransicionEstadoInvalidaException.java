package com.aracnidostore.exception;

import com.aracnidostore.model.EstadoPedido;

/** Se lanza al pedir un cambio de estado que el flujo no permite (HTTP 409). */
public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(EstadoPedido actual, EstadoPedido nuevo) {
        super("No se puede pasar un pedido de " + actual + " a " + nuevo);
    }
}
