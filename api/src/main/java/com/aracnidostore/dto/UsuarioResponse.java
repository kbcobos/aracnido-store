package com.aracnidostore.dto;

import com.aracnidostore.model.Usuario;

/** JSON que sale al consultar un usuario. */
public record UsuarioResponse(Long id, String nombre, String email) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail());
    }
}
