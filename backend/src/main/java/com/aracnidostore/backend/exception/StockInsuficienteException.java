package com.aracnidostore.backend.exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String message) {
        super(message);
    }

    public StockInsuficienteException(String nombreProducto, Integer stockDisponible, Integer cantidadPedida) {
        super("Stock insuficiente para \"" + nombreProducto + "\": " +
                "disponible " + stockDisponible + ", pedido " + cantidadPedida);
    }
}
