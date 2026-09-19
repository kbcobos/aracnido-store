package com.aracnidostore.excepciones;

public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }

    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para \"" + nombreProducto + "\": " +
                "disponible " + stockDisponible + ", solicitado " + cantidadSolicitada);
    }
}
