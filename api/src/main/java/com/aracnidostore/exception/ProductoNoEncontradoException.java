package com.aracnidostore.exception;

/**
 * Se lanza cuando se busca un producto por id y no existe. Es la misma
 * excepción que ya usabas en la preentrega de consola; extiende
 * RuntimeException (unchecked) por las mismas razones. La diferencia es
 * que ahora no la atrapa un try/catch en Main: la atrapa
 * GlobalExceptionHandler y la convierte en una respuesta HTTP 404.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con ID " + id);
    }
}
