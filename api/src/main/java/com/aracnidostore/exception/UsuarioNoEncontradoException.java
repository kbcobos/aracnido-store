package com.aracnidostore.exception;

/** Se lanza cuando se busca un usuario por id y no existe (HTTP 404). */
public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("No existe un usuario con ID " + id);
    }
}
