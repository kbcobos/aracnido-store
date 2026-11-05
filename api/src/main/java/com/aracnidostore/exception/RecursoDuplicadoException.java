package com.aracnidostore.exception;

/**
 * Se lanza cuando se intenta crear algo que ya existe y debe ser único
 * (una categoría con el mismo nombre, un usuario con el mismo email).
 * Se traduce a HTTP 409 Conflict.
 */
public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
