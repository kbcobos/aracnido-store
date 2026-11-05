package com.aracnidostore.exception;

/**
 * Se lanza cuando se referencia una categoría (por ejemplo al crear un
 * producto con "categoriaId": 99) que no existe.
 */
public class CategoriaNoEncontradaException extends RuntimeException {

    public CategoriaNoEncontradaException(Long id) {
        super("No existe una categoría con ID " + id);
    }
}
