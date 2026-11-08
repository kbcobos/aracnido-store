package com.aracnidostore.exception;

/** Se lanza al crear o renombrar un producto con un nombre que ya existe (HTTP 409). */
public class ProductoDuplicadoException extends RuntimeException {

    public ProductoDuplicadoException(String nombre) {
        super("Ya existe un producto con el nombre: " + nombre);
    }
}
