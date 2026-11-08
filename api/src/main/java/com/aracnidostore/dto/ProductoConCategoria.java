package com.aracnidostore.dto;

import java.math.BigDecimal;

public record ProductoConCategoria(
        Long id,
        String nombre,
        BigDecimal precio,
        int stock,
        String categoria
) {
}
