package com.aracnidostore.dto;

import com.aracnidostore.model.Categoria;

/** JSON que sale al consultar una categoría. */
public record CategoriaResponse(Long id, String nombre) {

    public static CategoriaResponse from(Categoria c) {
        return new CategoriaResponse(c.getId(), c.getNombre());
    }
}
