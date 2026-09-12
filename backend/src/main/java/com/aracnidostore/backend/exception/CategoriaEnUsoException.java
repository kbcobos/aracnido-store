package com.aracnidostore.backend.exception;

public class CategoriaEnUsoException extends RuntimeException {

    public CategoriaEnUsoException(String nombreCategoria) {
        super("No se puede eliminar la categoría \"" + nombreCategoria +
                "\" porque tiene productos asociados");
    }
}
