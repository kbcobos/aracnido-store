package com.aracnidostore.exception;

/** Se lanza cuando se busca un pedido por id y no existe (HTTP 404). */
public class PedidoNoEncontradoException extends RuntimeException {

    public PedidoNoEncontradoException(Long id) {
        super("No existe un pedido con ID " + id);
    }
}
