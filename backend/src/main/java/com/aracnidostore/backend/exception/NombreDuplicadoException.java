package com.aracnidostore.backend.exception;

public class NombreDuplicadoException extends RuntimeException {

    public NombreDuplicadoException(String nombre) {
        super("Ya existe un registro con el nombre \"" + nombre + "\"");
    }
}
