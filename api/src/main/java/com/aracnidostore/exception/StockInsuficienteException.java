package com.aracnidostore.exception;

/**
 * Se lanza al crear un pedido con más unidades de las que hay en stock.
 * Es la misma excepción de la preentrega; ahora se convierte en HTTP 400.
 */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String producto, int disponible, int solicitado) {
        super("Stock insuficiente para '" + producto + "': disponible " + disponible
                + ", solicitado " + solicitado);
    }
}
